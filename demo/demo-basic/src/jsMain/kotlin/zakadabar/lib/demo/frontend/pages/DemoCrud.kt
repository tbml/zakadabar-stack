/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend.pages

import zakadabar.lib.blobs.frontend.image.ZkImagesField
import zakadabar.lib.demo.data.DemoBlob
import zakadabar.lib.demo.data.DemoBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.resources.localized

/**
 * CRUD target for [DemoBo].
 * 
 * Generated with Bender at 2021-06-04T02:35:21.583Z.
 */
class DemoCrud : ZkCrudTarget<DemoBo>() {
    init {
        companion = DemoBo.Companion
        boClass = DemoBo::class
        editorClass = DemoForm::class
        tableClass = DemoTable::class
    }
}

class DemoForm : ZkForm<DemoBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<DemoForm>()) {
            + section {
                + bo::id
                + bo::name
                + bo::value
            }

            + ZkImagesField(this, DemoBlob.comm, bo.id) {
                DemoBlob(EntityId(), bo.id, "image", it.name, it.type, it.size.toLong())
            }
        }

    }
}

/**
 * Table for [DemoBo].
 * 
 * Generated with Bender at 2021-06-04T02:35:21.584Z.
 */
class DemoTable : ZkTable<DemoBo>() {

    override fun onConfigure() {

        crud = target<DemoCrud>()

        titleText = localized<DemoTable>()

        add = true
        search = true
        export = true

        + DemoBo::id
        + DemoBo::name
        + DemoBo::value

        + actions()
    }

}