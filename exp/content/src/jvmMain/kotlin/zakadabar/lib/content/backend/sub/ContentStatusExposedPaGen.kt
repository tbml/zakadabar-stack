/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend.sub

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.content.data.sub.ContentStatusBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId


/**
 * Exposed based Persistence API for ContentStatusBo.
 * 
 * Generated with Bender at 2021-06-05T02:07:27.692Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class ContentStatusExposedPaGen : ExposedPaBase<ContentStatusBo,ContentStatusExposedTableGen>(
    table = ContentStatusExposedTableGen
) {
    override fun ResultRow.toBo() = ContentStatusBo(
        id = this[table.id].entityId(),
        name = this[table.name]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: ContentStatusBo) {
        this[table.name] = bo.name
    }
}

/**
 * Exposed based SQL table for ContentStatusBo.
 * 
 * Generated with Bender at 2021-06-05T02:07:27.693Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object ContentStatusExposedTableGen : ExposedPaTable<ContentStatusBo>(
    tableName = "content_status"
) {

    internal val name = varchar("name", 100)

}