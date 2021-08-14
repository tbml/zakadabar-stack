/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.core.data.BaseBo
import zakadabar.core.data.schema.BoSchema
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.util.default

/**
 * DTO classes are usually defined in commonMain. This one here is to make the
 * example easier to write, but it is not accessible on the backend and it has
 * no communication feature.
 */
class DoubleExampleDto(
    var value: Double,
    var optValue: Double?,
    var invalidValue: Double,
    var readOnlyValue: Double
) : BaseBo {
    override fun schema() = BoSchema {
        + ::value
        + ::optValue
        + ::invalidValue default 1.0 notEquals 1.0
        + ::readOnlyValue
    }
}

/**
 * This example shows double form fields.
 */
class FormDoubleExample(
    element: HTMLElement
) : ZkForm<DoubleExampleDto>(element) {

    override fun onConfigure() {
        super.onConfigure()
        bo = default { }
        mode = ZkElementMode.Action
        setAppTitle = false
    }

    override fun onCreate() {
        super.onCreate()

        + section {
            + bo::value
            + bo::optValue
            + bo::invalidValue
            + bo::readOnlyValue readOnly true
        }

        // Make invalidValue touched, so the form will show styles.
        // This is just for the example, not needed in actual code.

        bo::invalidValue.find().touched = true
        validate()
    }

}