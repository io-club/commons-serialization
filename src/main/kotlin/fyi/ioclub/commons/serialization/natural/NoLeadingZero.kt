/*
 * Copyright © 2025 IO Club
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* To remove leading zeros. */

package fyi.ioclub.commons.serialization.natural

import fyi.ioclub.commons.datamodel.array.slice.ByteArraySlice
import fyi.ioclub.commons.datamodel.array.slice.asSliceFrom
import fyi.ioclub.commons.datamodel.array.slice.put
import fyi.ioclub.commons.datamodel.array.slice.write
import java.io.OutputStream
import java.nio.ByteBuffer


// To get array index of the first non-zero byte

fun ByteArraySlice.arrayFindFirstNonZeroByte(): Int = with(arrayIterator()) {
    Iterable { this }.any { it != BYTE_0 }
    nextIndex()
}

// To get no-leading-zero byte array

fun ByteArraySlice.noLeadingZeroTo(destination: ByteArraySlice): Int = with(toNoLeadingZeroByteArraySlice()) {
    copyInto(destination)
    length
}

fun ByteArraySlice.toNoLeadingZeroByteArray(): ByteArray = toNoLeadingZeroByteArraySlice().toSlicedArray()

fun ByteArraySlice.toNoLeadingZeroByteArraySlice(): ByteArraySlice = asSliceFrom(arrayFindFirstNonZeroByte())

// To put into byte array

fun ByteArraySlice.putNaturalNoLeadingZero(value: NaturalBigInteger) =
    wOpTmpl(value::naturalToByteArraySlice, ::copyInto)

fun ByteArraySlice.putNaturalNoLeadingZero(value: Long) = putNoLead0LISTmpl(value::naturalToByteArraySlice)
fun ByteArraySlice.putNaturalNoLeadingZero(value: Int) = putNoLead0LISTmpl(value::naturalToByteArraySlice)
fun ByteArraySlice.putNaturalNoLeadingZero(value: Short) = putNoLead0LISTmpl(value::naturalToByteArraySlice)

private inline fun ByteArraySlice.putNoLead0LISTmpl(nToBas: () -> ByteArraySlice) = wOpTmpl(nToBas, ::noLeadingZeroTo)

fun NaturalBigInteger.toNoLeadingZero(destination: ByteArraySlice) = let(destination::putNaturalNoLeadingZero)
fun Long.toNoLeadingZero(destination: ByteArraySlice) = let(destination::putNaturalNoLeadingZero)
fun Int.toNoLeadingZero(destination: ByteArraySlice) = let(destination::putNaturalNoLeadingZero)
fun Short.toNoLeadingZero(destination: ByteArraySlice) = let(destination::putNaturalNoLeadingZero)

fun NaturalBigInteger.toNoLeadingZeroByteArray(): ByteArray = naturalToByteArray(AUTO)
fun Long.toNoLeadingZeroByteArray(): ByteArray = naturalToNoLeadingZeroByteArraySlice().toSlicedArray()
fun Int.toNoLeadingZeroByteArray(): ByteArray = naturalToNoLeadingZeroByteArraySlice().toSlicedArray()
fun Short.toNoLeadingZeroByteArray(): ByteArray = naturalToNoLeadingZeroByteArraySlice().toSlicedArray()

fun NaturalBigInteger.naturalToNoLeadingZeroByteArraySlice(): ByteArraySlice = naturalToByteArraySlice(AUTO)

fun Long.naturalToNoLeadingZeroByteArraySlice(): ByteArraySlice =
    naturalToByteArraySlice().toNoLeadingZeroByteArraySlice()

fun Int.naturalToNoLeadingZeroByteArraySlice(): ByteArraySlice =
    naturalToByteArraySlice().toNoLeadingZeroByteArraySlice()

fun Short.naturalToNoLeadingZeroByteArraySlice(): ByteArraySlice =
    naturalToByteArraySlice().toNoLeadingZeroByteArraySlice()

// To put into byte buffer

fun ByteBuffer.putNaturalNoLeadingZero(value: NaturalBigInteger) =
    putNoLead0Tmpl(value::naturalToNoLeadingZeroByteArraySlice)

fun ByteBuffer.putNaturalNoLeadingZero(value: Long) = putNoLead0Tmpl(value::naturalToNoLeadingZeroByteArraySlice)
fun ByteBuffer.putNaturalNoLeadingZero(value: Int) = putNoLead0Tmpl(value::naturalToNoLeadingZeroByteArraySlice)
fun ByteBuffer.putNaturalNoLeadingZero(value: Short) = putNoLead0Tmpl(value::naturalToNoLeadingZeroByteArraySlice)

private inline fun ByteBuffer.putNoLead0Tmpl(toNoLead0Bas: () -> ByteArraySlice) = wOpTmpl(toNoLead0Bas, ::put)

// To write into output stream

fun OutputStream.writeNaturalNoLeadingZero(value: NaturalBigInteger) =
    writeNoLead0Tmpl(value::naturalToNoLeadingZeroByteArraySlice)

fun OutputStream.writeNaturalNoLeadingZero(value: Long): Int =
    writeNoLead0Tmpl(value::naturalToNoLeadingZeroByteArraySlice)

fun OutputStream.writeNaturalNoLeadingZero(value: Int) = writeNoLead0Tmpl(value::naturalToNoLeadingZeroByteArraySlice)

fun OutputStream.writeNaturalNoLeadingZero(value: Short) = writeNoLead0Tmpl(value::naturalToNoLeadingZeroByteArraySlice)

private inline fun OutputStream.writeNoLead0Tmpl(toNoLead0Bas: () -> ByteArraySlice) = wOpTmpl(toNoLead0Bas, ::write)

private inline fun wOpTmpl(toBas: () -> ByteArraySlice, writer: (ByteArraySlice) -> Unit): Int {
    val bas = toBas()
    writer(bas)
    return bas.length
}
