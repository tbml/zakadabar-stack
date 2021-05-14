/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.modal

import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.modal.ZkModalBase
import zakadabar.stack.frontend.builtin.modal.zkModalStyles
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

open class MyMessageDialog : ZkModalBase<String>() {

    override fun onCreate() {
        classList += zkModalStyles.modal

        + column {
            + div(zkModalStyles.content) {
                + "This is my message dialog."
            }

            + row(zkModalStyles.buttons) {
                + ZkButton("I will use translated strings instead", onClick = ::onOk)
            }
        }
    }

    open fun onOk() = io {
        channel.send("You promised!")
    }
}