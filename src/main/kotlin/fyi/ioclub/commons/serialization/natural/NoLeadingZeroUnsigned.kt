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
import java.io.OutputStream
import java.nio.ByteBuffer

fun ByteArraySlice.putNaturalNoLeadingZero(value: ULong) = putNaturalNoLeadingZero(value.toLong())
fun ByteArraySlice.putNaturalNoLeadingZero(value: UInt) = putNaturalNoLeadingZero(value.toInt())
fun ByteArraySlice.putNaturalNoLeadingZero(value: UShort) = putNaturalNoLeadingZero(value.toShort())

fun ULong.naturalToNoLeadingZero(destination: ByteArraySlice) = toLong().naturalToNoLeadingZero(destination)
fun UInt.naturalToNoLeadingZero(destination: ByteArraySlice) = toInt().naturalToNoLeadingZero(destination)
fun UShort.naturalToNoLeadingZero(destination: ByteArraySlice) = toShort().naturalToNoLeadingZero(destination)

fun ULong.naturalToNoLeadingZeroByteArray() = toLong().naturalToNoLeadingZeroByteArray()
fun UInt.naturalToNoLeadingZeroByteArray() = toInt().naturalToNoLeadingZeroByteArray()
fun UShort.naturalToNoLeadingZeroByteArray() = toShort().naturalToNoLeadingZeroByteArray()

fun ULong.naturalToNoLeadingZeroByteArraySlice() = toLong().naturalToNoLeadingZeroByteArraySlice()
fun UInt.naturalToNoLeadingZeroByteArraySlice() = toInt().naturalToNoLeadingZeroByteArraySlice()
fun UShort.naturalToNoLeadingZeroByteArraySlice() = toShort().naturalToNoLeadingZeroByteArraySlice()

fun ByteBuffer.putNaturalNoLeadingZero(value: ULong) = putNaturalNoLeadingZero(value.toLong())
fun ByteBuffer.putNaturalNoLeadingZero(value: UInt) = putNaturalNoLeadingZero(value.toInt())
fun ByteBuffer.putNaturalNoLeadingZero(value: UShort) = putNaturalNoLeadingZero(value.toShort())

fun OutputStream.writeNaturalNoLeadingZero(value: ULong) = writeNaturalNoLeadingZero(value.toLong())
fun OutputStream.writeNaturalNoLeadingZero(value: UInt) = writeNaturalNoLeadingZero(value.toInt())
fun OutputStream.writeNaturalNoLeadingZero(value: UShort) = writeNaturalNoLeadingZero(value.toShort())
