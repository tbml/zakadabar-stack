/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.data.entity.EntityDtoCompanion
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.util.PublicApi

@PublicApi
@Serializable
data class FolderDto(

    override val id: Long,
    override val entityRecord: EntityRecordDto?

) : EntityDto<FolderDto> {

    override fun getRecordType() = recordType

    override fun comm() = comm

    companion object : EntityDtoCompanion<FolderDto>() {
        override val recordType = "${Stack.shid}/folder"
    }

}