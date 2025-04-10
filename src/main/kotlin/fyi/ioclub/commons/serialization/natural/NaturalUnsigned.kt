/*
 * Copyright © 2025 IO Club
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* For unsigned integrals */

package fyi.ioclub.commons.serialization.natural

import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

// To get

@JvmOverloads
fun ByteArray.getNaturalULong(len: Int = size) = getNaturalULong(0, len)
fun ByteArray.getNaturalULong(off: Int, len: Int) = getNaturalLong(off, len).toULong()

@JvmOverloads
fun ByteArray.getNaturalUInt(len: Int = size) = getNaturalUInt(0, len)
fun ByteArray.getNaturalUInt(off: Int, len: Int) = getNaturalInt(off, len).toUInt()

@JvmOverloads
fun ByteArray.getNaturalUShort(len: Int = size) = getNaturalUShort(0, len)
fun ByteArray.getNaturalUShort(off: Int, len: Int) = getNaturalShort(off, len).toUShort()

@JvmOverloads
fun ByteArray.getNaturalUByte(len: Int = size) = getNaturalUByte(0, len)
fun ByteArray.getNaturalUByte(off: Int, len: Int) = getNaturalByte(off, len).toUByte()

@JvmOverloads
fun ByteBuffer.getNaturalULong(len: Int = remaining()) = getNaturalLong(len).toULong()

@JvmOverloads
fun ByteBuffer.getNaturalUInt(len: Int = remaining()) = getNaturalInt(len).toUInt()

@JvmOverloads
fun ByteBuffer.getNaturalUShort(len: Int = remaining()) = getNaturalShort(len).toUShort()

@JvmOverloads
fun ByteBuffer.getNaturalUByte(len: Int = remaining()) = getNaturalByte(len).toUByte()

@JvmOverloads
fun InputStream.readNaturalULong(len: Int = available()) = readNaturalLong(len).toULong()

@JvmOverloads
fun InputStream.readNaturalUInt(len: Int = available()) = readNaturalInt(len).toUInt()

@JvmOverloads
fun InputStream.readNaturalUShort(len: Int = available()) = readNaturalShort(len).toUShort()

@JvmOverloads
fun InputStream.readNaturalUByte(len: Int = available()) = readNaturalByte(len).toUByte()

// To put

@JvmOverloads
fun ByteArray.putNatural(value: ULong, len: Int = size) = putNatural(value, 0, len)
fun ByteArray.putNatural(value: ULong, off: Int, len: Int) = putNatural(value.toLong(), off, len)

@JvmOverloads
fun ByteArray.putNatural(value: UInt, len: Int = size) = putNatural(value, 0, len)
fun ByteArray.putNatural(value: UInt, off: Int, len: Int) = putNatural(value.toInt(), off, len)

@JvmOverloads
fun ByteArray.putNatural(value: UShort, len: Int = size) = putNatural(value, 0, len)
fun ByteArray.putNatural(value: UShort, off: Int, len: Int) = putNatural(value.toShort(), off, len)

@JvmOverloads
fun ByteArray.putNatural(value: UByte, len: Int = size) = putNatural(value, 0, len)
fun ByteArray.putNatural(value: UByte, off: Int, len: Int) = putNatural(value.toByte(), off, len)

@JvmOverloads
fun ByteBuffer.putNatural(value: ULong, len: Int = remaining()) = putNatural(value.toLong(), len)

@JvmOverloads
fun ByteBuffer.putNatural(value: UInt, len: Int = remaining()) = putNatural(value.toInt(), len)

@JvmOverloads
fun ByteBuffer.putNatural(value: UShort, len: Int = remaining()) = putNatural(value.toShort(), len)

@JvmOverloads
fun ByteBuffer.putNatural(value: UByte, len: Int = remaining()) = putNatural(value.toByte(), len)

fun OutputStream.writeNatural(value: ULong, len: Int) = writeNatural(value.toLong(), len)
fun OutputStream.writeNatural(value: UInt, len: Int) = writeNatural(value.toInt(), len)
fun OutputStream.writeNatural(value: UShort, len: Int) = writeNatural(value.toShort(), len)
fun OutputStream.writeNatural(value: UByte, len: Int) = writeNatural(value.toByte(), len)

@JvmOverloads
fun ULong.naturalToBytes(dst: ByteArray, len: Int = dst.size) = naturalToBytes(dst, 0, len)
fun ULong.naturalToBytes(dst: ByteArray, off: Int, len: Int) = toLong().naturalToBytes(dst, off, len)

@JvmOverloads
fun UInt.naturalToBytes(dst: ByteArray, len: Int = dst.size) = naturalToBytes(dst, 0, len)
fun UInt.naturalToBytes(dst: ByteArray, off: Int, len: Int) = toInt().naturalToBytes(dst, off, len)

@JvmOverloads
fun UShort.naturalToBytes(dst: ByteArray, len: Int = dst.size) = naturalToBytes(dst, 0, len)
fun UShort.naturalToBytes(dst: ByteArray, off: Int, len: Int) = toShort().naturalToBytes(dst, off, len)

@JvmOverloads
fun UByte.naturalToBytes(dst: ByteArray, len: Int = dst.size) = naturalToBytes(dst, 0, len)
fun UByte.naturalToBytes(dst: ByteArray, off: Int, len: Int) = toByte().naturalToBytes(dst, off, len)

@JvmOverloads
fun ULong.naturalToBytes(len: Int = ULong.SIZE_BYTES) = toLong().naturalToBytes(len)

@JvmOverloads
fun UInt.naturalToBytes(len: Int = UInt.SIZE_BYTES) = toInt().naturalToBytes(len)

@JvmOverloads
fun UShort.naturalToBytes(len: Int = UShort.SIZE_BYTES) = toShort().naturalToBytes(len)

@JvmOverloads
fun UByte.naturalToBytes(len: Int = UByte.SIZE_BYTES) = toByte().naturalToBytes(len)

//val ULong.naturalBytes get() = naturalToBytes()
//val UInt.naturalBytes get() = naturalToBytes()
//val UShort.naturalBytes get() = naturalToBytes()
//val UByte.naturalBytes get() = naturalToBytes()
