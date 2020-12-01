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

package zakadabar.stack.frontend.builtin.desktop.image

import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.builtin.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.util.PublicApi

@PublicApi
class ImageView(private val dto: EntityRecordDto) : ZkElement() {


    override fun init(): ImageView {

        val url = EntityRecordDto.revisionUrl(dto.id)

        element.innerHTML = """<img src="$url" class="${coreClasses.whMax100p}" >"""

        return this
    }

}