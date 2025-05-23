/*
 * Copyright © 2025 IO Club
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * Data encapsulating by data length.
 *
 * @author SiumLhahah
 */

package fyi.ioclub.commons.serialization.encapsulation

import fyi.ioclub.commons.datamodel.array.concat.concat
import fyi.ioclub.commons.datamodel.array.slice.*
import fyi.ioclub.commons.datamodel.container.Container
import fyi.ioclub.commons.serialization.natural.*
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

// Writing operations for natural encapsulated

// `ByteArraySlice.putNaturalEncapsulated(value: T): Int`
// `ByteBuffer.putNaturalEncapsulated(value: T): Int`
// `OutputStream.writeNaturalEncapsulated(value: T): Int`

fun ByteArraySlice.putNaturalEncapsulated(value: NaturalBigInteger) = nWOpEncTmpl(value, ::putNEncTmpl)
fun ByteArraySlice.putNaturalEncapsulated(value: ULong) = nWOpEncTmpl(value, ::putNEncTmpl)
fun ByteArraySlice.putNaturalEncapsulated(value: UInt) = nWOpEncTmpl(value, ::putNEncTmpl)
fun ByteArraySlice.putNaturalEncapsulated(value: UShort) = nWOpEncTmpl(value, ::putNEncTmpl)
fun ByteArraySlice.putNaturalEncapsulated(value: UByte) = putNaturalEncapsulated(value.toByte())
fun ByteArraySlice.putNaturalEncapsulated(value: Long) = putNaturalEncapsulated(value.toULong())
fun ByteArraySlice.putNaturalEncapsulated(value: Int) = putNaturalEncapsulated(value.toULong())
fun ByteArraySlice.putNaturalEncapsulated(value: Short) = putNaturalEncapsulated(value.toULong())
fun ByteArraySlice.putNaturalEncapsulated(value: Byte) = wOpNBEncTmpl(value, ::setByteAt0) { b ->
    asSlice(1, 1).array[offset] = b
}

private inline fun ByteArraySlice.putNEncTmpl(
    isSmall: Boolean, toByte: () -> Byte, toNoLead0Bas: () -> ByteArraySlice
) = wOpNEncTmpl(isSmall, toByte, toNoLead0Bas, ::setByteAt0) { it.copyInto(asSliceFrom(1)) }

fun ByteBuffer.putNaturalEncapsulated(value: NaturalBigInteger) = nWOpEncTmpl(value, ::putNEncTmpl)
fun ByteBuffer.putNaturalEncapsulated(value: ULong) = nWOpEncTmpl(value, ::putNEncTmpl)
fun ByteBuffer.putNaturalEncapsulated(value: UInt) = nWOpEncTmpl(value, ::putNEncTmpl)
fun ByteBuffer.putNaturalEncapsulated(value: UShort) = nWOpEncTmpl(value, ::putNEncTmpl)
fun ByteBuffer.putNaturalEncapsulated(value: UByte) = putNaturalEncapsulated(value.toByte())
fun ByteBuffer.putNaturalEncapsulated(value: Long) = putNaturalEncapsulated(value.toULong())
fun ByteBuffer.putNaturalEncapsulated(value: Int) = putNaturalEncapsulated(value.toULong())
fun ByteBuffer.putNaturalEncapsulated(value: Short) = putNaturalEncapsulated(value.toULong())
fun ByteBuffer.putNaturalEncapsulated(value: Byte) = wOpNBEncTmpl(value, ::put, ::put)

private inline fun ByteBuffer.putNEncTmpl(
    isSmall: Boolean, toByte: () -> Byte, toNoLead0Bas: () -> ByteArraySlice
) = wOpNEncTmpl(isSmall, toByte, toNoLead0Bas, ::put, ::put)

fun OutputStream.writeNaturalEncapsulated(value: NaturalBigInteger) = nWOpEncTmpl(value, ::writeNEncTmpl)
fun OutputStream.writeNaturalEncapsulated(value: ULong) = nWOpEncTmpl(value, ::writeNEncTmpl)
fun OutputStream.writeNaturalEncapsulated(value: UInt) = nWOpEncTmpl(value, ::writeNEncTmpl)
fun OutputStream.writeNaturalEncapsulated(value: UShort) = nWOpEncTmpl(value, ::writeNEncTmpl)
fun OutputStream.writeNaturalEncapsulated(value: UByte) = writeNaturalEncapsulated(value.toByte())
fun OutputStream.writeNaturalEncapsulated(value: Long) = writeNaturalEncapsulated(value.toULong())
fun OutputStream.writeNaturalEncapsulated(value: Int) = writeNaturalEncapsulated(value.toULong())
fun OutputStream.writeNaturalEncapsulated(value: Short) = writeNaturalEncapsulated(value.toULong())
fun OutputStream.writeNaturalEncapsulated(value: Byte) = wOpNBEncTmpl(value, ::write, ::write)

private inline fun OutputStream.writeNEncTmpl(
    isSmall: Boolean, toByte: () -> Byte, toNoLead0Bas: () -> ByteArraySlice
) = wOpNEncTmpl(isSmall, toByte, toNoLead0Bas, ::write, ::write)

private inline fun wOpNEncTmpl(
    isSmall: Boolean, toByte: () -> Byte, toNoLead0Bas: () -> ByteArraySlice,
    wOpB0: WriteByte, wOpBasLeft: (ByteArraySlice) -> Unit
): Int = if (isSmall) {
    wOpB0(toByte())
    1
} else {
    val src = toNoLead0Bas()
    val srcLen = src.length
    wOpB0((srcLen + 0b1000_0000).toByte())
    wOpBasLeft(src)
    1 + srcLen
}

private inline fun wOpNBEncTmpl(value: Byte, wOpB0: WriteByte, wOpB1: WriteByte) = if (value >= BYTE_0) {
    // `value.toUByte() >= 127`
    wOpB0(value)
    1
} else {
    wOpB0(BYTE_129)
    wOpB1(value)
    2
}
// `T.naturalEncapsulateTo(destination: ByteArraySlice): Int`

fun NaturalBigInteger.naturalEncapsulateTo(destination: ByteArraySlice) = let(destination::putNaturalEncapsulated)
fun ULong.naturalEncapsulateTo(destination: ByteArraySlice) = let(destination::putNaturalEncapsulated)
fun UInt.naturalEncapsulateTo(destination: ByteArraySlice) = let(destination::putNaturalEncapsulated)
fun UShort.naturalEncapsulateTo(destination: ByteArraySlice) = let(destination::putNaturalEncapsulated)
fun UByte.naturalEncapsulateTo(destination: ByteArraySlice) = let(destination::putNaturalEncapsulated)
fun Long.naturalEncapsulateTo(destination: ByteArraySlice) = let(destination::putNaturalEncapsulated)
fun Int.naturalEncapsulateTo(destination: ByteArraySlice) = let(destination::putNaturalEncapsulated)
fun Short.naturalEncapsulateTo(destination: ByteArraySlice) = let(destination::putNaturalEncapsulated)
fun Byte.naturalEncapsulateTo(destination: ByteArraySlice) = let(destination::putNaturalEncapsulated)

// `T.naturalEncapsulateToByteArray(): ByteArray`
// `T.naturalEncapsulateToByteArraySlice(): ByteArraySlice`
// `T.naturalEncapsulateToByteArraySliceList(): List<ByteArraySlice>`

fun NaturalBigInteger.naturalEncapsulateToByteArray(): ByteArray = nWOpEncTmpl(this, ::nEncToBArrTmpl)
fun ULong.naturalEncapsulateToByteArray(): ByteArray = nWOpEncTmpl(this, ::nEncToBArrTmpl)
fun UInt.naturalEncapsulateToByteArray(): ByteArray = nWOpEncTmpl(this, ::nEncToBArrTmpl)
fun UShort.naturalEncapsulateToByteArray(): ByteArray = nWOpEncTmpl(this, ::nEncToBArrTmpl)
fun UByte.naturalEncapsulateToByteArray() = toByte().naturalEncapsulateToByteArray()
fun Long.naturalEncapsulateToByteArray() = toULong().naturalEncapsulateToByteArray()
fun Int.naturalEncapsulateToByteArray() = toUInt().naturalEncapsulateToByteArray()
fun Short.naturalEncapsulateToByteArray() = toUShort().naturalEncapsulateToByteArray()
fun Byte.naturalEncapsulateToByteArray(): ByteArray =
    if (this >= BYTE_0) byteArrayOf(this) else byteArrayOf(BYTE_129, this)

fun NaturalBigInteger.naturalEncapsulateToByteArraySlice(): ByteArraySlice = nWOpEncTmpl(this, ::nEncToBasTmpl)
fun ULong.naturalEncapsulateToByteArraySlice(): ByteArraySlice = nWOpEncTmpl(this, ::nEncToBasTmpl)
fun UInt.naturalEncapsulateToByteArraySlice(): ByteArraySlice = nWOpEncTmpl(this, ::nEncToBasTmpl)
fun UShort.naturalEncapsulateToByteArraySlice(): ByteArraySlice = nWOpEncTmpl(this, ::nEncToBasTmpl)
fun UByte.naturalEncapsulateToByteArraySlice() = toByte().naturalEncapsulateToByteArraySlice()
fun Long.naturalEncapsulateToByteArraySlice() = toULong().naturalEncapsulateToByteArraySlice()
fun Int.naturalEncapsulateToByteArraySlice() = toUInt().naturalEncapsulateToByteArraySlice()
fun Short.naturalEncapsulateToByteArraySlice() = toUShort().naturalEncapsulateToByteArraySlice()
fun Byte.naturalEncapsulateToByteArraySlice(): ByteArraySlice = naturalEncapsulateToByteArray().asSlice()

fun NaturalBigInteger.naturalEncapsulateToByteArraySliceList(): List<ByteArraySlice> =
    nWOpEncTmpl(this, ::nEncToBasListTmpl)

fun ULong.naturalEncapsulateToByteArraySliceList(): List<ByteArraySlice> = nWOpEncTmpl(this, ::nEncToBasListTmpl)
fun UInt.naturalEncapsulateToByteArraySliceList(): List<ByteArraySlice> = nWOpEncTmpl(this, ::nEncToBasListTmpl)
fun UShort.naturalEncapsulateToByteArraySliceList(): List<ByteArraySlice> = nWOpEncTmpl(this, ::nEncToBasListTmpl)
fun UByte.naturalEncapsulateToByteArraySliceList() = toByte().naturalEncapsulateToByteArraySliceList()
fun Long.naturalEncapsulateToByteArraySliceList() = toULong().naturalEncapsulateToByteArraySliceList()
fun Int.naturalEncapsulateToByteArraySliceList() = toUInt().naturalEncapsulateToByteArraySliceList()
fun Short.naturalEncapsulateToByteArraySliceList() = toUShort().naturalEncapsulateToByteArraySliceList()
fun Byte.naturalEncapsulateToByteArraySliceList(): List<ByteArraySlice> = listOf(naturalEncapsulateToByteArraySlice())

private inline fun nEncToBArrTmpl(
    isSmall: Boolean, toByte: () -> Byte, toNoLead0Bas: () -> ByteArraySlice,
): ByteArray = if (isSmall) smallNEncToBArrTmpl(toByte) else concat(bigNEncToBasListTmpl(toNoLead0Bas))

private inline fun nEncToBasTmpl(
    isSmall: Boolean, toByte: () -> Byte, toNoLead0Bas: () -> ByteArraySlice,
): ByteArraySlice = nEncToBArrTmpl(isSmall, toByte, toNoLead0Bas).asSlice()

private inline fun nEncToBasListTmpl(
    isSmall: Boolean, toByte: () -> Byte, toNoLead0Bas: () -> ByteArraySlice,
): List<ByteArraySlice> = if (isSmall) listOf(smallNEncToBArrTmpl(toByte).asSlice())
else bigNEncToBasListTmpl(toNoLead0Bas)

private inline fun smallNEncToBArrTmpl(toByte: () -> Byte) = byteArrayOf(toByte())
private inline fun bigNEncToBasListTmpl(toNoLead0Bas: () -> ByteArraySlice) = run {
    val src = toNoLead0Bas()
    listOf(byteArrayOf((src.length + 0b1000_0000).toByte()).asSlice(), src)
}

// Common operation templates

private inline fun <R> nWOpEncTmpl(value: NaturalBigInteger, opNEnc: OpNEnc<R>) = requireNatural(value).run {
    opNEnc(this < BIG_INTEGER_128, ::toByte) {
        naturalToNoLeadingZeroByteArraySlice().apply {
            if (length >= MAX_NUMBER_ENCAPSULATION_SIZE) throw IllegalArgumentException("Big integer $value is too big for number encapsulation")
        }
    }
}

private inline fun <R> nWOpEncTmpl(value: ULong, opNEnc: OpNEnc<R>) =
    value.run { opNEnc(this < U_LONG_128, ::toByte, ::naturalToNoLeadingZeroByteArraySlice) }

private inline fun <R> nWOpEncTmpl(value: UInt, opNEnc: OpNEnc<R>) =
    value.run { opNEnc(this < U_INT_128, ::toByte, ::naturalToNoLeadingZeroByteArraySlice) }

private inline fun <R> nWOpEncTmpl(value: UShort, opNEnc: OpNEnc<R>) =
    value.run { opNEnc(this < U_SHORT_128, ::toByte, ::naturalToNoLeadingZeroByteArraySlice) }

private typealias OpNEnc<R> = (isSmall: Boolean, toByte: () -> Byte, toNoLead0Bas: () -> ByteArraySlice) -> R

// Reading operations for natural encapsulated

// `Container.Mutable<ByteArraySlice>.getEncapsulatedNatural[T](): T`
// `ByteBuffer.getEncapsulatedNatural[T](): T`
// `InputStream.readEncapsulatedNatural[T](): T`

fun Container.Mutable<ByteArraySlice>.getEncapsulatedNaturalBigInteger(): NaturalBigInteger =
    getEncNTmpl(Byte::toBigInteger, ByteArraySlice::toNaturalBigInteger)

fun Container.Mutable<ByteArraySlice>.getEncapsulatedNaturalULong(): ULong =
    getEncNTmpl(Byte::toULong, ByteArraySlice::toNaturalULong)

fun Container.Mutable<ByteArraySlice>.getEncapsulatedNaturalUInt(): UInt =
    getEncNTmpl(Byte::toUInt, ByteArraySlice::toNaturalUInt)

fun Container.Mutable<ByteArraySlice>.getEncapsulatedNaturalUShort(): UShort =
    getEncNTmpl(Byte::toUShort, ByteArraySlice::toNaturalUShort)

fun Container.Mutable<ByteArraySlice>.getEncapsulatedNaturalUByte(): UByte =
    getEncNTmpl(Byte::toUByte, ByteArraySlice::toNaturalUByte)

fun Container.Mutable<ByteArraySlice>.getEncapsulatedNaturalLong(): Long =
    getEncNTmpl(Byte::toLong, ByteArraySlice::toNaturalLong)

fun Container.Mutable<ByteArraySlice>.getEncapsulatedNaturalInt(): Int =
    getEncNTmpl(Byte::toInt, ByteArraySlice::toNaturalInt)

fun Container.Mutable<ByteArraySlice>.getEncapsulatedNaturalShort(): Short =
    getEncNTmpl(Byte::toShort, ByteArraySlice::toNaturalShort)

fun Container.Mutable<ByteArraySlice>.getEncapsulatedNaturalByte(): Byte =
    getEncNTmpl(Byte::toByte, ByteArraySlice::toNaturalByte)

fun <T> Container.Mutable<ByteArraySlice>.getEncNTmpl(bToN: Byte.() -> T, toN: ByteArraySlice.() -> T): T {
    var mov = 1
    val ret = item.run {
        rOpEncNTmpl(array[offset], bToN) { len ->
            val n = asSlice(mov, len).toN()
            mov += len
            n
        }
    }
    item = item.asSliceFrom(mov)
    return ret
}

fun ByteBuffer.getEncapsulatedNaturalBigInteger(): NaturalBigInteger =
    getEncNTmpl(Byte::toBigInteger, ::getNaturalBigInteger)

fun ByteBuffer.getEncapsulatedNaturalULong(): ULong = getEncNTmpl(Byte::toULong, ::getNaturalULong)
fun ByteBuffer.getEncapsulatedNaturalUInt(): UInt = getEncNTmpl(Byte::toUInt, ::getNaturalUInt)
fun ByteBuffer.getEncapsulatedNaturalUShort(): UShort = getEncNTmpl(Byte::toUShort, ::getNaturalUShort)
fun ByteBuffer.getEncapsulatedNaturalUByte(): UByte = getEncNTmpl(Byte::toUByte, ::getNaturalUByte)
fun ByteBuffer.getEncapsulatedNaturalLong(): Long = getEncNTmpl(Byte::toLong, ::getNaturalLong)
fun ByteBuffer.getEncapsulatedNaturalInt(): Int = getEncNTmpl(Byte::toInt, ::getNaturalInt)
fun ByteBuffer.getEncapsulatedNaturalShort(): Short = getEncNTmpl(Byte::toShort, ::getNaturalShort)
fun ByteBuffer.getEncapsulatedNaturalByte(): Byte = getEncNTmpl(Byte::toByte, ::getNaturalByte)

private inline fun <T> ByteBuffer.getEncNTmpl(bToN: Byte.() -> T, getN: (Int) -> T) = rOpEncNTmpl(get(), bToN, getN)

fun InputStream.readEncapsulatedNaturalBigInteger(): NaturalBigInteger =
    readEncNTmpl(Byte::toBigInteger, ::readNaturalBigInteger)

fun InputStream.readEncapsulatedNaturalULong(): ULong = readEncNTmpl(Byte::toULong, ::readNaturalULong)
fun InputStream.readEncapsulatedNaturalUInt(): UInt = readEncNTmpl(Byte::toUInt, ::readNaturalUInt)
fun InputStream.readEncapsulatedNaturalUShort(): UShort = readEncNTmpl(Byte::toUShort, ::readNaturalUShort)
fun InputStream.readEncapsulatedNaturalUByte(): UByte = readEncNTmpl(Byte::toUByte, ::readNaturalUByte)
fun InputStream.readEncapsulatedNaturalLong(): Long = readEncNTmpl(Byte::toLong, ::readNaturalLong)
fun InputStream.readEncapsulatedNaturalInt(): Int = readEncNTmpl(Byte::toInt, ::readNaturalInt)
fun InputStream.readEncapsulatedNaturalShort(): Short = readEncNTmpl(Byte::toShort, ::readNaturalShort)
fun InputStream.readEncapsulatedNaturalByte(): Byte = readEncNTmpl(Byte::toByte, ::readNaturalByte)

private inline fun <T> InputStream.readEncNTmpl(bToN: Byte.() -> T, readN: (Int) -> T) =
    rOpEncNTmpl(read().toByte(), bToN, readN)

private inline fun <T> rOpEncNTmpl(byte0: Byte, bToN: Byte.() -> T, rOpN: (Int) -> T): T =
    if (byte0 >= 0) byte0.bToN()
    else rOpN(byte0.toUByte().toInt() - 0b1000_0000)
