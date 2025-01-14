/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.persistence

import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.AccountListSecureEntry
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.core.persistence.exposed.entityId
import zakadabar.core.authorize.AccountPublicBo

open class AccountPrivateExposedPa(
    table: AccountPrivateExposedTable = AccountPrivateExposedTableCommon
) : ExposedPaBase<AccountPrivateBo, AccountPrivateExposedTable>(
    table = table
) {
    open fun readByName(name: String) = table.select { table.accountName eq name }.first().toBo()

    open fun readByNameOrNull(name: String) = table.select { table.accountName eq name }.firstOrNull()?.toBo()

    open fun accountListSecure(states: AccountStateExposedTable): List<AccountListSecureEntry> =
        table
            .join(states, JoinType.INNER) { table.id eq states.entityId }
            .slice(
                table.id,
                table.accountName,
                table.fullName,
                table.email,
                table.phone,
                table.locale,
                states.validated,
                states.locked,
                states.expired,
                states.anonymized
            )
            .selectAll()
            .map {
                AccountListSecureEntry(
                    accountId = it[table.id].entityId(),
                    accountName = it[table.accountName],
                    fullName = it[table.fullName],
                    email = it[table.email],
                    phone = it[table.phone],
                    locale = it[table.locale],
                    validated = it[states.validated],
                    locked = it[states.locked],
                    expired = it[states.expired],
                    anonymized = it[states.anonymized]
                )
            }

    open fun accountList(withEmail: Boolean, withPhone: Boolean): List<AccountPublicBo> =
        table
            .slice(
                table.id,
                table.accountName,
                table.fullName,
                table.email,
                table.phone,
                table.theme,
                table.locale
            )
            .selectAll()
            .map {
                AccountPublicBo(
                    accountId = it[table.id].entityId(),
                    accountName = it[table.accountName],
                    fullName = it[table.fullName],
                    email = if (withEmail) it[table.email] else null,
                    phone = if (withPhone) it[table.phone] else null,
                    theme = it[table.theme],
                    locale = it[table.locale],
                )
            }

    override fun ResultRow.toBo() = AccountPrivateBo(
        id = this[table.id].entityId(),
        accountName = this[table.accountName],
        fullName = this[table.fullName],
        email = this[table.email],
        phone = this[table.phone],
        theme = this[table.theme],
        locale = this[table.locale]
    )

    override fun UpdateBuilder<*>.fromBo(bo: AccountPrivateBo) {
        this[table.accountName] = bo.accountName
        this[table.fullName] = bo.fullName
        this[table.email] = bo.email
        this[table.phone] = bo.phone
        this[table.theme] = bo.theme
        this[table.locale] = bo.locale
    }
}

object AccountPrivateExposedTableCommon : AccountPrivateExposedTable()

/**
 * Stores AccountPrivate entities this table is supposed to store data
 * that is sensitive and should be handled with care.
 *
 * @property  accountName   Name of the account. Must be unique system-wide.
 * @property  fullName      Full name of the person who owns this account.
 * @property  email         Email address. Not unique.
 * @property  phone         Phone number.
 * @property  theme         Theme this account prefers.
 * @property  locale        The locale this account prefers.
 */
open class AccountPrivateExposedTable : ExposedPaTable<AccountPrivateBo>(
    tableName = "account_private"
) {

    val accountName = varchar("account_name", 50)
    val fullName = varchar("full_name", 100)
    val email = varchar("email", 50)
    val phone = varchar("phone", 20).nullable()
    val theme = varchar("theme", 50).nullable()
    val locale = varchar("locale", 20)

}
