/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import kotlin.reflect.KProperty1

open class ZkBooleanColumn<T : DtoBase>(
    override val table: ZkTable<T>,
    private val prop: KProperty1<T, Boolean>
) : ZkColumn<T> {

    override var label = ZkApplication.strings.map[prop.name] ?: prop.name

    override fun render(builder: ZkElement, index: Int, row: T) {
        val checkbox = document.createElement("input") as HTMLInputElement
        checkbox.type = "checkbox"
        checkbox.value = prop.get(row).toString()
        checkbox.checked = prop.get(row)
        checkbox.style.cssText = "pointer-events:none"
        with(builder) {
            + checkbox
        }
    }

}