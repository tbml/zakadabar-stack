/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.frontend

import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable


/**
 * CRUD target for [LocaleBo].
 *
 * Generated with Bender at 2021-05-30T09:24:34.063Z.
 */
class Locales : ZkCrudTarget<LocaleBo>() {
    init {
        companion = LocaleBo.Companion
        boClass = LocaleBo::class
        pageClass = LocaleForm::class
        tableClass = LocaleTable::class
    }
}

/**
 * Form for [LocaleBo].
 *
 * Generated with Bender at 2021-05-30T09:24:34.064Z.
 */
class LocaleForm : ZkForm<LocaleBo>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<LocaleForm>()) {
            + section {
                + bo::id
                + bo::name
                + bo::description
            }
        }
    }
}

/**
 * Table for [LocaleBo].
 *
 * Generated with Bender at 2021-05-30T09:24:34.064Z.
 */
class LocaleTable : ZkTable<LocaleBo>() {

    override fun onConfigure() {

        crud = target<Locales>()

        titleText = translate<LocaleTable>()

        add = true
        search = true
        export = true

        // LocaleBo::id // record id and opt record id is not supported yet
        + LocaleBo::name
        + LocaleBo::description

        + actions()
    }
}