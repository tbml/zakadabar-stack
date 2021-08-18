/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server

import com.charleskorn.kaml.Yaml
import zakadabar.core.setting.JvmSystemEnvHandler
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Bootstrap settings loader which runs before anything else.
 *
 * @param  useEnv  When true, environment variables are merged into setting BOs.
 */
open class ServerSettingLoader(
    open val useEnv : Boolean = true
) : JvmSystemEnvHandler {

    open fun load(settingsPath : String): ServerSettingsBo {

        val paths = listOf(
            settingsPath,
            "./stack.server.yml",
            "./etc/stack.server.yaml",
            "./etc/stack.server.yml",
            "../etc/stack.server.yaml",
            "../etc/stack.server.yml",
            "./template/app/etc/stack.server.yaml" // this is for development
        )

        for (p in paths) {
            val path = Paths.get(p)
            if (! Files.exists(path)) continue

            val source = Files.readAllBytes(path).decodeToString()

            settingsLogger.info("ServerSettingsBo source: ${path.toAbsolutePath()}")

            val bo = Yaml.default.decodeFromString(ServerSettingsBo.serializer(), source)
            bo.settingsDirectory = path.parent.toAbsolutePath().toString()

            if (useEnv) {
                mergeEnvironment(bo, "stack.server", System.getenv())
            }

            return bo
        }

        throw IllegalArgumentException("cannot locate server settings file")
    }



}