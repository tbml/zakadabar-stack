/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import zakadabar.lib.markdown.frontend.MarkdownPage
import zakadabar.lib.markdown.frontend.MarkdownPathPage
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle

const val contentNamespace = "content"

object Welcome : MarkdownPage(
    "/api/$contentNamespace/welcome/Welcome.md",
    SiteMarkdownContext("/Welcome", "welcome/")
)

object WhatsNew : MarkdownPage(
    "/api/$contentNamespace/WhatsNew.md",
    SiteMarkdownContext("/WhatsNew", "/")
)

object ShowCase : MarkdownPage(
    "/api/$contentNamespace/welcome/ShowCase.md",
    SiteMarkdownContext("/ShowCase", "welcome/")
)

object Roadmap : MarkdownPage(
    "/api/$contentNamespace/Roadmap.md",
    SiteMarkdownContext("/Roadmap", "/")
)

object GetStarted : MarkdownPage(
    "/api/$contentNamespace/GetStarted.md",
    SiteMarkdownContext("/GetStarted", "/")
)

object GetHelp : MarkdownPage(
    "/api/$contentNamespace/help/GetHelp.md",
    SiteMarkdownContext("/GetHelp", "help/")
)


object DocumentationIntro : MarkdownPage(
    "/api/$contentNamespace/help/Documentation.md",
    SiteMarkdownContext("/DocumentationIntro", "help/")
)

object FAQ : MarkdownPage(
    "/api/$contentNamespace/help/FAQ.md",
    SiteMarkdownContext("FAQ", "help/")
)

object Documentation : MarkdownPathPage() {

    override fun setAppTitleBar(contextElements: List<ZkElement>) {
        application.title = ZkAppTitle("")
    }

    override fun url(): String {
        return "/api/$contentNamespace/$path"
    }

    override fun context() = SiteMarkdownContext(viewName, path)

}