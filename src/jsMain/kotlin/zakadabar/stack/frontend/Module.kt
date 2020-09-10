/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend

import zakadabar.stack.Stack
import zakadabar.stack.data.FolderDto
import zakadabar.stack.data.SystemDto
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.data.security.CommonAccountDto
import zakadabar.stack.frontend.builtin.desktop.Desktop
import zakadabar.stack.frontend.builtin.desktop.folder.NewFolder
import zakadabar.stack.frontend.builtin.desktop.navigator.EntityNavigator
import zakadabar.stack.frontend.builtin.i18n
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.comm.rest.EntityCache
import zakadabar.stack.frontend.extend.FrontendEntitySupport
import zakadabar.stack.frontend.extend.FrontendModule
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : FrontendModule() {

    override val uuid = Stack.uuid

    override fun init() {

        FrontendContext += uuid to i18n

        FrontendContext += Desktop
        FrontendContext += EntityNavigator

        EntityRecordDto.comm = EntityCache

        FrontendContext += FrontendEntitySupport(
            Stack.uuid,
            FolderDto.Companion,
            iconSource = Icons.folder,
            newView = NewFolder
        )

        FrontendContext += FrontendEntitySupport(
            Stack.uuid,
            SystemDto.Companion,
            comm = null,
            iconSource = Icons.settings
        )

        FrontendContext += FrontendEntitySupport(
            Stack.uuid,
            CommonAccountDto.Companion,
            comm = null,
            iconSource = Icons.account_box
        )

    }

}
