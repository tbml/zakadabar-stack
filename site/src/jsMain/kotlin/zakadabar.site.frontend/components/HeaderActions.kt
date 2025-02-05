/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteGreenBlueTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.theme.ZkThemeRotate
import zakadabar.core.resource.ZkIcons

class HeaderActions : ZkElement() {

    override fun onCreate() {
        + ZkThemeRotate(
            ZkIcons.darkMode to SiteDarkTheme(),
            ZkIcons.lightMode to SiteLightTheme(),
            ZkIcons.leaf to SiteGreenBlueTheme()
        )
    }

}