/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.toast

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.button.buttonSecondary
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.pages.zkPageStyles
import zakadabar.core.frontend.builtin.toast.ZkToast
import zakadabar.core.frontend.resources.theme
import zakadabar.core.frontend.util.default

/**
 * This example shows how to create toasts.
 */
class ToastFormExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + column(zkPageStyles.content) {

            + buttonSecondary("Open as Toast") {
                ZkToast(content = InlineForm(), hideAfter = null).run()
            } marginBottom theme.spacingStep

            + ZkToast(content = InlineForm(), hideAfter = null)
        }
    }

    class InlineForm : ZkForm<BuiltinBo>() {

        override fun onConfigure() {
            bo = default { }
            mode = ZkElementMode.Action
            setAppTitle = false
        }

        override fun onCreate() {
            super.onCreate()

            element.style.margin = "20px"

            + section("This is a form in a toast!") {
                + bo::id
                + bo::doubleValue
                + bo::instantValue
                + bo::optInstantValue
                + bo::stringValue
            }
        }
    }
}