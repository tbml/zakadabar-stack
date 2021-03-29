/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import zakadabar.stack.frontend.util.encodeURIComponent
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.json

/**
 * Communication functions for records.
 *
 * @property  companion   Companion of the query DTO this comm belongs to.
 */
@PublicApi
open class QueryComm(
    private val companion: QueryDtoCompanion<*>
) : QueryCommInterface {

    @PublicApi
    override suspend fun <RQ : Any, RS> query(request: RQ, requestSerializer: KSerializer<RQ>, responseSerializer: KSerializer<List<RS>>): List<RS> {

        val q = encodeURIComponent(Json.encodeToString(requestSerializer, request))

        val responsePromise = window.fetch("/api/${companion.namespace}/${request::class.simpleName}?q=${q}")
        val response = responsePromise.await()

        if (! response.ok) throw RuntimeException()

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(responseSerializer, text)
    }

}