/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.dock

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.util.PublicApi

@PublicApi
open class ZkDockedElementHeader(
    title: String = "",
    val icon: ZkElement? = null,
    @PublicApi val titleElement: ZkElement = ZkElement().also { it.build { + title } },
    private val tools: List<ZkElement> = emptyList()
) : ZkElement() {

    val toolElement = ZkElement()

    override fun onCreate() {
        element.classList.add(ZkDockStyles.header)

        this += icon?.withOptCss(ZkDockStyles.headerIcon)
        this += titleElement css ZkDockStyles.text
        this += toolElement.css(ZkDockStyles.extensions)

        toolElement += tools
    }

}