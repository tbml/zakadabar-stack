/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.audit

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.slf4j.Logger
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor
import kotlin.reflect.full.createType

/**
 * A simple auditor that writes the audit into the logger (on INFO channel)
 * passed in the constructor.
 *
 * @param  logger       The logger to write into.
 * @param  includeData  When true, content of the BOs is also written for create and update.
 */
class LogAuditor<T : EntityBo<T>>(
    private val logger : Logger,
    private val includeData : Boolean = true
) : Auditor<T> {

    override fun auditList(executor: Executor) {
        logger.info("${executor.accountId}: LIST")
    }

    override fun auditRead(executor: Executor, entityId: EntityId<T>) {
        logger.info("${executor.accountId}: READ $entityId")
    }

    override fun auditCreate(executor: Executor, entity: T) {
        val text = if (includeData) Json.encodeToString(serializer(entity::class.createType()), entity) else ""
        logger.info("${executor.accountId}: CREATE ${entity.id} // $text")
    }

    override fun auditUpdate(executor: Executor, entity: T) {
        val text = if (includeData) Json.encodeToString(serializer(entity::class.createType()), entity) else ""
        logger.info("${executor.accountId}: UPDATE ${entity.id} // $text")
    }

    override fun auditDelete(executor: Executor, entityId: EntityId<T>) {
        logger.info("${executor.accountId}: DELETE $entityId")
    }

    override fun <RQ : ActionBo<RS>, RS : BaseBo> auditAction(executor: Executor, bo: RQ) {
        val text = if (includeData) Json.encodeToString(serializer(bo::class.createType()), bo) else ""
        logger.info("${executor.accountId}: ACTION ${bo::class.simpleName} // $text")
    }


}