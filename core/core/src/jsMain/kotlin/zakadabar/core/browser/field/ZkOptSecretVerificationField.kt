/*
 * Copyright © 2020, Simplexion, Hungary and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.core.browser.field

import zakadabar.core.data.Secret
import zakadabar.core.resource.localizedStrings
import kotlin.reflect.KMutableProperty0

open class ZkOptSecretVerificationField(
    context : ZkFieldContext,
    prop: KMutableProperty0<Secret?>,
    label: String = localizedStrings.getNormalized(prop.name + "Verification")
) : ZkStringBase<Secret?,ZkOptSecretVerificationField>(
    context = context,
    prop = prop,
    label = label
) {

    override var valueOrNull : Secret?
        get() = verificationValue?.let { Secret(it) }
        set(value) {
            verificationValue = value?.value
            input.value = value?.toString() ?: ""
        }

    var verificationValue: String? = ""

    override fun getPropValue() = verificationValue ?: ""

    override fun setPropValue(value: String) {
        verificationValue = input.value.ifBlank { null }
        valid = (prop.get()?.value == verificationValue)
        onUserChange(verificationValue?.let { Secret(it) })
    }

    override fun buildFieldValue() {
        input.type = "password"
        input.autocomplete = "new-password"
        super.buildFieldValue()
    }

}