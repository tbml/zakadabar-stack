/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.backend.session

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.sessions.*
import zakadabar.stack.backend.authorize.AccountBlProvider
import zakadabar.stack.backend.ktor.KtorExecutor
import zakadabar.stack.backend.module
import zakadabar.stack.backend.server

private val accountBl by module<AccountBlProvider>()

class SessionAuthenticationProvider internal constructor(configuration: Configuration) : AuthenticationProvider(configuration) {
    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name)
}

fun Authentication.Configuration.configure(name: String? = null) {

    val provider = AuthenticationProvider(SessionAuthenticationProvider.Configuration(name))

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->

        call.sessions.get<StackSession>()?.let {
            context.principal(KtorExecutor(it.account, it.anonymous, it.roleIds, it.roleNames))
            return@intercept
        }

        val anonymous = accountBl.anonymous()
        val session = StackSession(anonymous.id, true, emptyList(), emptyList())

        call.sessions.set(session)
        context.principal(KtorExecutor(session.account, session.anonymous, session.roleIds, session.roleNames))

        if (call.attributes.getOrNull(LoginTimeoutKey) == true) {
            server.onLoginTimeout(call)
        }
    }

    register(provider)
}