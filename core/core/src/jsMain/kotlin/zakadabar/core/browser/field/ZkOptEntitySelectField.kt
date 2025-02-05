/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.field

import zakadabar.core.browser.field.select.DropdownRenderer
import zakadabar.core.browser.field.select.SelectRenderer
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import kotlin.reflect.KMutableProperty0

open class ZkOptEntitySelectField<ST : EntityBo<ST>>(
    context : ZkFieldContext,
    val prop: KMutableProperty0<EntityId<ST>?>,
    renderer : SelectRenderer<EntityId<ST>?,ZkOptEntitySelectField<ST>> = DropdownRenderer()
) : ZkSelectBase<EntityId<ST>?,ZkOptEntitySelectField<ST>>(context, prop.name, renderer) {

    override fun fromString(string: String): EntityId<ST> {
        return EntityId(string)
    }

    override fun getPropValue() = prop.get()

    override fun setPropValue(value: Pair<EntityId<ST>?, String>?, user : Boolean) {
        prop.set(value?.first)
        if (user) onUserChange(value?.first)
    }

}