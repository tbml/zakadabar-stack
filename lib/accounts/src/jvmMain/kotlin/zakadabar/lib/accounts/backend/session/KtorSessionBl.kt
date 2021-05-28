/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend.session

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.sessions.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.*
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.data.builtin.resources.setting
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.ktor.KtorRouter
import zakadabar.stack.backend.ktor.KtorSessionProvider
import zakadabar.stack.backend.ktor.executor
import zakadabar.stack.backend.module
import zakadabar.stack.backend.persistence.EmptyPersistenceApi
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.builtin.account.LoginAction
import zakadabar.stack.data.builtin.account.LogoutAction
import zakadabar.stack.data.builtin.account.SessionBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.builtin.misc.ServerDescriptionBo
import zakadabar.stack.data.entity.EntityId
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

class KtorSessionBl : EntityBusinessLogicBase<SessionBo>(
    boClass = SessionBo::class
), KtorSessionProvider {

    override val pa = EmptyPersistenceApi<SessionBo>()

    override val authorizer = object : Authorizer<SessionBo> {
        override fun authorizeRead(executor: Executor, entityId: EntityId<SessionBo>) {
            // everyone can read their own session
        }

        override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
            when (actionBo) {
                is LoginAction -> return
                is LogoutAction -> return
                else -> throw Forbidden()
            }
        }
    }

    override val auditor = auditor {
        includeData = false
    }

    override val router = object : KtorRouter<SessionBo>(this) {

        init {
            action(LoginAction::class, ::loginAction)
            action(LogoutAction::class, ::logoutAction)
        }

        override suspend fun read(call: ApplicationCall, id: String) {
            apiCacheControl(call)
            read(call)
        }

        override suspend fun action(call: ApplicationCall, actionClass: KClass<out BaseBo>, actionFunc: (Executor, BaseBo) -> BaseBo) {
            val executor = call.executor()
            val aText = call.receive<String>()
            val aObj = Json.decodeFromString(serializer(actionClass.createType()), aText) as BaseBo

            val response = when (aObj) {
                is LoginAction -> loginAction(call, executor, aObj)
                is LogoutAction -> logoutAction(call, executor)
                else -> throw NotImplementedError()
            }

            @Suppress("UNCHECKED_CAST")
            call.respond(response as Any)
        }

    }

    private val serverDescription by setting<ServerDescriptionBo>("zakadabar.server.description")

    private val accountBl by module<AccountBlProvider>()

    override fun onModuleLoad() {
        Sql.tables += SessionTable
    }

    fun read(call: ApplicationCall) = transaction {
        val session = call.sessions.get<StackSession>() ?: throw IllegalStateException()

        val anonymous = accountBl.anonymous()

        if (session.account == anonymous.id) {
            SessionBo(
                id = EntityId("current"),
                account = anonymous,
                anonymous = true,
                roles = emptyList(),
                serverDescription = serverDescription
            )
        } else {
            val account = accountBl.readPublic(session.account)
            val roles = accountBl.roles(account.id)
            SessionBo(
                id = EntityId("current"),
                account = account,
                anonymous = false,
                roles = roles.map { it.second },
                serverDescription = serverDescription
            )
        }
    }

    @Suppress("UNUSED_PARAMETER") // action is needed here because of route mapping
    private fun loginAction(executor: Executor, action: LoginAction): ActionStatusBo {
        throw IllegalStateException("reached placeholder action")
    }

    private fun loginAction(call: ApplicationCall, executor: Executor, action: LoginAction): ActionStatusBo {

        val account = try {
            authenticate(executor, action.accountName, action.password) ?: return ActionStatusBo(false)
        } catch (ex: AccountLockedException) {
            return ActionStatusBo(false, "locked")
        }

        val roleIds = mutableListOf<EntityId<out BaseBo>>()
        val roleNames = mutableListOf<String>()

        accountBl.roles(account.id).forEach {
            roleIds += it.first
            roleNames += it.second
        }

        if (StackRoles.siteMember !in roleNames) return ActionStatusBo(false)

        call.sessions.set(StackSession(account.id, accountBl.anonymous().id == account.id, roleIds, roleNames))

        return ActionStatusBo(true)
    }

    private fun authenticate(executor: Executor, accountName: String, password: Secret): AccountPublicBo? {
        val account = try {

            accountBl.authenticate(executor, accountName, password)

        } catch (ex: InvalidCredentials) {
            auditor.auditCustom(executor) { "login result=fail name=${accountName}" }
            return null
        } catch (ex: NoSuchElementException) {
            auditor.auditCustom(executor) { "login result=fail name=${accountName}" }
            return null
        } catch (ex: AccountLockedException) {
            auditor.auditCustom(executor) { "login result=locked name=${accountName}" }
            throw ex
        } catch (ex: AccountExpiredException) {
            auditor.auditCustom(executor) { "login result=expired name=${accountName}" }
            return null
        } catch (ex: AccountNotValidatedException) {
            auditor.auditCustom(executor) { "login result=not-validated name=${accountName}" }
            return null
        } catch (ex: Exception) {
            auditor.auditCustom(executor) { "login result=error name=${accountName} ${ex.localizedMessage}" }
            throw ex
        }

        auditor.auditCustom(executor) { ("login result=success new-account=${account.id} name=${accountName}") }

        return account
    }

    @Suppress("UNUSED_PARAMETER") // action is needed here because of route mapping
    private fun logoutAction(executor: Executor, action: LogoutAction): ActionStatusBo {
        throw IllegalStateException("reached placeholder action")
    }

    private fun logoutAction(call: ApplicationCall, executor: Executor): ActionStatusBo {

        val old = call.sessions.get<StackSession>() !!

        val anonymous = accountBl.anonymous()

        auditor.auditCustom(executor) { "logout old=${old.account} new=${anonymous.id}" }

        call.sessions.set(StackSession(anonymous.id, true, emptyList(), emptyList()))

        return ActionStatusBo(success = true)

    }

    override fun configure(conf : Sessions.Configuration) {
        with(conf) {
            val sessionType = StackSession::class
            val name = "ZKL_SESSION"

            @Suppress("DEPRECATION") // as in Ktor code
            val builder = CookieIdSessionBuilder(sessionType).apply {
                cookie.path = "/"
            }
            val transport = SessionTransportCookie(name, builder.cookie, builder.transformers)
            val tracker = RenewableSessionTrackerById(sessionType, StackSessionSerializer, SessionStorageSql, builder.sessionIdProvider)
            val provider = SessionProvider(name, sessionType, transport, tracker)
            register(provider)

            SessionMaintenanceTask.start()
        }
    }

    override fun configure(conf: Authentication.Configuration) {
        conf.configure()
    }

}