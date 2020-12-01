/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.BackendContext
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.builtin.blob.BlobTable
import zakadabar.stack.backend.util.executor
import zakadabar.stack.data.BlobDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.util.Executor
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.createType

/**
 * Base class for record backends. Supports CRUD, queries and BLOBs.
 */
abstract class RecordBackend<T : RecordDto<T>>(
    val blobTable: BlobTable? = null,
    val recordTable: IdTable<Long>? = null,
) : BackendModule {

    abstract val dtoClass: KClass<T>

    private val logger = LoggerFactory.getLogger("DTO") !!

    open val dtoType
        get() = (dtoClass.companionObject !!.objectInstance as RecordDtoCompanion<*>).recordType

    /**
     * Create a new record.
     *
     * URL: `POST /api/<shid>/<type>`
     *
     * @param executor Executor of the operation.
     * @param dto The record to create.
     *
     * @return DTO of the record created
     */
    open fun create(executor: Executor, dto: T): T {
        throw NotImplementedError()
    }

    /**
     * Read a record.
     *
     * URL: `GET /api/<shid>/<type>/<recordId>`
     *
     * @param executor Executor of the operation.
     * @param recordId The id of the record to read.
     *
     * @return DTO of the record
     */
    open fun read(executor: Executor, recordId: Long): T {
        throw NotImplementedError()
    }

    /**
     * Update an existing record.
     *
     * URL: `PATCH /api/<shid>/<type>`
     *
     * Id comes from the [dto] parameter.
     *
     * @param executor Executor of the operation.
     * @param dto Update.
     *
     * @return DTO of the updated record.
     */
    open fun update(executor: Executor, dto: T): T {
        throw NotImplementedError()
    }

    /**
     * Delete a record.
     *
     * URL: `DELETE /api/<shid>/<type>/<recordId>`
     *
     * @param executor Executor of the operation.
     * @param recordId Id of the record to delete.
     */
    open fun delete(executor: Executor, recordId: Long) {
        throw NotImplementedError()
    }

    /**
     * List all records of this type.
     *
     * URL: `GET /api/<shid>/<type>/all`
     *
     * @param executor Executor of the operation.
     *
     * @return list of DTOs of all records
     */
    open fun all(executor: Executor): List<T> {
        throw NotImplementedError()
    }

    /**
     * Get metadata of blobs that belong to this record.
     *
     * URL : `GET /api/<shid>/<type>/<rid>/blob/<bid>`
     *
     * @param executor Executor of the operation.
     * @param recordId The record to get blob metadata for.
     *
     * @return DTO of the blobs
     */
    open fun blobMeta(executor: Executor, recordId: Long): List<BlobDto> {
        check(blobTable != null) { "missing blob table" }

        return transaction {
            with(blobTable) {
                slice(id, dataRecord, name, type, size)
                    .select { dataRecord eq recordId }
                    .map { toDto(it, dtoType) }
            }
        }
    }

    /**
     * Get content of a blob.
     *
     * URL : `GET /api/<shid>/<type>/<recordId>/blob/<blobId>`
     *
     * @param executor Executor of the operation.
     * @param recordId The record to get blob metadata for.
     * @param blobId The id of the blob to get.
     *
     * @return Content of the blob (binary).
     */
    open fun blobRead(executor: Executor, recordId: Long, blobId: Long): Pair<BlobDto, ByteArray> {
        check(blobTable != null) { "missing blob table" }

        return transaction {
            blobTable
                .select { blobTable.id eq blobId and (blobTable.dataRecord eq recordId) }
                .map { blobTable.toDto(it, dtoType) to it[blobTable.content].bytes }
                .firstOrNull() ?: throw NotFoundException()
        }
    }

    /**
     * Create a new blob.
     *
     * URL : `POST /api/<shid>/<type>/<recordId>/blob`
     *
     * @param executor Executor of the operation.
     * @param dto The DTO of the blob to create.
     * @param bytes Content of the blob.
     *
     * @return Dto of the blob created.
     */
    open fun blobCreate(executor: Executor, dto: BlobDto, bytes: ByteArray): BlobDto {
        check(blobTable != null && recordTable != null) { "missing blob or record table" }

        return transaction {
            val id = blobTable.insert {
                it[dataRecord] = EntityID(dto.dataRecord, recordTable)
                it[name] = dto.name
                it[type] = dto.type
                it[size] = dto.size
                it[content] = ExposedBlob(bytes)
            } get blobTable.id

            dto.copy(id = id.value)
        }
    }

    /**
     * Delete a blob.
     *
     * URL : `DELETE /api/<shid>/<type>/<recordId>/blob/<blobId>
     *
     * @param executor Executor of the operation.
     * @param recordId Id record the blob belongs to.
     * @param blobId Id of the blob to delete.
     *
     * @return Content of the blob (binary).
     */
    open fun blobDelete(executor: Executor, recordId: Long, blobId: Long) {
        check(blobTable != null) { "missing blob table" }

        transaction {
            blobTable.deleteWhere { blobTable.id eq blobId }
        }
    }

    /**
     * Install route handlers.
     *
     * @param  route  Ktor Route context for installing routes
     */
    open fun install(route: Route) = Unit

    /**
     * An initialization function that is called during system startup to
     * initialize this module.
     *
     * When called all modules are loaded and the DB is accessible.
     */
    override fun init() = Unit

    /**
     * A cleanup function that is called during system shutdown to clean up
     * this module. DB is still accessible at this point.
     */
    override fun cleanup() = Unit

    /**
     * Adds CRUD routes for this record backend. Check crud functions for URLs.
     */
    fun Route.crud() {
        route(dtoType) {

            post {
                val executor = call.executor()
                val request = call.receive(dtoClass)
                logger.info("${executor.entityId}: CREATE $dtoType $request")
                call.respond(create(executor, request))
            }

            get("/{rid?}") {
                val id = call.parameters["rid"]?.toLongOrNull() ?: "all"
                val executor = call.executor()

                if (id == "all") {
                    if (BackendContext.logReads) logger.info("${executor.entityId}: ALL $dtoType")
                    call.respond(all(executor))
                } else {
                    if (BackendContext.logReads) logger.info("${executor.entityId}: READ $dtoType $id")
                    call.respond(read(executor, id as Long))
                }
            }

            patch {
                val executor = call.executor()
                val request = call.receive(dtoClass)
                logger.info("${executor.entityId}: UPDATE $dtoType $request")
                call.respond(update(executor, request))
            }

            delete("/{id}") {
                val executor = call.executor()
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException("missing record id")
                logger.info("${executor.entityId}: DELETE $dtoType $id")
                call.respond(delete(executor, id))
            }
        }
    }

    /**
     * Adds BLOB routes for this backend. Check blob functions for URLs.
     */
    fun Route.blob() {
        route("$dtoType/{rid}/blob") {

            get("/{bid?}") {
                val recordId = call.parameters["rid"]?.toLongOrNull() ?: throw BadRequestException("missing record id")
                val blobId = call.parameters["bid"]?.toLongOrNull()

                val executor = call.executor()

                if (blobId == null) {

                    if (BackendContext.logReads) logger.info("${executor.entityId}: BLOB-META $dtoType $recordId $blobId")

                    call.respond(blobMeta(executor, recordId))

                } else {

                    if (BackendContext.logReads) logger.info("${executor.entityId}: BLOB-READ $dtoType $recordId $blobId")

                    val (dto, bytes) = blobRead(executor, recordId, blobId)

                    // TODO add creation-date, modification-date
                    call.response.header(
                        HttpHeaders.ContentDisposition,
                        ContentDisposition.Attachment
                            .withParameter(ContentDisposition.Parameters.FileName, dto.name)
                            .withParameter(ContentDisposition.Parameters.Size, dto.size.toString())
                            .toString()
                    )

                    call.respondBytes(bytes)
                }
            }

            post {
                val recordId = call.parameters["rid"]?.toLongOrNull() ?: throw BadRequestException("missing record id")

                val executor = call.executor()

                if (BackendContext.logReads) logger.info("${executor.entityId}: BLOB-CREATE $dtoType $recordId")

                val headers = call.request.headers

                val length = headers["Content-Length"]?.toIntOrNull() ?: throw BadRequestException("missing content length")
                val disposition = headers["Content-Disposition"] ?: throw BadRequestException("missing content disposition")

                val bytes = ByteArray(length)

                val dto = BlobDto(
                    id = 0,
                    dataRecord = recordId,
                    dataType = dtoType,
                    name = parseHeaderValue(disposition).single().params.find { it.name == "filename" }?.value ?: throw BadRequestException("missing filename"),
                    type = headers["Content-Type"] ?: "application/octet-stream",
                    size = length.toLong()
                )

                call.receiveChannel().readFully(bytes, 0, length)

                call.respond(blobCreate(executor, dto, bytes))
            }

            delete("/{bid}") {
                val id = call.parameters["rid"]?.toLongOrNull() ?: throw BadRequestException("missing record id")
                val blobId = call.parameters["bid"]?.toLongOrNull() ?: throw BadRequestException("missing blob id")

                val executor = call.executor()

                if (BackendContext.logReads) logger.info("${executor.entityId}: BLOB-DELETE $dtoType $id $blobId")

                call.respond(blobDelete(executor, id, blobId))
            }

        }
    }

    /**
     * Adds a Query route for this backend.
     */
    fun <RQ : Any, RS : Any> Route.query(queryDto: KClass<RQ>, func: (Executor, RQ) -> RS) {
        get("$dtoType/${queryDto.simpleName}") {

            val executor = call.executor()

            val qText = call.parameters["q"]
            requireNotNull(qText)
            val qObj = Json.decodeFromString(serializer(queryDto.createType()), qText)

            if (BackendContext.logReads) logger.info("${executor.entityId}: GET $dtoType/${queryDto.simpleName} $qText")

            @Suppress("UNCHECKED_CAST")
            call.respond(func(executor, qObj as RQ))
        }
    }

}