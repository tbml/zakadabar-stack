/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.data

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.lib.examples.backend.data.SimpleStandaloneActionBl
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.data.builtin.account.AccountPrivateBackend
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.role.RoleBackend
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantBackend
import zakadabar.stack.backend.data.builtin.session.SessionBackend
import zakadabar.stack.backend.server
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.entity.EmptyEntityBo
import zakadabar.stack.data.entity.EntityComm
import kotlin.test.assertEquals

class SimpleStandaloneActionTest {

    companion object {

        @BeforeClass
        @JvmStatic
        fun setup() {
            server = Server()
            server += RoleGrantBackend
            server += RoleBackend
            server += PrincipalBackend
            server += AccountPrivateBackend
            server += SessionBackend
            server += object : SimpleStandaloneActionBl() {
                override val authorizer = object : Authorizer<EmptyEntityBo> {
                    override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
                        // allow the action
                    }
                }
            }
            server.main(arrayOf("--test"))
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            server.ktorServer.stop(1000, 10000)
        }
    }

    @Test
    fun testSimpleStandaloneAction() {
        runBlocking {
            EntityComm.baseUrl = "http://127.0.0.1:8888"
            assertEquals("test action", SimpleStandaloneAction(name = "ize").execute().reason)
        }
    }
}