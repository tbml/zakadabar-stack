/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.sidebar

import zakadabar.core.browser.ZkElement

open class ZkSidebarMinimizedSection(
    val letter : String,
    val section : ZkSideBarGroup
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + zkSideBarStyles.minimizedSection
        + letter

        on("click") { restore() }
    }

    open fun run() {
        section.sideBar?.let { sideBar ->
            val followers = sideBar.minimizedSections.find<ZkSidebarMinimizedSection>().filter {
                this.section.element.compareDocumentPosition(it.section.element).toInt() == 4
            }
            if (followers.isEmpty()) {
                sideBar.minimizedSections += this
            } else {
                sideBar.minimizedSections.insertBefore(this, followers.first())
            }
        }
    }

    protected fun restore() {
        section.restore()
        section.sideBar?.let {
            it.minimizedSections -= this
        }
    }
}