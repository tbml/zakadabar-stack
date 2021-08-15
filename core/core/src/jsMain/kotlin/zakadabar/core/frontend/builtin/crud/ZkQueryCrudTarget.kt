/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.crud

import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityBo
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.pages.zkPageStyles
import zakadabar.core.frontend.builtin.table.ZkTable
import zakadabar.core.frontend.util.io
import zakadabar.core.frontend.util.newInstance
import kotlin.reflect.KClass

/**
 * Routing target for CRUD pages which a query, create, read, update, delete.
 * the query uses table, others use form. Intended for top-level pages.
 * If you would like to include a crud on a page, use [ZkInlineQueryCrud].
 *
 * This class **does not** handle the table automatically as [ZkCrudTarget]
 * does, you have to do it yourself. See the documentation for details.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkQueryCrudTarget<T : EntityBo<T>, ET : BaseBo> : ZkCrudTarget<T>() {

    lateinit var queryTableClass: KClass<out ZkTable<ET>>

    override fun all(): ZkElement {

        val container = ZkElement()

        io {
            val table = queryTableClass.newInstance()
            container build {
                + zkPageStyles.fixed
                + table
            }
        }

        return container
    }

}
