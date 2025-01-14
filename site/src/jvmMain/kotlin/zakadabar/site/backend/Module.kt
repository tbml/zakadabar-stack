/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend

import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.cookbook.entity.builtin.ExampleReferenceBl
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.authorize.SimpleRoleAuthorizerProvider
import zakadabar.core.data.EntityId
import zakadabar.core.module.modules
import zakadabar.core.route.RoutedModule
import zakadabar.core.server.server
import zakadabar.core.server.util.ContentBackend
import zakadabar.core.util.PublicApi
import zakadabar.lib.examples.backend.builtin.BuiltinBl
import zakadabar.lib.examples.backend.data.SimpleExampleBl
import zakadabar.site.backend.business.RecipeBl

@PublicApi
object Module : RoutedModule {

    override fun onModuleLoad() {
        server += ContentBackend("content")
        server += RecipeBl()

        server += SimpleRoleAuthorizerProvider {
            all = PUBLIC
        }

        server += BuiltinBl()
        server += zakadabar.lib.examples.backend.builtin.ExampleReferenceBl()
        server += ExampleReferenceBl()
        server += SimpleExampleBl()
    }

    override fun onModuleStart() {
        transaction {
            val pa = modules.first<ExampleReferenceBl>().pa

            if (pa.list().size < 10) {
                (1..10).forEach {
                    pa.create(ExampleReferenceBo(EntityId(), "ref $it"))
                }
            }
        }
    }
}