/*
 * Copyright © 2025 IO Club
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* To get non-negative integers */

package fyi.ioclub.commons.serialization.natural

import fyi.ioclub.commons.datamodel.array.iterator.iterateInReverse
import fyi.ioclub.commons.datamodel.array.slice.*
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

fun ByteArraySlice.toNaturalBigInteger(): NaturalBigInteger = let(::NaturalBigInteger)

fun ByteArraySlice.toNaturalLong(): Long =
    toNPriTmpl(LONG_0) { value, ub, shrBits -> value + ub.toLong() shl shrBits }

fun ByteArraySlice.toNaturalInt(): Int =
    toNPriTmpl(INT_0) { value, ub, shrBits -> value + ub.toInt() shl shrBits }

fun ByteArraySlice.toNaturalShort(): Short =
    toNPriTmpl(SHORT_0) { value, ub, shrBits -> (value + ub.toInt() shl shrBits).toShort() }

fun ByteArraySlice.toNaturalByte(): Byte =
    toNPriTmpl(BYTE_0) { value, ub, shrBits -> (value + ub.toInt() shl shrBits).toByte() }

/** For [ByteArraySlice.toNaturalLong] to [ByteArraySlice.toNaturalByte], */
private inline fun <T> ByteArraySlice.toNPriTmpl(initVal: T, plusAndShl: (T, UByte, Int) -> T): T {
    val arr = array
    var j = endArrayIndexExclusive
    val iProgress = (0..<length * Byte.SIZE_BITS step Byte.SIZE_BITS)
    return iProgress.fold(initVal) { value, i -> plusAndShl(value, arr[--j].toUByte(), i) }
}

fun ByteBuffer.getNaturalBigInteger(length: Int) = getNTmpl(length, ByteArraySlice::toNaturalBigInteger)
fun ByteBuffer.getNaturalLong(length: Int = Long.SIZE_BYTES) = getNTmpl(length, ByteArraySlice::toNaturalLong)
fun ByteBuffer.getNaturalInt(length: Int = Int.SIZE_BYTES) = getNTmpl(length, ByteArraySlice::toNaturalInt)
fun ByteBuffer.getNaturalShort(length: Int = Short.SIZE_BYTES) = getNTmpl(length, ByteArraySlice::toNaturalShort)
fun ByteBuffer.getNaturalByte(length: Int = Byte.SIZE_BYTES) = getNTmpl(length, ByteArraySlice::toNaturalByte)

/** For [ByteBuffer.getNaturalBigInteger] to [ByteBuffer.getNaturalByte]. */
private inline fun <T> ByteBuffer.getNTmpl(length: Int, basToN: ByteArraySlice.() -> T): T =
    basToN(getArraySlice(length))

fun InputStream.readNaturalBigInteger(length: Int) = readNTmpl(length, ByteArraySlice::toNaturalBigInteger)
fun InputStream.readNaturalLong(length: Int = Long.SIZE_BYTES) = readNTmpl(length, ByteArraySlice::toNaturalLong)
fun InputStream.readNaturalInt(length: Int = Int.SIZE_BYTES) = readNTmpl(length, ByteArraySlice::toNaturalInt)
fun InputStream.readNaturalShort(length: Int = Short.SIZE_BYTES) = readNTmpl(length, ByteArraySlice::toNaturalShort)
fun InputStream.readNaturalByte(length: Int = Byte.SIZE_BYTES) = readNTmpl(length, ByteArraySlice::toNaturalByte)

/** For [InputStream.readNaturalBigInteger] to [InputStream.readNaturalByte]. */
private inline fun <reified T> InputStream.readNTmpl(len: Int, basToN: ByteArraySlice.() -> T): T =
    readArraySlice(len).basToN()

// To put non-negative integers

// `ByteArraySlice.putNatural(value: T, length: Int)`, `T` for integral type

/**
 * Like reading operations for [NaturalBigInteger],
 * no choice of [NATURAL_BIG_INTEGER_AUTO_LEAST_LENGTH] as length.
 *
 * For such objective, use [ByteArraySlice.putNaturalNoLeadingZero] instead.
 */
fun ByteArraySlice.putNatural(value: NaturalBigInteger): Int {
    requireNatural(value)
    val srcArr = value.toByteArray()
    val srcNon0From = if (srcArr[0] == BYTE_0) 0 else 1
    val non0Src = srcArr.asSliceFrom(srcNon0From)
    val dstLen = this.length
    val dstNon0From = dstLen - non0Src.length
    asSliceTo(dstNon0From).fill(BYTE_0)
    non0Src.copyInto(asSliceFrom(dstNon0From))
    return dstLen
}

fun ByteArraySlice.putNatural(value: Long): Int = putNTmpl(value) { value, shrBits -> (value ushr shrBits).toByte() }
fun ByteArraySlice.putNatural(value: Int): Int = putNTmpl(value) { value, shrBits -> (value ushr shrBits).toByte() }
fun ByteArraySlice.putNatural(value: Short): Int = putNatural(value.toInt())
fun ByteArraySlice.putNatural(value: Byte): Int = fillBasTmpl {
    val lead0To = endArrayIndexInclusive
    array[lead0To] = value
    lead0To
}

/** For [ByteArraySlice.putNatural]. */
private inline fun <T> ByteArraySlice.putNTmpl(value: T, shrToByte: (T, Int) -> Byte) = fillBasTmpl {
    arrayIterator(endArrayIndexExclusive).iterateInReverse().run {
        Iterable { this }.forEachIndexed { i, _ -> set(shrToByte(value, i * Byte.SIZE_BITS)) }
        previousIndex()
    }
}

/** @param fillAfterLead0 returns array index. */
private inline fun ByteArraySlice.fillBasTmpl(fillAfterLead0: () -> Int): Int {
    val lead0To = fillAfterLead0()
    array.asSliceFrom(offset, lead0To).fill(BYTE_0)
    return length
}

// `ByteBuffer.putNatural(value: T, length: Int)`, `T` for integral type

fun ByteBuffer.putNatural(value: NaturalBigInteger, length: Int = AUTO) = writingOpNTmpl(value, length, ::put)

fun ByteBuffer.putNatural(value: Long, length: Int = Long.SIZE_BYTES) =
    putNTmpl(value, length, ByteArraySlice::putNatural)

fun ByteBuffer.putNatural(value: Int, length: Int = Int.SIZE_BYTES) =
    putNTmpl(value, length, ByteArraySlice::putNatural)

fun ByteBuffer.putNatural(value: Short, length: Int = Short.SIZE_BYTES) =
    putNTmpl(value, length, ByteArraySlice::putNatural)

fun ByteBuffer.putNatural(value: Byte, length: Int = Byte.SIZE_BYTES) =
    putNTmpl(value, length, ByteArraySlice::putNatural)

/** For [ByteBuffer.putNatural]. */
private inline fun <T> ByteBuffer.putNTmpl(value: T, len: Int, putN: ByteArraySlice.(T) -> Unit): Int =
    writingOpNTmpl(value, len, putN, ::put)

// `OutputStream.writeNatural(value: T,  length: Int): Int`, `T` for integral type

fun OutputStream.writeNatural(value: NaturalBigInteger, length: Int = AUTO): Int =
    writingOpNTmpl(value, length, ::write)

fun OutputStream.writeNatural(value: Long, length: Int = Long.SIZE_BYTES) =
    writeNTmpl(value, length, ByteArraySlice::putNatural)

fun OutputStream.writeNatural(value: Int, length: Int = Int.SIZE_BYTES) =
    writeNTmpl(value, length, ByteArraySlice::putNatural)

fun OutputStream.writeNatural(value: Short, length: Int = Short.SIZE_BYTES) =
    writeNTmpl(value, length, ByteArraySlice::putNatural)

fun OutputStream.writeNatural(value: Byte, length: Int = Byte.SIZE_BYTES) =
    writeNTmpl(value, length, ByteArraySlice::putNatural)

/** For [OutputStream.writeNatural]. */
private inline fun <T> OutputStream.writeNTmpl(value: T, len: Int, putN: ByteArraySlice.(T) -> Unit): Int =
    writingOpNTmpl(value, len, putN, ::write)

/** For [ByteBuffer.putNatural] and [OutputStream.writeNatural] for [NaturalBigInteger]. */
private inline fun writingOpNTmpl(value: NaturalBigInteger, len: Int, writeBas: (ByteArraySlice) -> Unit): Int {
    requireNatural(value)
    val bas = if (len == AUTO) value.naturalToByteArraySlice()
    else ByteArray(len).asSlice().also { dst -> dst.putNatural(value) }
    writeBas(bas)
    return bas.length
}

/** For [ByteBuffer.putNTmpl] and [OutputStream.writeNTmpl]. */
private inline fun <T> writingOpNTmpl(
    value: T, len: Int, putN: ByteArraySlice.(T) -> Unit, writeBas: (ByteArraySlice) -> Unit,
): Int {
    val dst = ByteArray(len).asSlice()
    dst.putN(value)
    writeBas(dst)
    return len
}

// `T.naturalToBytes(destination: ByteArraySlice,  length: Int): Int`, `T` for integral type

fun NaturalBigInteger.naturalTo(destination: ByteArraySlice) = let(destination::putNatural)
fun Long.naturalTo(destination: ByteArraySlice) = let(destination::putNatural)
fun Int.naturalTo(destination: ByteArraySlice) = let(destination::putNatural)
fun Short.naturalTo(destination: ByteArraySlice) = let(destination::putNatural)
fun Byte.naturalTo(destination: ByteArraySlice) = let(destination::putNatural)

// `T.naturalToBytes(length: Int): ByteArray`, `T` for integral type

fun NaturalBigInteger.naturalToByteArray(length: Int = AUTO): ByteArray =
    naturalToByteArraySlice(length).run { if (length == AUTO) toSlicedArray() else array }

fun Long.naturalToByteArray(length: Int = Long.SIZE_BYTES): ByteArray = naturalToByteArraySlice(length).array
fun Int.naturalToByteArray(length: Int = Int.SIZE_BYTES): ByteArray = naturalToByteArraySlice(length).array
fun Short.naturalToByteArray(length: Int = Short.SIZE_BYTES): ByteArray = naturalToByteArraySlice(length).array
fun Byte.naturalToByteArray(length: Int = Byte.SIZE_BYTES): ByteArray = naturalToByteArraySlice(length).array

fun NaturalBigInteger.naturalToByteArraySlice(length: Int = AUTO): ByteArraySlice = with(let(::requireNatural)) {
    if (length == AUTO) {
        toByteArray().run { if (first() != BYTE_0) asSlice() else asSliceFrom(1) }
    } else nToBasTmpl(length, ::naturalTo)
}

fun Long.naturalToByteArraySlice(length: Int = Long.SIZE_BYTES): ByteArraySlice = nToBasTmpl(length, ::naturalTo)
fun Int.naturalToByteArraySlice(length: Int = Int.SIZE_BYTES): ByteArraySlice = nToBasTmpl(length, ::naturalTo)
fun Short.naturalToByteArraySlice(length: Int = Short.SIZE_BYTES): ByteArraySlice = nToBasTmpl(length, ::naturalTo)
fun Byte.naturalToByteArraySlice(length: Int = Byte.SIZE_BYTES): ByteArraySlice = nToBasTmpl(length, ::naturalTo)

private inline fun nToBasTmpl(length: Int, nTo: (ByteArraySlice) -> Unit): ByteArraySlice =
    ByteArray(length).asSlice().also(nTo)
