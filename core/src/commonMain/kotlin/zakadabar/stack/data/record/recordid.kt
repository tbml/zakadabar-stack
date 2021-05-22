/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_TYPEALIAS_PARAMETER") // record id should be record type bound

package zakadabar.stack.data.record

import kotlinx.serialization.Serializable

@Serializable
class RecordId<T> : Comparable<RecordId<T>> {

    val value : String

    constructor() {
        value = ""
    }

    constructor(value : String) {
        this.value = value
    }

    constructor(value : Long) {
        this.value = value.toString()
    }

    fun toLong() = value.toLong()

    fun isEmpty() = value.isEmpty()

    override fun compareTo(other: RecordId<T>) = this.value.compareTo(other.value)

    override fun equals(other : Any?) = if (other != null && other is RecordId<*>) this.value == other.value else false

    override fun toString() = value
    override fun hashCode() = value.hashCode()

}

typealias EmptyRecordId<T> = RecordId<T>
typealias LongRecordId<T> = RecordId<T>
typealias StringRecordId<T> = RecordId<T>