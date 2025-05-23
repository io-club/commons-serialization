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

import fyi.ioclub.commons.datamodel.array.slice.ByteArraySlice
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

// Reading operations

fun ByteArraySlice.toNaturalULong(): ULong = toNaturalLong().toULong()
fun ByteArraySlice.toNaturalUInt(): UInt = toNaturalLong().toUInt()
fun ByteArraySlice.toNaturalUShort(): UShort = toNaturalLong().toUShort()
fun ByteArraySlice.toNaturalUByte(): UByte = toNaturalLong().toUByte()

fun ByteBuffer.getNaturalULong(length: Int = ULong.SIZE_BYTES) = getNaturalLong(length).toULong()
fun ByteBuffer.getNaturalUInt(length: Int = UInt.SIZE_BYTES) = getNaturalInt(length).toUInt()
fun ByteBuffer.getNaturalUShort(length: Int = UShort.SIZE_BYTES) = getNaturalShort(length).toUShort()
fun ByteBuffer.getNaturalUByte(length: Int = UByte.SIZE_BYTES) = getNaturalByte(length).toUByte()

fun InputStream.readNaturalULong(length: Int = ULong.SIZE_BYTES) = readNaturalLong(length).toULong()
fun InputStream.readNaturalUInt(length: Int = UInt.SIZE_BYTES) = readNaturalInt(length).toUInt()
fun InputStream.readNaturalUShort(length: Int = UShort.SIZE_BYTES) = readNaturalShort(length).toUShort()
fun InputStream.readNaturalUByte(length: Int = UByte.SIZE_BYTES) = readNaturalByte(length).toUByte()

// Writing operations

fun ByteArraySlice.putNatural(value: ULong) = putNatural(value.toLong())
fun ByteArraySlice.putNatural(value: UInt) = putNatural(value.toInt())
fun ByteArraySlice.putNatural(value: UShort) = putNatural(value.toShort())
fun ByteArraySlice.putNatural(value: UByte) = putNatural(value.toByte())

fun ByteBuffer.putNatural(value: ULong, length: Int = ULong.SIZE_BYTES) = putNatural(value.toLong(), length)
fun ByteBuffer.putNatural(value: UInt, length: Int = UInt.SIZE_BYTES) = putNatural(value.toInt(), length)
fun ByteBuffer.putNatural(value: UShort, length: Int = UShort.SIZE_BYTES) = putNatural(value.toShort(), length)
fun ByteBuffer.putNatural(value: UByte, length: Int = UByte.SIZE_BYTES) = putNatural(value.toByte(), length)

fun OutputStream.writeNatural(value: ULong, length: Int = ULong.SIZE_BYTES) = writeNatural(value.toLong(), length)
fun OutputStream.writeNatural(value: UInt, length: Int = UInt.SIZE_BYTES) = writeNatural(value.toInt(), length)
fun OutputStream.writeNatural(value: UShort, length: Int = UShort.SIZE_BYTES) = writeNatural(value.toShort(), length)
fun OutputStream.writeNatural(value: UByte, length: Int = UByte.SIZE_BYTES) = writeNatural(value.toByte(), length)

fun ULong.naturalTo(destination: ByteArraySlice) = toLong().naturalTo(destination)
fun UInt.naturalTo(destination: ByteArraySlice) = toInt().naturalTo(destination)
fun UShort.naturalTo(destination: ByteArraySlice) = toShort().naturalTo(destination)
fun UByte.naturalTo(destination: ByteArraySlice) = toByte().naturalTo(destination)

fun ULong.naturalToByteArray(length: Int = ULong.SIZE_BYTES) = toLong().naturalToByteArray(length)
fun UInt.naturalToByteArray(length: Int = UInt.SIZE_BYTES) = toInt().naturalToByteArray(length)
fun UShort.naturalToByteArray(length: Int = UShort.SIZE_BYTES) = toShort().naturalToByteArray(length)
fun UByte.naturalToByteArray(length: Int = UByte.SIZE_BYTES) = toByte().naturalToByteArray(length)

fun ULong.naturalToByteArraySlice(length: Int = ULong.SIZE_BYTES) = toLong().naturalToByteArraySlice(length)
fun UInt.naturalToByteArraySlice(length: Int = UInt.SIZE_BYTES) = toInt().naturalToByteArraySlice(length)
fun UShort.naturalToByteArraySlice(length: Int = UShort.SIZE_BYTES) = toShort().naturalToByteArraySlice(length)
fun UByte.naturalToByteArraySlice(length: Int = UByte.SIZE_BYTES) = toByte().naturalToByteArraySlice(length)
