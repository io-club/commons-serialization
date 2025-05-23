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

//import fyi.ioclub.commons.serialization.encapsulation.naturalEncapsulateToCompressed
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.math.BigInteger
//import fyi.ioclub.commons.serialization.encapsulation.naturalEncapsulateToCompressed as ec
import java.io.InputStream as IS
import java.nio.ByteBuffer as BB

//private val TEST_BIG_INTEGER = BigInteger.valueOf(0)
//private const val TEST_U_LONG = 0UL
//private const val TEST_U_INT = 0u
//private const val TEST_U_SHORT: UShort = 0u
//private const val TEST_U_BYTE: UByte = 0u
//private const val TEST_LONG = 0L
//private const val TEST_INT = 0
//private const val TEST_SHORT: Short = 0
//private const val TEST_BYTE: Byte = 0
//
//const val SIZE = 16
//
//internal inline fun byteArrayTest(tester: (ByteArray) -> Unit) = ByteArray(SIZE).let(tester)
//internal inline fun byteBufferTest(tester: (BB) -> Unit) = BB.allocate(SIZE).let(tester)
//internal inline fun ioStreamTest(tester: (IS, OutputStream) -> Unit) =
//    ByteArrayOutputStream(SIZE).let { tester(ByteArrayInputStream(it.toByteArray()), it) }
//
//private fun <T> T.testUnit(
//    toByteArray: (ByteArray) -> Unit,
//    toBB: (BB) -> Unit,
//    toOutputStream: (OutputStream) -> Unit,
//    fromBB: (BB) -> T,
//    fromIS: (IS) -> T,
//) {
//    byteArrayTest { toByteArray(it) }
//    byteBufferTest { toBB(it); assert(fromBB(it) == this) }
//    ioStreamTest { i, o -> toOutputStream(o); assert(fromIS(i) == this) }
//}
//
//fun testNaturalEncapsulating() {
//    TEST_BIG_INTEGER.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateBigInteger, IS::unencapsulateBigInteger) }
//    TEST_U_LONG.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateULong, IS::unencapsulateULong) }
//    TEST_U_INT.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateUInt, IS::unencapsulateUInt) }
//    TEST_U_SHORT.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateUShort, IS::unencapsulateUShort) }
//    TEST_U_BYTE.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateUByte, IS::unencapsulateUByte) }
//    TEST_LONG.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateLong, IS::unencapsulateLong) }
//    TEST_INT.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateInt, IS::unencapsulateInt) }
//    TEST_SHORT.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateShort, IS::unencapsulateShort) }
//    TEST_BYTE.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateByte, IS::unencapsulateByte) }
//    (1).toByte().naturalEncapsulateToCompressed()
//}
//
//fun main() = testNaturalEncapsulating()