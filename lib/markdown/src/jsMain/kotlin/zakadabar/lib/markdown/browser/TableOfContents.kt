/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.browser

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import org.w3c.dom.set
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.util.getElementId
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.browser.util.w3c.IntersectionObserver
import zakadabar.core.browser.util.w3c.IntersectionObserverEntry
import zakadabar.core.resource.theme
import zakadabar.lib.markdown.browser.flavour.ZkMarkdownContext

class TableOfContents(
    val context: ZkMarkdownContext,
    val content: HTMLElement
) : ZkElement() {

    private lateinit var observer: IntersectionObserver

    private lateinit var tocContent: HTMLElement

    override fun onCreate() {
        super.onCreate()

        classList += markdownStyles.tocContainer

        // the query selector does not work if we use it synchronously
        window.requestAnimationFrame {

            element.parentElement?.parentElement?.parentElement?.getBoundingClientRect()?.let {
                tocContent.style.maxHeight = "${it.height - theme.spacingStep}px"
            }

            initObserver()
        }

        tocContent = div(markdownStyles.tocContent) {
            context.tableOfContents.forEach {
                + div(markdownStyles.tocEntry) {
                    buildPoint.id = it.tocId

                    + div(markdownStyles.tocText) {
                        buildPoint.style.paddingLeft = "${it.level * theme.spacingStep}px"
                        + it.text
                    }

                    on("click") { event ->
                        val (_, id) = getElementId(event, "zk-", false) ?: return@on
                        content.querySelector("[data-toc-id=\"${id}\"]")?.scrollIntoView()
                    }
                }
            }
        }

        + tocContent
    }

    private fun initObserver() {
        val options: dynamic = object {}
        options["root"] = content
        options["rootMargin"] = "0px"
        options["threshold"] = 0

        observer = IntersectionObserver(observerCallback, options)

        val headers = content.querySelectorAll("[data-toc-id]")
        for (i in 0 until headers.length) {
            observer.observe(headers[i] as HTMLElement)
        }
    }

    override fun onDestroy() {
        observer.disconnect()
    }

    private val observerCallback = fun(entries: Array<IntersectionObserverEntry>, _: IntersectionObserver) {

        var hasIntersecting = false

        entries.forEach {
            if (it.isIntersecting) hasIntersecting = true
        }

        if (! hasIntersecting) return

        clearActive()

        for (it in entries) {
            val target = it.target as HTMLElement
            val tocId = target.dataset["tocId"] ?: continue
            val tocElement = document.getElementById(tocId) as? HTMLElement ?: continue

            if (it.isIntersecting) {
                tocElement.dataset["active"] = "true"
                tocElement.scrollIntoView(
                    object {
                        val block = "nearest"
                        val inline = "nearest"
                    }.asDynamic()
                )

                val id = target.id
                if (id.isNotEmpty()) {
                    application.replaceNavState(hash = id)
                }
                break
            }
        }
    }

    private fun clearActive() {
        val active = element.querySelectorAll("[data-active=\"true\"]")
        for (i in 0 until active.length) {
            (active[i] as HTMLElement).dataset["active"] = "false"
        }
    }
}