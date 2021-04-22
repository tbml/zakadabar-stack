/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.demo.data.ship

import kotlinx.serialization.*
import zakadabar.stack.data.action.ActionDto
import zakadabar.stack.data.action.ActionDtoCompanion
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.util.PublicApi

@Serializable
@PublicApi
class ShootAtShipAction : ActionDto<ActionStatusDto> {

    // Do not forget to add query and action classes to ShipDto!

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusDto.serializer())

    companion object : ActionDtoCompanion<ActionStatusDto>(ShipDto.namespace)

}