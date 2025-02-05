/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

import kotlinx.serialization.Serializable

const val versionMask = 0xffff0fff.toInt()
const val version = 0x00004000
const val variantMask = 0x3fffffff
const val variant = 0x80000000.toInt()

val hexChars = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

@Serializable
class UUID : Comparable<UUID> {

    companion object {
        val NIL = UUID(IntArray(4) { 0 }, 0)
        val mask = 0xffffffff.toULong()
    }

    val msbm: Int
    val msbl: Int
    val lsbm: Int
    val lsbl: Int

    val msb: Long
        get() = (((msbm.toULong()) shl 32) or (msbl.toULong() and mask)).toLong()

    val lsb: Long
        get() = (((lsbm.toULong()) shl 32) or (lsbl.toULong() and mask)).toLong()

    constructor() {
        val array = fourRandomInt()

        msbm = array[0]
        msbl = (array[1] and versionMask) or version
        lsbm = (array[2] and variantMask) or variant
        lsbl = array[3]
    }

    constructor(msbm: Int, msbl: Int, lsbm: Int, lsbl: Int) {
        this.msbm = msbm
        this.msbl = msbl
        this.lsbm = lsbm
        this.lsbl = lsbl
    }

    constructor(array: IntArray, position: Int) {
        var idx = position

        msbm = array[idx ++]
        msbl = array[idx ++]
        lsbm = array[idx ++]
        lsbl = array[idx]
    }

    constructor(msb: Long, lsb: Long) {
        msbm = (msb.toULong() shr 32).toInt()
        msbl = (msb.toULong() and mask).toInt()
        lsbm = (lsb.toULong() shr 32).toInt()
        lsbl = (lsb.toULong() and mask).toInt()
    }

    constructor(uuid: String) {
        val s = uuid.replace("-", "")

        msbm = s.substring(0, 8).toLong(16).toInt()
        msbl = s.substring(8, 16).toLong(16).toInt()
        lsbm = s.substring(16, 24).toLong(16).toInt()
        lsbl = s.substring(24, 32).toLong(16).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (other is UUID) {
            if (other.msbm != this.msbm) return false
            if (other.msbl != this.msbl) return false
            if (other.lsbm != this.lsbm) return false
            if (other.lsbl != this.lsbl) return false
            return true
        } else {
            return false
        }
    }

    override fun hashCode() = msbm

    override fun toString(): String {
        val chars = CharArray(36)

        digits(msbm, chars, 0, 8)
        chars[8] = '-'
        digits(msbl shr 16, chars, 9, 4)
        chars[13] = '-'
        digits(msbl, chars, 14, 4)
        chars[18] = '-'
        digits(lsbm shr 16, chars, 19, 4)
        chars[23] = '-'
        digits(lsbm, chars, 24, 4)
        digits(lsbl, chars, 28, 8)

        return chars.concatToString()
    }

    private fun digits(value: Int, chars: CharArray, position: Int, digits: Int) {
        var i = position + digits
        var v = value

        while (i > position) {
            chars[-- i] = hexChars[v and 0xf]
            v = v shr 4
        }
    }

    override fun compareTo(other: UUID): Int {
        var r = msbm.compareTo(other.msbm)
        if (r != 0) return r

        r = msbl.compareTo(other.msbl)
        if (r != 0) return r

        r = lsbm.compareTo(other.lsbm)
        if (r != 0) return r

        return lsbl.compareTo(other.lsbl)
    }
}