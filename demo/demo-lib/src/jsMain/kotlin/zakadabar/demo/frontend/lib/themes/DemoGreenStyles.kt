/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.lib.themes

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

class DemoGreenStyles : ZkCssStyleSheet<ZkTheme>() {

    val exampleStyle by cssClass {
        color = ZkColors.white
        padding = 20
        backgroundColor = ZkColors.Green.c600

        hover {
            backgroundColor = ZkColors.Green.a400
        }
    }

}