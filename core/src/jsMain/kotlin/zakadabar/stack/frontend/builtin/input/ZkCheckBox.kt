/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.input

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.resources.ZkIconSource

open class ZkCheckBox(
    open val iconSource: ZkIconSource,
    checked: Boolean = false,
    val onChange: ((Boolean) -> Unit)? = null
) : ZkElement() {

    open val checkbox = document.createElement("input") as HTMLInputElement
    open val label = document.createElement("label") as HTMLLabelElement

    open var disabled: Boolean
        get() = checkbox.disabled
        set(value) {
            checkbox.disabled = value
        }

    open var checked: Boolean = checked
        set(value) {
            checkbox.checked = value
            field = value
        }

    override fun onCreate() {
        checkbox.id = "${this.id}-checkbox"
        checkbox.type = "checkbox"
        checkbox.className = zkInputStyles.checkBox
        checkbox.checked = checked
        + checkbox

        label.htmlFor = checkbox.id
        label.className = zkInputStyles.checkboxLabel
        + label

        label.innerHTML = iconSource.svg(18)

        on(checkbox, "click", ::onChange)
    }

    open fun onChange(event: Event) {
        onChange?.invoke(checkbox.checked)
    }

}