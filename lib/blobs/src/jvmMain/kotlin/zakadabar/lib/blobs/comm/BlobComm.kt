/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.comm

import io.ktor.client.request.*
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import zakadabar.core.comm.CommBase.Companion.baseUrl
import zakadabar.core.comm.CommBase.Companion.client
import zakadabar.core.comm.CommBase.Companion.onError
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.util.PublicApi
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCreateState

/**
 * REST communication functions for entities.
 *
 * @property  namespace    Namespace of the entity this comm handles.
 *
 * @property  serializer   The serializer to serialize/deserialize objects
 *                         sent/received.
 */
@PublicApi
open class BlobComm<T : BlobBo<T, RT>, RT : EntityBo<RT>>(
    private val namespace: String,
    private val serializer: KSerializer<T>
) : BlobCommInterface<T,RT> {

    @PublicApi
    override suspend fun create(bo: T): T {
        require(bo.id.isEmpty()) { "id is empty in $bo" }

        val text = try {
            client.post<String>("$baseUrl/api/$namespace/blob/meta") {
                header("Content-Type", "application/json")
                body = Json.encodeToString(serializer, bo)
            }
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun read(id: EntityId<T>): T {
        val text = try {
            client.get<String>("$baseUrl/api/$namespace/blob/meta/$id")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun update(bo: T): T {
        require(! bo.id.isEmpty()) { "ID of the $bo is 0 " }

        val text = try {
            client.patch<String>("$baseUrl/api/$namespace/blob/meta/${bo.id}") {
                header("Content-Type", "application/json")
                body = Json.encodeToString(serializer, bo)
            }
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(serializer, text)
    }

    @PublicApi
    override suspend fun all(): List<T> {
        val text = try {
            client.get<String>("$baseUrl/api/$namespace/blob/meta")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

    @PublicApi
    override suspend fun delete(id: EntityId<T>) {
        try {
            client.delete<Unit>("$baseUrl/api/$namespace/blob/meta/$id")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

    @PublicApi
    override suspend fun upload(bo : T, data: Any) : T {
        val channel = Channel<Boolean>()

        upload(bo, data) { _, state, _ ->
            GlobalScope.launch(Dispatchers.Default) {
                when (state) {
                    BlobCreateState.Error -> channel.send(false)
                    BlobCreateState.Done -> channel.send(true)
                    else -> Unit
                }
            }
        }

        if (!channel.receive()) throw RuntimeException("blob upload error")

        return bo
    }

    @PublicApi
    override suspend fun upload(bo : T, data: Any, callback: (bo: T, state: BlobCreateState, uploaded: Long) -> Unit) : T {
        require(data is ByteArray)

        callback(bo, BlobCreateState.Starting, 0)

        GlobalScope.launch(Dispatchers.Default) {

            try {
                client.post<String>("$baseUrl/api/$namespace/blob/content/${bo.id}") {
                    body = ByteArrayContent(data)
                }

                callback(bo, BlobCreateState.Done, data.size.toLong())

            } catch (ex: Exception) {
                callback(bo, BlobCreateState.Error, 0L)
                onError(ex)
                throw ex
            }
        }

        bo.size = data.size.toLong()

        return bo
    }

    @PublicApi
    override suspend fun download(id: EntityId<T>): ByteArray {
        return try {
            client.get("$baseUrl/api/$namespace/blob/content/$id")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }
    }

    @PublicApi
    override suspend fun byReference(reference: EntityId<RT>?, disposition : String?): List<T> {
        val q = disposition?.let { "?disposition=$it" } ?: ""
        val text = try {
            client.get<String>("$baseUrl/api/$namespace/blob/list/${if (reference == null) "" else "/$reference"}$q")
        } catch (ex: Exception) {
            onError(ex)
            throw ex
        }

        return Json.decodeFromString(ListSerializer(serializer), text)
    }

}