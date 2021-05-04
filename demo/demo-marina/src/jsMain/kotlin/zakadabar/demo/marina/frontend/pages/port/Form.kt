/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.port

import zakadabar.demo.marina.data.PortDto
import zakadabar.demo.marina.data.SeaDto
import zakadabar.demo.marina.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm

class Form : ZkForm<PortDto>() {

    override fun onCreate() {
        build(dto.name, Strings.port) {

            + section(Strings.basics) {
                + dto::id
                + dto::name
                + select(dto::sea) { SeaDto.all().by { it.name } }
            }

        }
    }
}