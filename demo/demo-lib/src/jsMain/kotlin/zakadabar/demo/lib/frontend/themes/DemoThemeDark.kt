/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.lib.frontend.themes

import zakadabar.stack.frontend.builtin.layout.ZkLayoutTheme
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinLightTheme
import zakadabar.stack.frontend.resources.ZkColors

class DemoThemeDark : ZkBuiltinLightTheme() {

    override var layout = ZkLayoutTheme(defaultBackground = ZkColors.LightBlue.c900)

}