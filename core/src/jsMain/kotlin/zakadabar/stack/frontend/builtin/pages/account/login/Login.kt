/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.stack.frontend.builtin.pages.account.login

import kotlinx.browser.window
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarStyles
import zakadabar.stack.frontend.util.marginBottom

/**
 * A simple username / password login page. When the login is
 * successful the page is reloaded by the browser to let all
 * components use the proper account information.
 */
object Login : ZkPage(ZkFullScreenLayout) {

    override fun onCreate() {

        + column {

            style {
                alignItems = "center"
                height = "100vh"
                width = "100vw"
            }

            + gap(height = 60)

            + div {

                style {
                    width = "300px"
                }

                + div(ZkTitleBarStyles.appTitleBar) {
                    style {
                        minHeight = "unset"
                        maxHeight = "unset"
                        paddingBottom = "${theme.layout.spacingStep / 2}px"
                    }
                    + strings.applicationName
                } marginBottom 20

                + LoginForm(
                    onCancel = { window.history.back() },
                    onSuccess = { window.location.pathname = "/" }
                )
            }
        }
    }

}