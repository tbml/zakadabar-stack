/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.tabcontainer

fun tabContainer(builder: (ZkTabContainer.() -> Unit)? = null) =
    ZkTabContainer(builder)