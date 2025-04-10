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

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: ULong, off: Int = 0) = putNoLeadingZero(value.toLong(), off)

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: UInt, off: Int = 0) = putNoLeadingZero(value.toInt(), off)

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: UShort, off: Int = 0) = putNoLeadingZero(value.toShort(), off)

@JvmOverloads
fun ULong.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = toLong().toNoLeadingZeroBytes(dst, off)

@JvmOverloads
fun UInt.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = toInt().toNoLeadingZeroBytes(dst, off)

@JvmOverloads
fun UShort.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = toShort().toNoLeadingZeroBytes(dst, off)
fun ULong.toNoLeadingZeroBytes() = toLong().toNoLeadingZeroBytes()
fun UInt.toNoLeadingZeroBytes() = toInt().toNoLeadingZeroBytes()
fun UShort.toNoLeadingZeroBytes() = toShort().toNoLeadingZeroBytes()
val ULong.noLeadingZeroBytes get() = toNoLeadingZeroBytes()
val UInt.noLeadingZeroBytes get() = toNoLeadingZeroBytes()
val UShort.noLeadingZeroBytes get() = toNoLeadingZeroBytes()
