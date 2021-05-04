/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.port

import zakadabar.demo.marina.data.PortDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object Ports : ZkCrudTarget<PortDto>() {
    init {
        companion = PortDto.Companion
        dtoClass = PortDto::class
        pageClass = Form::class
        tableClass = Table::class
    }
}