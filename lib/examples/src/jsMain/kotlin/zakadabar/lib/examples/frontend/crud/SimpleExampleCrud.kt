/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localized


/**
 * CRUD target for [SimpleExampleBo].
 * 
 * Generated with Bender at 2021-05-25T05:35:31.974Z.
 */
object SimpleExampleCrud : ZkCrudTarget<SimpleExampleBo>() {
    init {
        companion = SimpleExampleBo.Companion
        boClass = SimpleExampleBo::class
        editorClass = SimpleExampleForm::class
        tableClass = SimpleExampleTable::class
    }
}

/**
 * Form for [SimpleExampleBo].
 * 
 * Generated with Bender at 2021-05-25T05:35:31.974Z.
 */
class SimpleExampleForm : ZkForm<SimpleExampleBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<SimpleExampleForm>()) {
            + section {
                + bo::id
                + bo::name
            }
        }
    }
}

/**
 * Table for [SimpleExampleBo].
 * 
 * Generated with Bender at 2021-05-25T05:35:31.974Z.
 */
class SimpleExampleTable : ZkTable<SimpleExampleBo>() {

    override fun onConfigure() {

        titleText = localized<SimpleExampleTable>()

        add = true
        search = true
        export = true

        + SimpleExampleBo::id // record id and opt record id is not supported yet
        + SimpleExampleBo::name

        + actions()
    }
}