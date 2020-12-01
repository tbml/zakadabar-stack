/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.serialization

import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID
import kotlin.math.max

open class OutputCommArray(initialSize: Int = 100) {

    protected var array = UByteArray(initialSize)
    private val minimumIncrement: Int = array.size / 10

    protected var position = 0

    @PublicApi
    fun ensureCapacity(forDataSize: Int) {
        if (position + forDataSize > array.size) {
            val old = array
            array = UByteArray(array.size + max(forDataSize, minimumIncrement))
            old.copyInto(array)
        }
    }

    open fun write(value: Byte) {
        ensureCapacity(1)
        array[position ++] = value.toUByte()
    }

    open fun write(value: Int) {
        ensureCapacity(4)
        array[position ++] = (value shr 24).toUByte()
        array[position ++] = (value shr 16).toUByte()
        array[position ++] = (value shr 8).toUByte()
        array[position ++] = value.toUByte()
    }

    open fun write(value: Long) {
        ensureCapacity(8)
        array[position ++] = (value shr 56).toUByte()
        array[position ++] = (value shr 48).toUByte()
        array[position ++] = (value shr 40).toUByte()
        array[position ++] = (value shr 32).toUByte()
        array[position ++] = (value shr 24).toUByte()
        array[position ++] = (value shr 16).toUByte()
        array[position ++] = (value shr 8).toUByte()
        array[position ++] = value.toUByte()
    }

    open fun write(value: Long?) {
        if (value == null) {
            ensureCapacity(1)
            array[position ++] = 0xf0.toUByte()
            return
        }

        ensureCapacity(9)

        array[position ++] = 0xff.toUByte()
        write(value)
    }

    open fun write(value: Double) = write(value.toBits())

    open fun write(value: Float) = write(value.toBits())

    open fun write(value: UUID) {
        write(value.msbm)
        write(value.msbl)
        write(value.lsbm)
        write(value.lsbl)
    }

    open fun write(value: String) {
        val utf8 = value.encodeToByteArray()
        write(utf8.size)
        ensureCapacity(utf8.size)
        utf8.copyInto(array.asByteArray(), position)
        position += utf8.size
    }

    @PublicApi
    open fun writeOptString(value: String?) {
        ensureCapacity(1)

        if (value == null) {
            array[position ++] = 0xf0.toUByte()
            return
        } else {
            array[position ++] = 0xff.toUByte()
        }

        write(value)
    }

    open fun write(value: ByteArray) {
        ensureCapacity(value.size + 4)
        write(value.size)
        value.asUByteArray().copyInto(array, position, 0, value.size)
        position += value.size
    }

    fun pack(): ByteArray {
        val packed = ByteArray(position)
        array.copyInto(packed.asUByteArray(), 0, 0, position)
        return packed
    }
}