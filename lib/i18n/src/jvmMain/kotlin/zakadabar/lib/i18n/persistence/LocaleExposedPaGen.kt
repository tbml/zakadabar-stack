/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.i18n.persistence

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.core.persistence.exposed.entityId
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.lib.i18n.data.LocaleStatus


/**
 * Exposed based Persistence API for LocaleBo.
 *
 * Generated with Bender at 2021-06-07T02:42:38.718Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 *
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class LocaleExposedPaGen : ExposedPaBase<LocaleBo,LocaleExposedTableGen>(
    table = LocaleExposedTableGen
) {
    override fun ResultRow.toBo() = LocaleBo(
        id = this[table.id].entityId(),
        name = this[table.name],
        description = this[table.description],
        status = this[table.status]
    )

    override fun UpdateBuilder<*>.fromBo(bo: LocaleBo) {
        this[table.name] = bo.name
        this[table.description] = bo.description
        this[table.status] = bo.status
    }
}

/**
 * Exposed based SQL table for LocaleBo.
 *
 * Generated with Bender at 2021-06-07T02:42:38.718Z.
 *
 * **IMPORTANT** Please do not modify this class manually.
 *
 * If you need other fields, add them to the business object and then re-generate.
 */
object LocaleExposedTableGen : ExposedPaTable<LocaleBo>(
    tableName = "locale"
) {

    internal val name = varchar("name", 100)
    internal val description = text("description")
    internal val status = enumerationByName("status", 20, LocaleStatus::class)

}