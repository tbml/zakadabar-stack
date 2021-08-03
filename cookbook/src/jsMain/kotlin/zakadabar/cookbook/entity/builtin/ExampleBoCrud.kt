/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.entity.builtin

import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.crud.ZkInlineCrud
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.ZkStringField
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.resources.localized
import zakadabar.stack.resources.localizedStrings

class ExampleBoCrud : ZkCrudTarget<ExampleBo>()  {
    init {
        companion = ExampleBo.Companion
        boClass = ExampleBo::class
        editorClass = ExampleBoForm::class
        tableClass = ExampleBoTable::class
    }
}

class ExampleBoInlineCrud : ZkInlineCrud<ExampleBo>() {
    init {
        companion = ExampleBo.Companion
        boClass = ExampleBo::class
        editorClass = ExampleBoForm::class
        tableClass = ExampleBoTable::class
    }
}

class ExampleBoForm : ZkForm<ExampleBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<ExampleBoForm>()) {
            + section {
                + bo::id
                + bo::booleanValue
                + bo::doubleValue
                + bo::enumSelectValue
                + bo::instantValue
                + bo::intValue
                + bo::localDateValue
                + bo::localDateTimeValue
                + opt(bo::optBooleanValue, localizedStrings.trueText, localizedStrings.falseText)
                + bo::optDoubleValue
                + bo::optEnumSelectValue
                + bo::optInstantValue
                + bo::optIntValue
                + bo::optLocalDateValue
                + bo::optLocalDateTimeValue readOnly true
                + bo::optSecretValue
                + bo::optRecordSelectValue options { selectBy { it.name } }
                + bo::optStringValue
                + select(bo::optStringSelectValue, options = listOf("option 1", "option 2", "option3"))
                + bo::optTextAreaValue
                + bo::optUuidValue
                + bo::secretValue
                + bo::recordSelectValue options { selectBy { it.name } }
                + bo::recordSelectValue options { selectBy { it.name } readOnly true }
                + ZkStringField(this@ExampleBoForm, bo::stringValue).also { this@ExampleBoForm.fields += it }
                + select(bo::stringSelectValue, options = listOf("option 1", "option 2", "option3"))
                + textarea(bo::textAreaValue)
                + bo::uuidValue
            }
        }
    }
}

class ExampleBoTable : ZkTable<ExampleBo>() {

    override fun onConfigure() {

        titleText = localized<ExampleBoTable>()

        add = true
        search = true
        export = true

        // ExampleBo::id // record id and opt record id is not supported yet 
        + ExampleBo::booleanValue
        + ExampleBo::doubleValue
        + ExampleBo::enumSelectValue
        + ExampleBo::instantValue
        + ExampleBo::intValue
        + ExampleBo::localDateValue
        + ExampleBo::localDateTimeValue
        // + ExampleBo::optBooleanValue // opt boolean is not supported yet
        + ExampleBo::optDoubleValue
        // + ExampleBo::optEnumSelectValue // opt enum is not supported yet
        + ExampleBo::optIntValue
        + ExampleBo::optInstantValue
        + ExampleBo::optLocalDateValue
        + ExampleBo::optLocalDateTimeValue
        // + ExampleBo::optSecretValue // not supported yet
        // ExampleBo::optRecordSelectValue // record id and opt record id is not supported yet 
        + ExampleBo::optStringValue
        + ExampleBo::optStringSelectValue
        + ExampleBo::optTextAreaValue
        + ExampleBo::optUuidValue
        // + ExampleBo::secretValue // not supported yet
        // ExampleBo::recordSelectValue // record id and opt record id is not supported yet 
        + ExampleBo::stringValue
        + ExampleBo::stringSelectValue
        + ExampleBo::textAreaValue
        + ExampleBo::uuidValue

        + actions()
    }
}