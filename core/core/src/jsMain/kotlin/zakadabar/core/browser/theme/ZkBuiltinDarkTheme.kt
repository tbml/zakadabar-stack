/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.theme

import zakadabar.core.browser.layout.zkScrollBarStyles
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.ZkTheme
import zakadabar.core.util.after
import zakadabar.core.util.alpha

open class ZkBuiltinDarkTheme : ZkTheme {

    companion object {
        const val NAME = "zakadabar.stack.theme.dark"
    }

    override val name = NAME

    override var fontFamily = "Roboto, system-ui, -apple-system, BlinkMacSystemFont, sans-serif, Lato"
    override var fontSize: String = "16px"
    override var fontWeight: String = "300"

    override var backgroundColor = ZkColors.Zakadabar.gray8
    override var textColor = ZkColors.Zakadabar.gray2

    override var hoverBackgroundColor = "rgba(255,255,255,0.2)"
    override var hoverTextColor = ZkColors.Zakadabar.gray2

    override var primaryColor = ZkColors.Zakadabar.navPurple
    override var primaryPair = ZkColors.white
    override var secondaryColor = ZkColors.Zakadabar.gray3
    override var secondaryPair = ZkColors.Zakadabar.gray8
    override var successColor = ZkColors.Zakadabar.navGreen
    override var successPair = ZkColors.Zakadabar.gray8
    override var warningColor = ZkColors.Zakadabar.navOrange
    override var warningPair = ZkColors.Zakadabar.gray8
    override var dangerColor = ZkColors.Zakadabar.navRed
    override var dangerPair = ZkColors.white
    override var infoColor = ZkColors.Zakadabar.navBlue
    override var infoPair = ZkColors.white
    override var disabledColor = ZkColors.Zakadabar.gray4
    override var disabledPair = ZkColors.Zakadabar.gray7

    override var inputTextColor by after { textColor }
    override var inputBackgroundColor = ZkColors.Zakadabar.gray8

    override var disabledInputTextColor = ZkColors.Zakadabar.gray2
    override var disabledInputBackgroundColor = ZkColors.Zakadabar.gray6

    override var borderColor = ZkColors.Gray.c600

    override var border = "transparent"

    override var cornerRadius = 2
    override var spacingStep = 20

    override var boxShadow = "none" // "0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)"
    override var fixBorder = "1px solid ${ZkColors.Zakadabar.gray4}"
    override var blockBackgroundColor = ZkColors.Zakadabar.gray7

    override fun onResume() {
        onResumeScrollBar()
        onResumeTitleBar()
    }

    open fun onResumeScrollBar() {
        with(zkScrollBarStyles) {
            thumbColor = textColor.alpha(0.5)
            trackColor = backgroundColor
        }
    }

    open fun onResumeTitleBar() {
        @Suppress("DuplicatedCode") // no need to make this more complex
        with(zkTitleBarStyles) {
            appHandleBackground = ZkColors.Zakadabar.gray7
            appHandleText = textColor
            appHandleBorder = border
            appTitleBarBackground = ZkColors.Zakadabar.gray7
            appTitleBarText = textColor
            appTitleBarBorder = border
        }
    }
}