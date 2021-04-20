/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.lib.modal

import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.modal.ZkConfirmDialog
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.util.io

/**
 * This example shows how to create checkbox lists.
 */
object ConfirmDialog : ZkPage() {

    private val output = ZkElement()

    override fun onCreate() {
        super.onCreate()

        + column(ZkPageStyles.content) {
            + ZkButton(strings.show.capitalize(), ::onShowDialog)
            + output
        }
    }

    private fun onShowDialog() {
        io {
            val value = ZkConfirmDialog("Dialog Title", "Message to tell the user what to confirm").run()

            output.clear()
            output.build { + "selected option: $value" }
        }
    }

}