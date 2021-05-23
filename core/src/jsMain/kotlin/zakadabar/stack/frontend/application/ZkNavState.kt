/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import org.w3c.dom.url.URLSearchParams
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

/**
 * Stores the current navigation state of the browser window.
 *
 * @property  locale        The locale of the site, first segment of the URL.
 * @property  viewName      The name of the view in the URL, selects the Element to use.
 * @property  segments      The segments of the URL path (parts between '/' characters).
 * @property  hash       The hashtag from the URL path.
 * @property  recordId      Id of the record when specified in the URL.
 * @property  query         The query object when specified in the URL.
 * @property  args          The args object when specified in the URL.
 */
class ZkNavState(val urlPath: String, val urlQuery: String, val urlHashtag: String = "") {

    private val locale: String
    val viewName: String
    val segments: List<String>
    val hash: String
    val recordId: EntityId<*>
    val query: Any?
    val args: String?

    init {
        segments = urlPath.trim('/').split("/")

        // use application home when there are no segments

        if (segments.size < 2) {

            locale = ""
            viewName = ""
            hash = ""
            recordId = EntityId<BaseBo>()
            query = null
            args = null

        } else {

            val searchParams = URLSearchParams(urlQuery.trim('?'))

            locale = segments[0]
            viewName = segments[1]

            hash = if (segments.last().contains('#')) segments.last().substringAfter('#') else urlHashtag.trim('#')

            recordId = EntityId<BaseBo>(searchParams.get("id"))

            query = searchParams.get("query")

            args = searchParams.get("args")
        }
    }

    override fun toString(): String {
        return "urlPath=$urlPath, urlQuery=$urlQuery, viewName=$viewName, hash=$hash, recordId=$recordId, query=$query"
    }
}
