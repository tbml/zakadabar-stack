/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import kotlinx.serialization.KSerializer
import zakadabar.stack.exceptions.DataConflict

abstract class EntityBoCompanion<T : EntityBo<T>>(
    val boNamespace: String,
    val builder : EntityBoCompanion<T>.() -> Unit
) {

    val comm : EntityCommInterface<T> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { makeComm() }

    /**
     * Creates the comm instance.
     */
    open fun makeComm() = makeEntityComm(this)

    abstract fun serializer(): KSerializer<T>

    /**
     * Fetches an entity.
     *
     * @param  id  Id of the entity.
     *
     * @return  BO of the fetched entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun read(id: Long) = comm.read(id)

    /**
     * Fetches an entity.
     *
     * @param  id  Id of the entity.
     *
     * @return  BO of the fetched entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun read(id: String) = comm.read(id)

    /**
     * Fetches an entity.
     *
     * @param  id  Id of the entity.
     *
     * @return  BO of the fetched entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun read(id: EntityId<T>) = comm.read(id)

    /**
     * Deletes an entity on the server.
     *
     * @param  id  Id of the entity to delete.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun delete(id: EntityId<T>) = comm.delete(id)

    /**
     * Retrieves BOs of all entities of the given type.
     *
     * @return  List of entity BOs.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun all() = comm.all()

    /**
     * Retrieves BOs of all entities of the given type and puts them into a map.
     *
     * @return  Map of entity BOs with `id` as key and the BO as value.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun allAsMap() = comm.all().associateBy { it.id }
}