/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import zakadabar.core.schema.BoSchema

interface ZkFieldContext {

    val readOnly : Boolean

    val useShadow : Boolean

    val schema : BoSchema

    val addLabel : Boolean

    val dense : Boolean

    fun validate()

}