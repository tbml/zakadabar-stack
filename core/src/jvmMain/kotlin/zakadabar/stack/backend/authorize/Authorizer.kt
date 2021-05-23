/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor

/**
 * Implemented by authorizer classes. These are used by business logic
 * modules to authorize access.
 */
interface Authorizer<T : EntityBo<T>> {

    fun onModuleStart() {

    }

    fun authorizeList(executor: Executor) {
        throw Forbidden()
    }

    fun authorizeRead(executor: Executor, entityId: EntityId<T>) {
        throw Forbidden()
    }

    fun authorizeCreate(executor: Executor, entity: T) {
        throw Forbidden()
    }

    fun authorizeUpdate(executor: Executor, entity: T) {
        throw Forbidden()
    }

    fun authorizeDelete(executor: Executor, entityId: EntityId<T>) {
        throw Forbidden()
    }

}