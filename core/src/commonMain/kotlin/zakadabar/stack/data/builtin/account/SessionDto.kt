/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

@Serializable
data class SessionDto(

    override var id: Long, // always 1
    val account: AccountPublicDto,
    val anonymous: Boolean,
    val roles: List<String>

) : RecordDto<SessionDto> {

    companion object : RecordDtoCompanion<SessionDto>({
        namespace = "session"
    })

    override fun getRecordType() = namespace
    override fun comm() = comm

}