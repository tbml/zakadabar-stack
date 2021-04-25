/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.settings

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.data.builtin.resources.SettingDto
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<SettingDto>() {

    private val roles by preload { RoleDto.allAsMap() }

    override fun onConfigure() {
        super.onConfigure()

        add = true
        search = true
        export = true

        titleText = strings.roles
        crud = Roles

        + SettingDto::id
        + custom {
            label = strings.role
            render = { + roles[it.role]?.name }
        }
        + SettingDto::name
        + SettingDto::value

        + actions()
    }

}