/*
 * Copyright © 2025 IO Club
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fyi.ioclub.commons.serialization.encapsulation

import fyi.ioclub.commons.datamodel.array.concat.concat
import fyi.ioclub.commons.datamodel.array.slice.ByteArraySlice
import fyi.ioclub.commons.datamodel.array.slice.asSliceTo
import fyi.ioclub.commons.datamodel.array.slice.put
import fyi.ioclub.commons.serialization.natural.NaturalBigInteger
import java.io.OutputStream
import java.nio.ByteBuffer

internal fun checkIndexBounds(capacity: Int, offset: Int, length: Int) {
    if (offset < 0) throw IndexOutOfBoundsException(offset)
    (offset + length).let { if (it > capacity) throw IndexOutOfBoundsException(it) }
}

internal val BIG_INTEGER_128: NaturalBigInteger = 0b1000_0000L.toBigInteger()
internal const val U_LONG_128: ULong = 0b1000_0000U
internal const val U_INT_128: ULong = 0b1000_0000U
internal const val U_SHORT_128: UShort = 0b1000_0000U
internal const val BYTE_0: Byte = 0

internal const val MAX_NUMBER_ENCAPSULATION_SIZE = 0b1000_0000

internal typealias WriteByte = (Byte) -> Unit

internal fun ByteArraySlice.setByteAt0(b: Byte) {
    asSliceTo(1).array[offset] = b
}

internal fun OutputStream.write(b: Byte): Unit = write(b.toInt())

internal fun Byte.toBigInteger() = toLong().toBigInteger()

internal const val BYTE_129: Byte = 0b1000_0001.toByte()
