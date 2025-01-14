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
package zakadabar.core.schema.entries

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import zakadabar.core.schema.BoPropertyConstraintImpl
import zakadabar.core.schema.BoSchemaEntry
import zakadabar.core.schema.BoSchemaEntryExtension
import zakadabar.core.schema.ValidityReport
import zakadabar.core.schema.descriptor.BoConstraintType
import zakadabar.core.schema.descriptor.BoProperty
import zakadabar.core.schema.descriptor.InstantBoConstraint
import zakadabar.core.schema.descriptor.InstantBoProperty
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

class InstantBoSchemaEntry(
    override val kProperty: KMutableProperty0<Instant>
    ) : BoSchemaEntry<Instant, InstantBoSchemaEntry> {

    override val rules = mutableListOf<BoPropertyConstraintImpl<Instant>>()

    override val extensions = mutableListOf<BoSchemaEntryExtension<Instant>>()

    override var defaultValue: Instant = Clock.System.now()

    inner class Max(@PublicApi val limit: Instant) : BoPropertyConstraintImpl<Instant> {

        override fun validate(value: Instant, report: ValidityReport) {
            if (value > limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Instant) : BoPropertyConstraintImpl<Instant> {

        override fun validate(value: Instant, report: ValidityReport) {
            if (value < limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.Min, limit)

    }

    inner class Before(@PublicApi val limit: Instant) : BoPropertyConstraintImpl<Instant> {

        override fun validate(value: Instant, report: ValidityReport) {
            if (value >= limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.Before, limit)

    }

    inner class After(@PublicApi val limit: Instant) : BoPropertyConstraintImpl<Instant> {

        override fun validate(value: Instant, report: ValidityReport) {
            if (value <= limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.After, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: Instant) : BoPropertyConstraintImpl<Instant> {

        override fun validate(value: Instant, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.NotEquals, invalidValue)

    }

    @PublicApi
    infix fun max(limit: Instant): InstantBoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Instant): InstantBoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun before(limit: Instant): InstantBoSchemaEntry {
        rules += Before(limit)
        return this
    }

    @PublicApi
    infix fun after(limit: Instant): InstantBoSchemaEntry {
        rules += After(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: Instant): InstantBoSchemaEntry {
        rules += NotEquals(invalidValue)
        return this
    }

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }

    @PublicApi
    infix fun default(value: Instant): InstantBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue ?: Clock.System.now())
    }

    override fun decodeFromText(text : String?) : Instant {
        return Instant.parse(text!!)
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is InstantBoProperty)
        kProperty.set(bo.value!!)
    }

    override fun toBoProperty() = InstantBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        defaultValue,
        kProperty.get()
    )

    override fun constraints() = rules.map { it.toBoConstraint() }

}