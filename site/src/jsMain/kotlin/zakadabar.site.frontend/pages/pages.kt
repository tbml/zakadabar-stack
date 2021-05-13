/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import zakadabar.lib.markdown.frontend.MarkdownPage
import zakadabar.lib.markdown.frontend.MarkdownPathPage
import zakadabar.site.data.ContentQuery
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle

object Welcome : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/welcome/Welcome.md",
    ContentContext("/Welcome", "welcome/")
)

object WhatsNew : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/WhatsNew.md",
    ContentContext("/WhatsNew", "/")
)

object ShowCase : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/welcome/ShowCase.md",
    ContentContext("/ShowCase", "welcome/")
)

object Roadmap : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/Roadmap.md",
    ContentContext("/Roadmap", "/")
)

object GetStarted : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/GetStarted.md",
    ContentContext("/GetStarted", "/")
)

object GetHelp : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/help/GetHelp.md",
    ContentContext("/GetHelp", "help/")
)


object DocumentationIntro : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/help/Documentation.md",
    ContentContext("/DocumentationIntro", "help/")
)

object FAQ : MarkdownPage(
    "/${ContentQuery.dtoNamespace}/help/FAQ.md",
    ContentContext("FAQ", "help/")
)

object Documentation : MarkdownPathPage() {

    fun open(path: String) {
        application.changeNavState(this, path)
    }

    override fun setAppTitle() {
        application.title = ZkAppTitle("")
    }

    override fun url(): String {
        return "/content/$path"
    }

    override fun context() = ContentContext(viewName, path)

}