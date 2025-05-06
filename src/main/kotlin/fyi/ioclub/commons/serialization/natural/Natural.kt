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

import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.util.*


private const val BIG_INTEGER_POSITIVE = 1

@JvmOverloads
fun ByteArray.getNaturalBigInteger(len: Int = size) = getNaturalBigInteger(0, len)
fun ByteArray.getNaturalBigInteger(off: Int, len: Int) = BigInteger(BIG_INTEGER_POSITIVE, this, off, len)

private inline fun <reified T> ByteArray.getNaturalBasic(
    off: Int,
    len: Int,
    initVal: T,
    plusAndShl: (T, UByte, Int) -> T
): T {
    var j = (if (off >= 0) off else size + off).also { Objects.checkFromIndexSize(it, len, size) } + len
    var value = initVal
    for (i in 0..<len * Byte.SIZE_BITS step Byte.SIZE_BITS) value = plusAndShl(value, get(--j).toUByte(), i)
    return value
}

@JvmOverloads
fun ByteArray.getNaturalLong(len: Int = size) = getNaturalLong(0, len)
fun ByteArray.getNaturalLong(off: Int, len: Int) =
    getNaturalBasic(off, len, 0L) { value, ub, shrBits -> value + ub.toLong() shl shrBits }

@JvmOverloads
fun ByteArray.getNaturalInt(len: Int = size) = getNaturalInt(0, len)
fun ByteArray.getNaturalInt(off: Int, len: Int) =
    getNaturalBasic(off, len, 0) { value, ub, shrBits -> value + ub.toInt() shl shrBits }

@JvmOverloads
fun ByteArray.getNaturalShort(len: Int = 0) = getNaturalShort(0, len)
fun ByteArray.getNaturalShort(off: Int, len: Int) =
    getNaturalBasic<Short>(off, len, 0) { value, ub, shrBits -> (value + ub.toInt() shl shrBits).toShort() }

@JvmOverloads
fun ByteArray.getNaturalByte(len: Int = 0) = getNaturalByte(0, len)
fun ByteArray.getNaturalByte(off: Int, len: Int) =
    getNaturalBasic<Byte>(off, len, 0) { value, ub, shrBits -> (value + ub.toInt() shl shrBits).toByte() }

private inline fun <reified T> ByteBuffer.getNaturalTemplate(
    len: Int,
    byteArrayToNatural: ByteArray.(Int, Int) -> T
): T {
    val pos = position()
    if (len > limit() - pos) throw BufferUnderflowException()
    position(pos + len)
    return byteArrayToNatural(array(), pos, len)
}

@JvmOverloads
fun ByteBuffer.getNaturalBigInteger(len: Int = remaining()) = getNaturalTemplate(len, ByteArray::getNaturalBigInteger)

@JvmOverloads
fun ByteBuffer.getNaturalLong(len: Int = remaining()) = getNaturalTemplate(len, ByteArray::getNaturalLong)

@JvmOverloads
fun ByteBuffer.getNaturalInt(len: Int = remaining()) = getNaturalTemplate(len, ByteArray::getNaturalInt)

@JvmOverloads
fun ByteBuffer.getNaturalShort(len: Int = remaining()) = getNaturalTemplate(len, ByteArray::getNaturalShort)

@JvmOverloads
fun ByteBuffer.getNaturalByte(len: Int = remaining()) = getNaturalTemplate(len, ByteArray::getNaturalByte)

private inline fun <reified T> InputStream.readNaturalTemplate(len: Int, byteArrayToNatural: (ByteArray) -> T) =
    byteArrayToNatural(ByteArray(len).also { read(it) })

@JvmOverloads
fun InputStream.readNaturalBigInteger(len: Int = available()) =
    readNaturalTemplate(len, ByteArray::getNaturalBigInteger)

@JvmOverloads
fun InputStream.readNaturalLong(len: Int = available()) = readNaturalTemplate(len, ByteArray::getNaturalLong)

@JvmOverloads
fun InputStream.readNaturalInt(len: Int = available()) = readNaturalTemplate(len, ByteArray::getNaturalInt)

@JvmOverloads
fun InputStream.readNaturalShort(len: Int = available()) = readNaturalTemplate(len, ByteArray::getNaturalShort)

@JvmOverloads
fun InputStream.readNaturalByte(len: Int = available()) = readNaturalTemplate(len, ByteArray::getNaturalByte)

// To put non-negative integers

internal const val ZERO_BYTE: Byte = 0

@JvmOverloads
fun ByteArray.putNatural(value: BigInteger, len: Int = size) = putNatural(value, 0, len)
fun ByteArray.putNatural(value: BigInteger, off: Int, len: Int) {
    val src = value.toByteArray()
    val srcOff = if (src[0] == ZERO_BYTE) 0 else 1
    val srcLen = src.size - srcOff
    System.arraycopy(src, srcOff, this, off + len - srcLen, srcLen)
}

private inline fun <reified T> ByteArray.putNaturalBasic(value: T, off: Int, len: Int, shrToByte: (T, Int) -> Byte) {
    var j = off + len
    for (i in 0..<len * 8 step Byte.SIZE_BITS) this[--j] = shrToByte(value, i)
}

@JvmOverloads
fun ByteArray.putNatural(value: Long, len: Int = size) = putNatural(value, 0, len)
fun ByteArray.putNatural(value: Long, off: Int, len: Int) =
    putNaturalBasic(value, off, len) { value, shrBits -> (value ushr shrBits).toByte() }

@JvmOverloads
fun ByteArray.putNatural(value: Int, len: Int = size) = putNatural(value, 0, len)
fun ByteArray.putNatural(value: Int, off: Int, len: Int) =
    putNaturalBasic(value, off, len) { value, shrBits -> (value ushr shrBits).toByte() }

@JvmOverloads
fun ByteArray.putNatural(value: Short, len: Int = size) = putNatural(value, 0, len)
fun ByteArray.putNatural(value: Short, off: Int, len: Int) = putNatural(value.toInt(), off, len)

@JvmOverloads
fun ByteArray.putNatural(value: Byte, len: Int = size) = putNatural(value, 0, len)
fun ByteArray.putNatural(value: Byte, off: Int, len: Int) = { this[off + len - 1] = value }

private inline fun <reified T> writeNaturalTemplate(
    value: T,
    len: Int,
    naturalPutter: ByteArray.(T) -> Unit,
    writer: (ByteArray) -> Unit,
) = writer(ByteArray(len).also { it.naturalPutter(value) })

private inline fun <reified T> ByteBuffer.putNaturalTemplate(
    value: T,
    len: Int,
    naturalPutter: (ByteArray, T) -> Unit
) =
    writeNaturalTemplate(value, len, naturalPutter, ::put)

@JvmOverloads
fun ByteBuffer.putNatural(value: BigInteger, len: Int = remaining()) =
    putNaturalTemplate(value, len, ByteArray::putNatural)

@JvmOverloads
fun ByteBuffer.putNatural(value: Long, len: Int = remaining()) = putNaturalTemplate(value, len, ByteArray::putNatural)

@JvmOverloads
fun ByteBuffer.putNatural(value: Int, len: Int = remaining()) = putNaturalTemplate(value, len, ByteArray::putNatural)

@JvmOverloads
fun ByteBuffer.putNatural(value: Short, len: Int = remaining()) = putNaturalTemplate(value, len, ByteArray::putNatural)

@JvmOverloads
fun ByteBuffer.putNatural(value: Byte, len: Int = remaining()) = putNaturalTemplate(value, len, ByteArray::putNatural)

private inline fun <reified T> OutputStream.putNaturalTemplate(
    value: T,
    len: Int,
    naturalPutter: ByteArray.(T) -> Unit
) =
    writeNaturalTemplate(value, len, naturalPutter, ::write)

fun OutputStream.writeNatural(value: BigInteger, len: Int) = putNaturalTemplate(value, len, ByteArray::putNatural)
fun OutputStream.writeNatural(value: Long, len: Int) = putNaturalTemplate(value, len, ByteArray::putNatural)
fun OutputStream.writeNatural(value: Int, len: Int) = putNaturalTemplate(value, len, ByteArray::putNatural)
fun OutputStream.writeNatural(value: Short, len: Int) = putNaturalTemplate(value, len, ByteArray::putNatural)
fun OutputStream.writeNatural(value: Byte, len: Int) = putNaturalTemplate(value, len, ByteArray::putNatural)

@JvmOverloads
fun BigInteger.naturalToBytes(dst: ByteArray, len: Int = dst.size) = naturalToBytes(dst, 0, len)
fun BigInteger.naturalToBytes(dst: ByteArray, off: Int, len: Int) = dst.putNatural(this, off, len)

@JvmOverloads
fun Long.naturalToBytes(dst: ByteArray, len: Int = dst.size) = naturalToBytes(dst, 0, len)
fun Long.naturalToBytes(dst: ByteArray, off: Int, len: Int) = dst.putNatural(this, off, len)

@JvmOverloads
fun Int.naturalToBytes(dst: ByteArray, len: Int = 0) = naturalToBytes(dst, 0, len)
fun Int.naturalToBytes(dst: ByteArray, off: Int, len: Int) = dst.putNatural(this, off, len)

@JvmOverloads
fun Short.naturalToBytes(dst: ByteArray, len: Int = dst.size) = naturalToBytes(dst, 0, len)
fun Short.naturalToBytes(dst: ByteArray, off: Int, len: Int) = dst.putNatural(this, off, len)

@JvmOverloads
fun Byte.naturalToBytes(dst: ByteArray, len: Int = dst.size) = naturalToBytes(dst, 0, len)
fun Byte.naturalToBytes(dst: ByteArray, off: Int, len: Int) = dst.putNatural(this, off, len)

const val BIG_INTEGER_NATURAL_DEFAULT_SIZE = -1

fun BigInteger.naturalToBytes(len: Int = BIG_INTEGER_NATURAL_DEFAULT_SIZE): ByteArray =
    if (len == BIG_INTEGER_NATURAL_DEFAULT_SIZE) {
        val src = toByteArray()
        if (src[0] != ZERO_BYTE) src
        else {
            val len = src.size - 1
            ByteArray(len).also { System.arraycopy(src, 1, it, 0, len) }
        }
    } else ByteArray(len).also { naturalToBytes(it, len) }

private inline fun naturalToBytesBasic(len: Int, toBytes: (ByteArray) -> Unit) = ByteArray(len).also { toBytes(it) }

@JvmOverloads
fun Long.naturalToBytes(len: Int = Long.SIZE_BYTES) = naturalToBytesBasic(len, ::naturalToBytes)

@JvmOverloads
fun Int.naturalToBytes(len: Int = Int.SIZE_BYTES) = naturalToBytesBasic(len, ::naturalToBytes)

@JvmOverloads
fun Short.naturalToBytes(len: Int = Short.SIZE_BYTES) = naturalToBytesBasic(len, ::naturalToBytes)

@JvmOverloads
fun Byte.naturalToBytes(len: Int = Byte.SIZE_BYTES) = naturalToBytesBasic(len, ::naturalToBytes)
