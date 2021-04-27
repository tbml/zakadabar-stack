/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class WebSocketSettingsDto(

    var pingPeriod: Long = 60,
    var timeout: Long = 60,
    var maxFrameSize: Long = 1000000,
    var masking: Boolean = false

) : DtoBase {

    override fun schema() = DtoSchema {
        + ::pingPeriod min 0 default 60
        + ::timeout min 0 default 15
        + ::maxFrameSize min 0 default 1000000
        + ::masking default false
    }

}
