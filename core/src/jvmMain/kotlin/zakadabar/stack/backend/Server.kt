/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.custom.CustomBackend
import zakadabar.stack.backend.data.Sql
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.session.SessionBackend
import zakadabar.stack.backend.data.builtin.session.SessionStorageSql
import zakadabar.stack.backend.data.builtin.session.StackSession
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.account.AccountPublicDto
import zakadabar.stack.util.Executor
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import kotlin.reflect.full.isSubclassOf

val moduleLogger = LoggerFactory.getLogger("modules") !! // log module events

fun main(argv: Array<String>) = Server().main(argv)

class Server : CliktCommand() {

    companion object {
        /**
         * This variable contains the anonymous account. Each server should have one
         * this is used for public access.
         *
         * For example, check AccountPrivateBackend in the demo.
         */
        lateinit var anonymous: AccountPublicDto

        /**
         * Find an account by its id. Used by [SessionBackend].
         *
         * For example, check AccountPrivateBackend in the demo.
         *
         * @return the public account dto and the id of the principal that belongs to this account
         */
        lateinit var findAccountById: (accountId: Long) -> Pair<AccountPublicDto, Long>

        /**
         * Find an account by its id. Used by [SessionBackend].
         *
         * For example, check AccountPrivateBackend in the demo.
         *
         * @return the public account dto and the id of the principal that belongs to this account
         */
        lateinit var findAccountByName: (accountName: String) -> Pair<AccountPublicDto, Long>

        /**
         * When true GET (read and query) requests are logged by DTO backends.
         */
        var logReads: Boolean = true

        private val modules = mutableListOf<BackendModule>()

        private val dtoBackends = mutableListOf<RecordBackend<*>>()

        private val customBackends = mutableListOf<CustomBackend>()

        operator fun plusAssign(dtoBackend: RecordBackend<*>) {
            this.modules += dtoBackend
            this.dtoBackends += dtoBackend
            dtoBackend.onModuleLoad()
        }

        operator fun plusAssign(customBackend: CustomBackend) {
            this.modules += customBackend
            this.customBackends += customBackend
            customBackend.onModuleLoad()
        }
    }

    private val configPath
            by option("-c", "--config", help = "Path to the configuration file or directory.")
                .file(mustExist = true, mustBeReadable = true, canBeDir = true)
                .convert { it.path }
                .default("./zakadabar-config.yaml")

    override fun run() {

        val config = loadConfig()

        Sql.onCreate(config.database) // initializes SQL connection

        loadModules(config) // load modules

        Sql.onStart() // create missing tables and columns

        startModules() // start the modules

        // start the server

        val server = embeddedServer(Netty, port = config.ktor.port) {

            install(Sessions) {
                cookie<StackSession>("STACK_SESSION", SessionStorageSql) {
                    cookie.path = "/"
                }
            }

            install(Authentication) {
                session()

                basic(name = "basic") {
                    realm = config.serverName
                    validate {
                        val (account, principalId) = SessionBackend.authenticate(anonymous.id, it.name, it.password) ?: return@validate null
                        return@validate Executor(account.id, PrincipalBackend.roles(principalId))
                    }
                }

            }

            install(ContentNegotiation) {
                json()
            }

            install(WebSockets) {
                val c = config.ktor.websocket
                pingPeriod = Duration.ofSeconds(c.pingPeriod)
                timeout = Duration.ofSeconds(c.timeout)
                maxFrameSize = c.maxFrameSize
                masking = c.masking
            }

            install(StatusPages) {
                exception<Unauthorized> {
                    call.respond(HttpStatusCode.Unauthorized)
                }
                exception<EntityNotFoundException> {
                    call.respond(HttpStatusCode.NotFound)
                }
                status(HttpStatusCode.NotFound) {
                    val uri = call.request.uri
                    // this check is here so we will redirect only when needed
                    // api not found has to go directly to the browser, check
                    // for index.html is there to avoid recursive redirection
                    if (! uri.startsWith("/api") && uri != "/index.html") {
                        call.respondRedirect("/index.html")
                    }
                }
            }

            routing {

                authenticate {

                    get("health") {
                        call.respondText("OK", ContentType.Text.Plain)
                    }

                    route("api") {

                        // api installs add routes and the code to serve them
                        dtoBackends.forEach {
                            it.onInstallRoutes(this)
                        }

                        customBackends.forEach {
                            it.onInstallRoutes(this)
                        }

                    }

                    // TODO move static stuff under an URL like "static/"
                    static {
                        staticRootFolder = File(config.staticResources)
                        files(".")
                        default("index.html")
                    }

                }

            }
        }

        server.start(wait = true)
    }

    private fun loadConfig(): Configuration {

        val paths = listOf(
            configPath,
            "./zakadabar-server.yml",
            "./etc/zakadabar-server.yaml",
            "./etc/zakadabar-server.yml",
            "../etc/zakadabar-server.yaml",
            "../etc/zakadabar-server.yml",
            "./resources/app-template/etc/zakadabar-server.yaml" // this is for development
        )

        for (p in paths) {
            val path = Paths.get(p)
            if (! Files.exists(path)) continue

            val source = Files.readAllBytes(path).decodeToString()
            return Yaml.default.decodeFromString(Configuration.serializer(), source)
        }

        throw IllegalArgumentException("cannot locate configuration file")
    }

    private fun loadModules(config: Configuration) {

        config.modules.forEach {

            val installable = Server::class.java.classLoader.loadClass(it).kotlin

            require(installable.isSubclassOf(BackendModule::class)) { "module $it is not instance of BackendModule (maybe the name is wrong)" }

            try {

                val module = (installable.objectInstance as BackendModule)
                modules += module

                when (module) {
                    is RecordBackend<*> -> dtoBackends += module
                    is CustomBackend -> customBackends += module
                    else -> {
                        modules += module
                        module.onModuleLoad()
                    }
                }

                moduleLogger.info("loaded module $it")

            } catch (ex: Throwable) {
                moduleLogger.error("failed to load module $it")
                throw ex
            }

        }
    }

    private fun startModules() {
        modules.forEach {
            try {
                it.onModuleStart()
                moduleLogger.info("started module $it")
            } catch (ex: Throwable) {
                moduleLogger.error("failed to start module $it")
                throw ex
            }
        }
    }

}
