/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.resources.MaterialColors

data class ZkSideBarTheme(
    val background: String = MaterialColors.black,
    val text: String = MaterialColors.white,
    val hoverBackground: String = "rgba(255,255,255,0.2)",
    val hoverText: String = text,
    val activeBackground: String = background,
    val activeText: String = text
)