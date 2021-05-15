/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.lib.examples.frontend.query

import zakadabar.lib.examples.data.builtin.ExampleQuery
import zakadabar.lib.examples.resources.strings
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.builtin.toast.dangerToast
import zakadabar.stack.frontend.builtin.toast.successToast
import zakadabar.stack.frontend.resources.theme
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.log
import zakadabar.stack.frontend.util.plusAssign

object QueryPage : ZkPage(cssClass = zkPageStyles.fixed) {

    val form = QueryForm(QueryPage::runQuery)

    val table = ResultTable()

    override fun onConfigure() {
        setAppTitle = false
    }

    override fun onCreate() {
        super.onCreate()

        classList += zkLayoutStyles.grid
        gridTemplateRows = "max-content 1fr"

        form.dto = default()
        form.mode = ZkElementMode.Query

        + form marginBottom (theme.spacingStep)
        + div {
            style {
                position = "relative"
                overflowY = "hidden"
            }
            + table
        }

    }

    private fun runQuery(query: ExampleQuery) {
        io {
            try {
                table.setData(query.execute())
                successToast { strings.querySuccess }
            } catch (ex: Exception) {
                dangerToast { strings.queryFail }
                log(ex)
            }
        }
    }

}