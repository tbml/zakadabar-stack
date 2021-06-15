/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

abstract class BlobBoCompanion<T : BlobBo<T>>(
    val boNamespace: String
) {

    private var _comm: BlobCommInterface<T>? = null

    private fun makeComm(): BlobCommInterface<T> {
        val nc = makeBlobComm(this)
        _comm = nc
        return nc
    }

    var comm: BlobCommInterface<T>
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

    abstract fun serializer(): KSerializer<T>

    suspend fun upload(bo : T, data: Any, callback: (bo : T, state: BlobCreateState, uploaded: Long) -> Unit) =
        comm.upload(bo, data, callback)

    suspend fun listByReference(reference : EntityId<out BaseBo>) =
        comm.listByReference(reference)

}