/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

/**
 * Basic CSS classes used by ZkElement and ZkBuilder.
 */
class ZkClasses(theme: Theme) : CssStyleSheet<ZkClasses>(theme) {

    companion object {
        val zkClasses = ZkClasses(FrontendContext.theme).attach()
    }

    val w100 by cssClass {
        width = "100%"
    }

    val h100 by cssClass {
        height = "100%"
    }

    val hidden by cssClass {
        display = "none !important"
    }

    val row by cssClass {
        display = "flex"
        flexDirection = "row"
    }

    val column by cssClass {
        display = "flex"
        flexDirection = "column"
    }

    val grow by cssClass {
        flexGrow = 1
    }

}