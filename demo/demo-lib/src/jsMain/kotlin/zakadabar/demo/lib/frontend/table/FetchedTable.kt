/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.lib.frontend.table

import zakadabar.demo.lib.data.builtin.BuiltinDto
import zakadabar.demo.lib.frontend.crud.BuiltinTable
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.util.io

/**
 * This example shows all built in table columns with generated table data.
 */
object FetchedTable : ZkPage(cssClass = ZkPageStyles.fixed) {

    override fun onCreate() {
        super.onCreate()
        io {
            // Add the table and set the data. It is not important to use these
            // together, you can add the table and set the data later.

            + BuiltinTable().setData(BuiltinDto.all())
        }
    }

}