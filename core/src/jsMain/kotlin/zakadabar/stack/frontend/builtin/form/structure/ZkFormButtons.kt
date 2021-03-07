/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.structure

import kotlinx.browser.window
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkBuiltinStrings.Companion.builtin
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormMode
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

open class ZkFormButtons<T : DtoBase>(
    private val form: ZkForm<T>
) : ZkElement() {

    override fun onCreate() {
        when (form.mode) {
            ZkFormMode.Create ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(builtin.save) { form.submit() }
                }

            ZkFormMode.Read -> {
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    form.openUpdate?.let { + submitButton(builtin.edit) { it(form.dto) } }
                }
            }
            ZkFormMode.Update ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(builtin.save) { form.submit() }
                }
            ZkFormMode.Delete ->
                + row(ZkFormStyles.buttons) {
                    + backButton()
                    + submitButton(builtin.delete) { form.submit() }
                }
            ZkFormMode.Action -> {
            }
            ZkFormMode.Query -> {
            }
        }
    }

    open fun backButton() =
        ZkButton(builtin.back) {
            var touched = false
            form.fields.forEach { touched = touched || it.touched }

            if (touched) {
                // TODO replace this with a styled dialog
                if (window.confirm(builtin.notSaved)) ZkApplication.back()
            } else {
                ZkApplication.back()
            }

        } marginRight 10

    open fun submitButton(text: String, onClick: (() -> Unit)? = null) =
        ZkButton(text, onClick).also { form.submitButton = it }

}