/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.business

import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.lib.i18n.persistence.LocaleExposedPa

class LocaleBl : EntityBusinessLogicBase<LocaleBo>(
    boClass = LocaleBo::class
) {

    override val pa = LocaleExposedPa()

    override val authorizer by provider()

    fun byName(name : String) = pa.byName(name)

}