/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.speed

import zakadabar.demo.Demo
import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.R
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement.Companion.buildNew
import zakadabar.stack.frontend.elements.ZkElement.Companion.launchBuildNew

object Speeds : ZkCrud<SpeedDto>(Demo.shid, "/speeds") {

    override fun all() = launchBuildNew {
        + SimpleButton(R.new) { Speeds.openCreate() }
        + SpeedTable().setData(SpeedDto.all())
    }

    override fun create() = buildNew {
        + SpeedForm(SpeedDto(0, "", 0.0), FormMode.Create)
    }

    override fun read(recordId: Long) = launchBuildNew {
        + SpeedForm(SpeedDto.read(recordId), FormMode.Read)
    }

    override fun update(recordId: Long) = launchBuildNew {
        + SpeedForm(SpeedDto.read(recordId), FormMode.Update)
    }

    override fun delete(recordId: Long) = launchBuildNew {
        + SpeedForm(SpeedDto.read(recordId), FormMode.Delete)
    }

}

