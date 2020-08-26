/*
 * Copyright © 2020, Simplexion, Hungary
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
package zakadabar.stack.frontend.builtin.navigator

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.navigator.NavigatorClasses.Companion.navigatorClasses
import zakadabar.stack.frontend.builtin.simple.SimpleText
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.extend.FrontendEntitySupport

class NewEntityItem(
    private val newEntity: NewEntity,
    internal val support: FrontendEntitySupport<*>
) : ComplexElement() {

    override fun init(): ComplexElement {
        super.init()

        className = navigatorClasses.newEntityItem

        this += (support.iconSource?.simple16 ?: Icons.description.simple16).withClass(navigatorClasses.newEntityIcon)
        this += SimpleText(support.displayName).withClass(navigatorClasses.newEntityName)

        on("click", ::onClick)

        return this
    }

    private fun onClick(@Suppress("UNUSED_PARAMETER") event: Event) {
        newEntity.selected = support
        newEntity.next()
    }
}
