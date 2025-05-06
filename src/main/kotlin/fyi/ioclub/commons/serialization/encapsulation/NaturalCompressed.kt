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
 * Data encapsulateing by its size.
 *
 * @author SiumLhahah
 */

package fyi.ioclub.commons.serialization.encapsulation

import fyi.ioclub.commons.serialization.natural.BIG_INTEGER_NATURAL_DEFAULT_SIZE
import fyi.ioclub.commons.serialization.natural.findFirstNonZeroByte
import fyi.ioclub.commons.serialization.natural.getNaturalBigInteger
import fyi.ioclub.commons.serialization.natural.getNaturalByte
import fyi.ioclub.commons.serialization.natural.getNaturalInt
import fyi.ioclub.commons.serialization.natural.getNaturalLong
import fyi.ioclub.commons.serialization.natural.getNaturalShort
import fyi.ioclub.commons.serialization.natural.getNaturalUInt
import fyi.ioclub.commons.serialization.natural.getNaturalULong
import fyi.ioclub.commons.serialization.natural.naturalToBytes
import fyi.ioclub.commons.serialization.natural.readNaturalBigInteger
import fyi.ioclub.commons.serialization.natural.readNaturalByte
import fyi.ioclub.commons.serialization.natural.readNaturalInt
import fyi.ioclub.commons.serialization.natural.readNaturalLong
import fyi.ioclub.commons.serialization.natural.readNaturalShort
import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * Write a natural number to a buffer.
 *
 * @param value  natural number
 * @param maxLen maximum number of bytes for the natural number to be encapsulated to
 * @return the number of bytes written
 */
private inline fun <reified T> bufferEncapsulateLIS(
    value: T,
    maxLen: Int,
    isSmall: Boolean,
    toByte: (T) -> Byte,
    naturalToBytes: (T, Int) -> ByteArray,
    firstByteWriter: (Byte) -> Unit,
    byteArrayWriter: (ByteArray, Int, Int) -> Unit,
): Int {
    if (isSmall) {
        firstByteWriter(toByte(value))
        return 1
    }
    val src = naturalToBytes(value, maxLen)
    val srcOff = src.findFirstNonZeroByte()
    val srcLen = maxLen - srcOff
    firstByteWriter((srcLen + 0b1000_0000).toByte())
    byteArrayWriter(src, srcOff, srcLen)
    return 1 + srcLen
}

/**
 * Write a natural number to the byte array.
 *
 * @param value  natural number
 * @param off    offset
 * @param maxLen maximum number of bytes for the natural number to be encapsulated to
 * @return the number of bytes written
 */
private inline fun <reified T> ByteArray.encapsulateLIS(
    value: T,
    off: Int,
    maxLen: Int,
    isSmall: Boolean,
    toByte: (T) -> Byte,
    naturalToBytes: (T, Int) -> ByteArray,
) = bufferEncapsulateLIS(
    value,
    maxLen,
    isSmall,
    toByte,
    naturalToBytes,
    { this[off] = it },
    { src, srcOff, len -> System.arraycopy(src, srcOff, this, off + 1, len) },
)

private val BIG_INTEGER_128 = BigInteger.valueOf(0b1000_0000L)

private fun BigInteger.requireNonNeg() {
    if (signum() < 0) throw IllegalArgumentException("As a natural number, the big integer must not be negative")
}

private fun BigInteger.naturalToBytesLimited(len: Int) =
    (naturalToBytes(len)).also { if (it.size >= 0b1000_0000) throw IllegalArgumentException("The big integer is too big") }

@JvmOverloads
fun ByteArray.encapsulateNaturalCompressed(value: BigInteger, off: Int = 0, maxLen: Int = BIG_INTEGER_NATURAL_DEFAULT_SIZE) = value.run {
    requireNonNeg()
    encapsulateLIS(this, off, maxLen, this < BIG_INTEGER_128, BigInteger::toByte, BigInteger::naturalToBytesLimited)
}

@JvmOverloads
fun ByteArray.encapsulateNaturalCompressed(value: ULong, off: Int = 0, maxLen: Int = ULong.SIZE_BYTES) =
    encapsulateLIS(value, off, maxLen, value < 0b1000_0000UL, ULong::toByte, ULong::naturalToBytes)

@JvmOverloads
fun ByteArray.encapsulateNaturalCompressed(value: UInt, off: Int = 0, maxLen: Int = UInt.SIZE_BYTES) =
    encapsulateLIS(value, off, maxLen, value < 0b1000_0000U, UInt::toByte, UInt::naturalToBytes)

private const val U_SHORT_128: UShort = 0b1000_0000U

@JvmOverloads
fun ByteArray.encapsulateNaturalCompressed(value: UShort, off: Int = 0, maxLen: Int = UShort.SIZE_BYTES) =
    encapsulateLIS(value, off, maxLen, value < U_SHORT_128, UShort::toByte, UShort::naturalToBytes)

@JvmOverloads
fun ByteArray.encapsulateNaturalCompressed(value: UByte, off: Int = 0) = encapsulateNaturalCompressed(value.toByte(), off)

@JvmOverloads
fun ByteArray.encapsulateNaturalCompressed(value: Long, off: Int = 0, maxLen: Int = Long.SIZE_BYTES) =
    encapsulateNaturalCompressed(value.toULong(), off, maxLen)

@JvmOverloads
fun ByteArray.encapsulateNaturalCompressed(value: Int, off: Int = 0, maxLen: Int = Int.SIZE_BYTES) =
    encapsulateNaturalCompressed(value.toUInt(), off, maxLen)

@JvmOverloads
fun ByteArray.encapsulateNaturalCompressed(value: Short, off: Int = 0, maxLen: Int = Short.SIZE_BYTES) =
    encapsulateNaturalCompressed(value.toUShort(), off, maxLen)

private const val BYTE_129: Byte = 0b1000_0001.toByte()

private inline fun bufferEncapsulateNaturalB(value: Byte, byteWriter: (Byte) -> Unit) = if (value >= 0) {
    byteWriter(value)
    1
} else {
    byteWriter(BYTE_129)
    byteWriter(value)
    2
}

@JvmOverloads
fun ByteArray.encapsulateNaturalCompressed(value: Byte, off: Int = 0): Int {
    var pos = off
    return bufferEncapsulateNaturalB(value) { this[pos++] = it }
}

private inline fun <reified T> ByteBuffer.encapsulateLIS(
    value: T,
    maxLen: Int,
    isSmall: Boolean,
    toByte: (T) -> Byte,
    naturalToBytes: (T, Int) -> ByteArray,
) = bufferEncapsulateLIS(value, maxLen, isSmall, toByte, naturalToBytes, ::put, ::put)

@JvmOverloads
fun ByteBuffer.encapsulateNaturalCompressed(value: BigInteger, maxLen: Int = BIG_INTEGER_NATURAL_DEFAULT_SIZE) = value.run {
    requireNonNeg()
    encapsulateLIS(this, maxLen, this < BIG_INTEGER_128, BigInteger::toByte, BigInteger::naturalToBytesLimited)
}

@JvmOverloads
fun ByteBuffer.encapsulateNaturalCompressed(value: ULong, maxLen: Int = ULong.SIZE_BYTES) =
    encapsulateLIS(value, maxLen, value < 0b1000_0000UL, ULong::toByte, ULong::naturalToBytes)

@JvmOverloads
fun ByteBuffer.encapsulateNaturalCompressed(value: UInt, maxLen: Int = UInt.SIZE_BYTES) =
    encapsulateLIS(value, maxLen, value < 0b1000_0000UL, UInt::toByte, UInt::naturalToBytes)

@JvmOverloads
fun ByteBuffer.encapsulateNaturalCompressed(value: UShort, maxLen: Int = UShort.SIZE_BYTES) =
    encapsulateLIS(value, maxLen, value < U_SHORT_128, UShort::toByte, UShort::naturalToBytes)

fun ByteBuffer.encapsulateNaturalCompressed(value: UByte) = encapsulateNaturalCompressed(value.toByte())

@JvmOverloads
fun ByteBuffer.encapsulateNaturalCompressed(value: Long, maxLen: Int = Long.SIZE_BYTES) = encapsulateNaturalCompressed(value.toULong(), maxLen)

@JvmOverloads
fun ByteBuffer.encapsulateNaturalCompressed(value: Int, maxLen: Int = Int.SIZE_BYTES) = encapsulateNaturalCompressed(value.toUInt(), maxLen)

@JvmOverloads
fun ByteBuffer.encapsulateNaturalCompressed(value: Short, maxLen: Int = Short.SIZE_BYTES) = encapsulateNaturalCompressed(value.toUShort(), maxLen)

fun ByteBuffer.encapsulateNaturalCompressed(value: Byte) = bufferEncapsulateNaturalB(value, ::put)

private inline fun <reified T> OutputStream.encapsulateLIS(
    value: T,
    maxLen: Int,
    isSmall: Boolean,
    toByte: (T) -> Byte,
    naturalToBytes: (T, Int) -> ByteArray,
) = bufferEncapsulateLIS(value, maxLen, isSmall, toByte, naturalToBytes, { write(it.toInt()) }, ::write)

@JvmOverloads
fun OutputStream.encapsulateNaturalCompressed(value: BigInteger, maxLen: Int = BIG_INTEGER_NATURAL_DEFAULT_SIZE) = value.run {
    requireNonNeg()
    encapsulateLIS(this, maxLen, this < BIG_INTEGER_128, BigInteger::toByte, BigInteger::naturalToBytesLimited)
}

@JvmOverloads
fun OutputStream.encapsulateNaturalCompressed(value: ULong, maxLen: Int = ULong.SIZE_BYTES) =
    encapsulateLIS(value, maxLen, value < 0b1000_0000UL, ULong::toByte, ULong::naturalToBytes)

@JvmOverloads
fun OutputStream.encapsulateNaturalCompressed(value: UInt, maxLen: Int = UInt.SIZE_BYTES) =
    encapsulateLIS(value, maxLen, value < 0b1000_0000UL, UInt::toByte, UInt::naturalToBytes)

@JvmOverloads
fun OutputStream.encapsulateNaturalCompressed(value: UShort, maxLen: Int = UShort.SIZE_BYTES) =
    encapsulateLIS(value, maxLen, value < U_SHORT_128, UShort::toByte, UShort::naturalToBytes)

fun OutputStream.encapsulateNaturalCompressed(value: UByte) = encapsulateNaturalCompressed(value.toByte())

@JvmOverloads
fun OutputStream.encapsulateNaturalCompressed(value: Long, maxLen: Int = Long.SIZE_BYTES) = encapsulateNaturalCompressed(value.toULong(), maxLen)

@JvmOverloads
fun OutputStream.encapsulateNaturalCompressed(value: Int, maxLen: Int = Int.SIZE_BYTES) = encapsulateNaturalCompressed(value.toUInt(), maxLen)

@JvmOverloads
fun OutputStream.encapsulateNaturalCompressed(value: Short, maxLen: Int = Short.SIZE_BYTES) = encapsulateNaturalCompressed(value.toUShort(), maxLen)

fun OutputStream.encapsulateNaturalCompressed(value: Byte) = bufferEncapsulateNaturalB(value) { write(it.toInt()) }

@JvmOverloads
fun BigInteger.naturalEncapsulateToCompressed(dst: ByteArray, off: Int = 0, maxLen: Int = BIG_INTEGER_NATURAL_DEFAULT_SIZE) =
    dst.encapsulateNaturalCompressed(this, off, maxLen)

@JvmOverloads
fun ULong.naturalEncapsulateToCompressed(dst: ByteArray, off: Int = 0, maxLen: Int = ULong.SIZE_BYTES) =
    dst.encapsulateNaturalCompressed(this, off, maxLen)

@JvmOverloads
fun UInt.naturalEncapsulateToCompressed(dst: ByteArray, off: Int = 0, maxLen: Int = UInt.SIZE_BYTES) =
    dst.encapsulateNaturalCompressed(this, off, maxLen)

@JvmOverloads
fun UShort.naturalEncapsulateToCompressed(dst: ByteArray, off: Int = 0, maxLen: Int = UShort.SIZE_BYTES) =
    dst.encapsulateNaturalCompressed(this, off, maxLen)

fun UByte.naturalEncapsulateToCompressed(dst: ByteArray, off: Int = 0) = dst.encapsulateNaturalCompressed(this, off)

@JvmOverloads
fun Long.naturalEncapsulateToCompressed(dst: ByteArray, off: Int = 0, maxLen: Int = Long.SIZE_BYTES) =
    dst.encapsulateNaturalCompressed(this, off, maxLen)

@JvmOverloads
fun Int.naturalEncapsulateToCompressed(dst: ByteArray, off: Int = 0, maxLen: Int = Int.SIZE_BYTES) =
    dst.encapsulateNaturalCompressed(this, off, maxLen)

@JvmOverloads
fun Short.naturalEncapsulateToCompressed(dst: ByteArray, off: Int = 0, maxLen: Int = Short.SIZE_BYTES) =
    dst.encapsulateNaturalCompressed(this, off, maxLen)

fun Byte.naturalEncapsulateToCompressed(dst: ByteArray, off: Int = 0) = dst.encapsulateNaturalCompressed(this, off)

@JvmOverloads
fun BigInteger.naturalEncapsulateToCompressed(dst: ByteBuffer, maxLen: Int = BIG_INTEGER_NATURAL_DEFAULT_SIZE) =
    dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun ULong.naturalEncapsulateToCompressed(dst: ByteBuffer, maxLen: Int = ULong.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun UInt.naturalEncapsulateToCompressed(dst: ByteBuffer, maxLen: Int = UInt.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun UShort.naturalEncapsulateToCompressed(dst: ByteBuffer, maxLen: Int = UShort.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

fun UByte.naturalEncapsulateToCompressed(dst: ByteBuffer) = dst.encapsulateNaturalCompressed(this)

@JvmOverloads
fun Long.naturalEncapsulateToCompressed(dst: ByteBuffer, maxLen: Int = Long.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun Int.naturalEncapsulateToCompressed(dst: ByteBuffer, maxLen: Int = Int.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun Short.naturalEncapsulateToCompressed(dst: ByteBuffer, maxLen: Int = Short.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

fun Byte.naturalEncapsulateToCompressed(dst: ByteBuffer) = dst.encapsulateNaturalCompressed(this)

@JvmOverloads
fun BigInteger.naturalEncapsulateToCompressed(dst: OutputStream, maxLen: Int = BIG_INTEGER_NATURAL_DEFAULT_SIZE) =
    dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun ULong.naturalEncapsulateToCompressed(dst: OutputStream, maxLen: Int = ULong.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun UInt.naturalEncapsulateToCompressed(dst: OutputStream, maxLen: Int = UInt.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun UShort.naturalEncapsulateToCompressed(dst: OutputStream, maxLen: Int = UShort.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

fun UByte.naturalEncapsulateToCompressed(dst: OutputStream) = dst.encapsulateNaturalCompressed(this)

@JvmOverloads
fun Long.naturalEncapsulateToCompressed(dst: OutputStream, maxLen: Int = Long.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun Int.naturalEncapsulateToCompressed(dst: OutputStream, maxLen: Int = Int.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

@JvmOverloads
fun Short.naturalEncapsulateToCompressed(dst: OutputStream, maxLen: Int = Short.SIZE_BYTES) = dst.encapsulateNaturalCompressed(this, maxLen)

fun Byte.naturalEncapsulateToCompressed(dst: OutputStream) = dst.encapsulateNaturalCompressed(this)

/**
 * Encapsulate an integral to bytes.
 *
 * @param maxLen size in bytes
 * @return the result bytes
 */
private inline fun encapsulateToCompressedLIS(
    maxLen: Int,
    isSmall: Boolean,
    toByte: () -> Byte,
    naturalToBytes: (Int) -> ByteArray,
): ByteArray {
    if (isSmall) return byteArrayOf(toByte())
    val src = naturalToBytes(maxLen)
    val off = src.findFirstNonZeroByte()
    val naturalSize = maxLen - off
    val dst = ByteArray(1 + naturalSize)
    dst[0] = (naturalSize + 0b1000_0000).toByte()
    System.arraycopy(src, off, dst, 1, naturalSize)
    return dst
}

@JvmOverloads
fun BigInteger.naturalEncapsulateToCompressed(maxLen: Int = BIG_INTEGER_NATURAL_DEFAULT_SIZE) = run {
    requireNonNeg()
    encapsulateToCompressedLIS(maxLen, this < BIG_INTEGER_128, ::toByte, ::naturalToBytesLimited)
}

@JvmOverloads
fun ULong.naturalEncapsulateToCompressed(maxLen: Int = ULong.SIZE_BYTES) =
    encapsulateToCompressedLIS(maxLen, this < 0b1000_0000UL, ::toByte, ::naturalToBytes)

@JvmOverloads
fun UInt.naturalEncapsulateToCompressed(maxLen: Int = UInt.SIZE_BYTES) =
    encapsulateToCompressedLIS(maxLen, this < 0b1000_0000U, ::toByte, ::naturalToBytes)

@JvmOverloads
fun UShort.naturalEncapsulateToCompressed(maxLen: Int = UShort.SIZE_BYTES) =
    encapsulateToCompressedLIS(maxLen, this < U_SHORT_128, ::toByte, ::naturalToBytes)

fun UByte.naturalEncapsulateToCompressed() = toByte().naturalEncapsulateToCompressed()

@JvmOverloads
fun Long.naturalEncapsulateToCompressed(maxLen: Int = Long.SIZE_BYTES) = toULong().naturalEncapsulateToCompressed(maxLen)

@JvmOverloads
fun Int.naturalEncapsulateToCompressed(maxLen: Int = Int.SIZE_BYTES) = toUInt().naturalEncapsulateToCompressed(maxLen)

@JvmOverloads
fun Short.naturalEncapsulateToCompressed(maxLen: Int = Short.SIZE_BYTES) = toUShort().naturalEncapsulateToCompressed(maxLen)

private const val BYTE_0: Byte = 0

fun Byte.naturalEncapsulateToCompressed() =
    if (this >= BYTE_0) byteArrayOf(this) else ByteArray(2).apply { encapsulateNaturalCompressed(this@naturalEncapsulateToCompressed) }

/**
 * Read a natural number from the byte buffer.
 *
 * @return the result integral
 */
private inline fun <reified T> bufferUnencapsulateLIS(fromByte: Byte.() -> T, readNatural: (Int) -> T, byte0: Byte) =
    if (byte0 >= 0) fromByte(byte0) else readNatural(byte0.toUByte().toInt() - 0b1000_0000)

private inline fun bufferUnencapsulateB(byte0: Byte, readNatural: (Int) -> Byte) =
    if (byte0 >= 0) byte0 else readNatural(byte0.toUByte().toInt() - 0b1000_000)

private inline fun <reified T> ByteBuffer.unencapsulateLIS(fromByte: Byte.() -> T, readNatural: (Int) -> T) =
    bufferUnencapsulateLIS(fromByte, readNatural, get())

private inline fun <reified T> InputStream.unencapsulateLIS(fromByte: Byte.() -> T, readNatural: (Int) -> T) =
    bufferUnencapsulateLIS(fromByte, readNatural, read().toByte())

fun ByteBuffer.unencapsulateBigInteger(): BigInteger = unencapsulateLIS({ BigInteger.valueOf(toLong()) }, ::getNaturalBigInteger)
fun ByteBuffer.unencapsulateULong() = unencapsulateLIS(Byte::toULong, ::getNaturalULong)
fun ByteBuffer.unencapsulateUInt() = unencapsulateLIS(Byte::toUInt, ::getNaturalUInt)
fun ByteBuffer.unencapsulateUShort() = unencapsulateLIS(Byte::toULong, ::getNaturalShort)
fun ByteBuffer.unencapsulateUByte() = unencapsulateByte().toUByte()
fun ByteBuffer.unencapsulateLong() = unencapsulateLIS(Byte::toLong, ::getNaturalLong)
fun ByteBuffer.unencapsulateInt() = unencapsulateLIS(Byte::toInt, ::getNaturalInt)
fun ByteBuffer.unencapsulateShort() = unencapsulateLIS(Byte::toShort, ::getNaturalShort)
fun ByteBuffer.unencapsulateByte() = bufferUnencapsulateB(get(), ::getNaturalByte)

fun InputStream.unencapsulateBigInteger(): BigInteger = unencapsulateLIS({ BigInteger.valueOf(toLong()) }, ::readNaturalBigInteger)
fun InputStream.unencapsulateULong() = unencapsulateLIS(Byte::toLong, ::readNaturalLong)
fun InputStream.unencapsulateUInt() = unencapsulateLIS(Byte::toInt, ::readNaturalInt)
fun InputStream.unencapsulateUShort() = unencapsulateLIS(Byte::toShort, ::readNaturalShort)
fun InputStream.unencapsulateUByte() = unencapsulateByte().toUByte()
fun InputStream.unencapsulateLong() = unencapsulateLIS(Byte::toLong, ::readNaturalLong)
fun InputStream.unencapsulateInt() = unencapsulateLIS(Byte::toInt, ::readNaturalInt)
fun InputStream.unencapsulateShort() = unencapsulateLIS(Byte::toShort, ::readNaturalShort)
fun InputStream.unencapsulateByte() = bufferUnencapsulateB(read().toByte(), ::readNaturalByte)
