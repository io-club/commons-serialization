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
import fyi.ioclub.commons.datamodel.array.slice.*
import fyi.ioclub.commons.datamodel.container.Container
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

// Writing operations for data encapsulation

/**
 * Copies [source] data length and data value to
 * position [ByteArraySlice.offset] of destination [ByteArraySlice.array].
 *
 * @return the number of bytes written
 *
 * @throws IndexOutOfBoundsException if source length greater than destination length
 */
fun ByteArraySlice.putEncapsulated(source: ByteArraySlice) = wOpEncTmplWithOff(source, ::putNaturalEncapsulated)
{ src, off -> src.copyInto(this.asSliceFrom(off)) }

/**
 * Copies [source] data length and data value to [this] destination buffer.
 *
 * @return the number of bytes written
 */
fun ByteBuffer.putEncapsulated(source: ByteArraySlice) = wOpEncTmpl(source, ::putNaturalEncapsulated, ::put)

/**
 * Copies [source] data length and data value to [this] destination output stream.
 *
 * @return the number of bytes written
 */
fun OutputStream.writeEncapsulated(source: ByteArraySlice) = wOpEncTmpl(source, ::writeNaturalEncapsulated, ::write)

private inline fun wOpEncTmpl(
    src: ByteArraySlice, wOpNEnc: (n: Int) -> Int, wOpBas: (ByteArraySlice) -> Unit
) = wOpEncTmplWithOff(src, wOpNEnc) { bas, _ -> wOpBas(bas) }

private inline fun wOpEncTmplWithOff(
    src: ByteArraySlice, wOpNEnc: (n: Int) -> Int, wOpBasWithOff: (ByteArraySlice, off: Int) -> Unit
): Int {
    val srcLen = src.length
    val len0 = wOpNEnc(srcLen)
    wOpBasWithOff(src, len0)
    return len0 + srcLen
}

/**
 * Copies [this] data length and data value to position [offset] of [destination] array.
 *
 * @return the number of bytes written
 */
fun ByteArraySlice.encapsulateTo(destination: ByteArraySlice) = let(destination::putEncapsulated)

/**
 * Copies [this] data length and data value to [destination] buffer.
 *
 * @return the number of bytes written
 */
fun ByteArraySlice.encapsulateTo(destination: ByteBuffer): Int = let(destination::putEncapsulated)

/**
 * Copies [this] data length and data value to [destination] output stream.
 *
 * @return the number of bytes written
 */
fun ByteArraySlice.encapsulateTo(destination: OutputStream): Int = let(destination::writeEncapsulated)

/**
 * Creates a [ByteArray] and copies [this] data length and data value to it.
 *
 * @return the result [ByteArray]
 */
fun ByteArraySlice.encapsulateToByteArray(): ByteArray = concat(encapsulateToByteArraySliceList())

/**
 * Creates a [List] of [ByteArraySlice],
 * containing data length [ByteArraySlice.length] and data value [this].
 *
 * @return the result [List]
 */
fun ByteArraySlice.encapsulateToByteArraySliceList(): List<ByteArraySlice> {
    val fieldLen = length.naturalEncapsulateToByteArraySliceList()
    return fieldLen + this
}

/**
 * Creates a [List] of [ByteArray],
 * containing data length [ByteArray.size] and data value [this].
 *
 * @return the result [List]
 */
fun ByteArray.encapsulateToByteArrayList(): List<ByteArray> = listOf(size.naturalEncapsulateToByteArray(), this)

// Reading operations for data encapsulation

/**
 * Reads encapsulated data value from [this] buffer
 * by data length specified by [getEncapsulatedNaturalInt] in buffer
 * to position [ByteArraySlice.offset] of [ByteArraySlice.array] of [destination].
 *
 * @return data length
 * @throws java.nio.BufferUnderflowException If there are bytes fewer than data length
 * remaining in this buffer.
 * @throws IndexOutOfBoundsException If data length is greater than [destination] length.
 */
fun ByteBuffer.getEncapsulated(destination: ByteArraySlice): Int =
    getEncapsulatedNaturalInt().apply { let(destination::asSliceTo).also(::get) }

/**
 * Reads encapsulated data value from [this] input stream
 * by data length specified in input stream
 * to position [ByteArraySlice.offset] of [ByteArraySlice.array] of [destination].
 *
 * @return data length
 * @throws java.io.IOException if the first byte cannot be read for any reason
 * other than end of file, or if the input stream has been closed,
 * or if some other I/O error occurs.
 * @throws IndexOutOfBoundsException if data length is greater than [destination] length.
 */
fun InputStream.readEncapsulated(destination: ByteArraySlice): Int =
    readEncapsulatedNaturalInt().apply { let(destination::asSliceTo).also(::read) }

fun Container.Mutable<ByteArraySlice>.getEncapsulatedByteArray(): ByteArray {
    val len = getEncapsulatedNaturalInt()
    val arr = ByteArray(len)
    val src = item
    src.asSliceTo(len).copyInto(arr.asSlice())
    item = src.asSliceFrom(len)
    return arr
}

/**
 * Creates a [ByteArray] of size equals data length
 * specified in [this] buffer,
 * and copies data value from buffer to it.
 *
 * @return the result byte array
 * @throws java.nio.BufferUnderflowException If there are bytes fewer than data length
 * remaining in this buffer.
 */
fun ByteBuffer.getEncapsulatedByteArray(): ByteArray = getEncapsulatedNaturalInt().let(::ByteArray).also(::get)

/**
 * Creates a [ByteArray] of size equals data length
 * specified in [this] input stream,
 * and copies data value from input stream to it.
 *
 * @return the result byte array
 * @throws java.io.IOException if the first byte cannot be read for any reason
 * other than end of file, or if the input stream has been closed,
 * or if some other I/O error occurs.
 */
fun InputStream.readEncapsulatedByteArray(): ByteArray = readEncapsulatedNaturalInt().let(::ByteArray).also(::read)

fun Container.Mutable<ByteArraySlice>.getEncapsulatedByteArraySlice(): ByteArraySlice {
    val len = getEncapsulatedNaturalInt()
    val src = item
    val data = src.asSliceTo(len)
    item = src.asSliceFrom(len)
    return data
}

fun ByteBuffer.getEncapsulatedByteArraySlice(): ByteArraySlice = getEncapsulatedNaturalInt().let(::getArraySlice)

fun InputStream.readEncapsulatedByteArraySlice(): ByteArraySlice = readEncapsulatedNaturalInt().let(::readArraySlice)
