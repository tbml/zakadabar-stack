/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import zakadabar.core.util.PublicApi
import zakadabar.core.util.default
import zakadabar.core.util.newInstance
import kotlin.reflect.KClass

object UNINITIALIZED

@PublicApi
inline fun <reified T : EntityBo<T>> String.toEntityId() = EntityId<T>(this)

// TODO check type safety for EntityId.read
@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T> EntityId<T>.read() : T {
    val kClass = T::class as KClass<Any>
    return (kClass.newInstance() as EntityBo<T>).comm().read(this) // TODO replace newInstance with comm registry
}

suspend inline fun <reified T : EntityBo<T>> EntityBoCompanion<T>.create(func: T.() -> Unit) : T {
    return default<T> {
        func()
    }.create()
}