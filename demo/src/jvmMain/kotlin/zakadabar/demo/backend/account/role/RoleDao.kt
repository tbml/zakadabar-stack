/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account.role

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.data.account.RoleDto

class RoleDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RoleDao>(RoleTable)

    var name by RoleTable.name
    var description by RoleTable.description

    fun toDto() = RoleDto(
        id = id.value,
        name = name,
        description = description
    )
}