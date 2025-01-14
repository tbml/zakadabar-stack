/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

/**
 * Base class for private account information. This information is meant to
 * be read only by the account owner and the security officers.
 *
 * @property  id            Id of the account, generated by SQL automatically.
 * @property  accountName   Name of the account, must be unique, use [CheckName]
 *                          to find if a name is already used.
 * @property  fullName      Full name of the account owner.
 * @property  email         Email address. Not necessarily unique.
 * @property  phone         Phone number.
 * @property  theme         Theme this account prefers.
 * @property  locale        The locale this account prefers.
 */
@Serializable
open class AccountPrivateBo(

    override var id: EntityId<AccountPrivateBo>,

    var accountName: String,
    var fullName: String,
    var email: String,
    var phone: String?,

    var theme: String?,
    var locale: String

) : EntityBo<AccountPrivateBo> {

    companion object : EntityBoCompanion<AccountPrivateBo>("zkl-account-private")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id

        + ::accountName min 2 max 50
        + ::fullName min 5 max 100
        + ::email min 4 max 50
        + ::phone min 10 max 20

        + ::locale max 20
        + ::theme max 50
    }
}