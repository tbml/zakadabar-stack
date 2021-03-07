/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package zakadabar.stack.frontend.builtin.toast

import kotlinx.coroutines.delay
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginRight
import zakadabar.stack.frontend.util.plusAssign

/**
 * A toast to show a message to the user.
 */
open class ZkToast(
    val message: String,
    val type: ZkToastType,
    val hideAfter: Long? = null
) : ZkElement() {

    override fun onCreate() {
        className = ZkToastStyles.toastContent

        val typeClass = when (type) {
            ZkToastType.Info -> ZkToastStyles.info
            ZkToastType.Success -> ZkToastStyles.success
            ZkToastType.Warning -> ZkToastStyles.warning
            ZkToastType.Error -> ZkToastStyles.error
            ZkToastType.Custom -> ""
        }

        classList += typeClass

        + div { + message } marginRight 16
        + ZkIconButton(ZkIcons.close, cssClass = typeClass) { ZkApplication.toasts -= this }

        if (type == ZkToastType.Success || type == ZkToastType.Info || hideAfter != null) {
            io {
                delay(hideAfter ?: 3000)
                ZkApplication.toasts -= this
            }
        }
    }

    open fun dispose() {
        ZkApplication.toasts -= this
    }

}