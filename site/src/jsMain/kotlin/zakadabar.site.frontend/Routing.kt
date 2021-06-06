/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.site.frontend

import zakadabar.lib.examples.frontend.form.SyntheticForm
import zakadabar.lib.examples.frontend.layout.TabContainer
import zakadabar.lib.examples.frontend.pages.ArgPage
import zakadabar.lib.examples.frontend.query.QueryPage
import zakadabar.lib.examples.frontend.sidebar.ExampleMarkdownSideBarTarget
import zakadabar.lib.examples.frontend.sidebar.ExampleSideBarTarget
import zakadabar.lib.examples.frontend.table.FetchedTable
import zakadabar.lib.examples.frontend.table.GeneratedTable
import zakadabar.site.frontend.pages.*
import zakadabar.stack.frontend.application.ZkAppRouting

const val contentNamespace = "content"

class Routing : ZkAppRouting(DefaultLayout, Landing) {

    init {
        + Landing
        + WhatsNew
        + Documentation
        + ChangeLog
        + GetStarted
        + GetHelp
        + Welcome
        + ShowCase
        + Roadmap
        + DocumentationIntro
        + FAQ
        + LegalNotices
        + Credits
        + ServicesAndSupport
        + Experimental
        + ProjectStatus
        + Versioning
        + UpcomingChanges

        + ExampleSideBarTarget
        + ExampleMarkdownSideBarTarget

        + ArgPage
        + TabContainer
        + GeneratedTable
        + FetchedTable
        + QueryPage
        + SyntheticForm

        + BenderPage
    }

}