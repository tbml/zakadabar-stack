/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.ActionBusinessLogicWrapper
import zakadabar.stack.backend.business.BusinessLogicCommon
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.module.modules
import kotlin.reflect.full.createType

open class WorkerBl : BusinessLogicCommon<BaseBo>(), RoutedModule {

    override val namespace = "zkl-schedule-worker"

    override val authorizer by provider()

    override val router = router {
        action(PushJob::class, ::pushJob)
        action(RequestJobCancel::class, ::requestJobCancel)
    }

    override fun onInstallRoutes(route: Any) {
        router.installRoutes(route)
    }

    open fun pushJob(executor: Executor, action: PushJob): ActionStatusBo {
        val module = modules.firstOrNull<BusinessLogicCommon<*>> { it.namespace == action.actionNamespace }
            ?: throw NotImplementedError("no module found for namespace '${action.actionNamespace}'")

        val (actionFunc, actionData) = module.router.prepareAction(action.actionType, action.actionData)

        module as ActionBusinessLogicWrapper

        runBlocking {
            launch {
                ActionExecution(action.jobId, executor, module, actionFunc, actionData).execute()
            }
        }

        return ActionStatusBo()
    }

    open fun requestJobCancel(executor: Executor, requestJobCancel: RequestJobCancel): ActionStatusBo {
        TODO("Not yet implemented")
    }

    class ActionExecution(
        val jobId: EntityId<Job>,
        val executor: Executor,
        val module: ActionBusinessLogicWrapper,
        val actionFunc: (Executor, BaseBo) -> Any?,
        val actionData: BaseBo
    ) {
        suspend fun execute() {

            try {
                val response = module.actionWrapper(executor, actionFunc, actionData)

                val responseData = response?.let {
                    Json.encodeToString(serializer(it::class.createType()), actionData)
                }

                JobSuccess(jobId, responseData).execute()

            } catch (ex : JobFailException) {

                JobFail(
                    jobId,
                    lastFailMessage = ex.message,
                    lastFailData = ex.failData,
                    retryAt = ex.retryAt
                ).execute()

            } catch (ex: Exception) {

                ex.printStackTrace()

                JobFail(
                    jobId,
                    lastFailMessage = ex.stackTraceToString(),
                    lastFailData = null,
                    retryAt = null
                ).execute()

            }
        }
    }


}