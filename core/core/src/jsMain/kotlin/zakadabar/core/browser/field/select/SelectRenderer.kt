/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.field.select

import zakadabar.core.browser.field.ZkSelectBase

interface SelectRenderer<VT, FT : ZkSelectBase<VT, FT>> {

    var field: ZkSelectBase<VT, FT>

    val context get() = field.context

    val items get() = field.items

    fun readOnly(value: Boolean)

    fun onCreate() {  }

    fun onPause() {  }

    fun buildFieldValue()

    fun render(value: VT?)

    fun focusValue()

}