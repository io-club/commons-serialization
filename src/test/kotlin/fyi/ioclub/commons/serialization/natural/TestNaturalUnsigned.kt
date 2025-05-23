/*
 * Copyright © 2025 IO Club
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fyi.ioclub.commons.serialization.natural

import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

private const val TEST_U_LONG = 0UL
private const val TEST_U_INT = 0u
private const val TEST_U_SHORT: UShort = 0u
private const val TEST_U_BYTE: UByte = 0u

//fun testNaturalUnsigned() {
//    test(
//        TEST_U_LONG,
//        ULong.SIZE_BYTES,
//        Getters(ByteArray::getNaturalULong, ByteBuffer::getNaturalULong, InputStream::readNaturalULong),
//        Putters(
//            ByteArray::putNatural,
//            ULong::naturalToByteArray,
//            ULong::naturalToByteArray,
//            ByteBuffer::putNatural,
//            OutputStream::writeNatural,
//        ),
//    )
//    test(
//        TEST_U_INT,
//        UInt.SIZE_BYTES,
//        Getters(ByteArray::getNaturalUInt, ByteBuffer::getNaturalUInt, InputStream::readNaturalUInt),
//        Putters(
//            ByteArray::putNatural,
//            UInt::naturalToByteArray,
//            UInt::naturalToByteArray,
//            ByteBuffer::putNatural,
//            OutputStream::writeNatural,
//        ),
//    )
//    test(
//        TEST_U_SHORT,
//        UShort.SIZE_BYTES,
//        Getters(ByteArray::getNaturalUShort, ByteBuffer::getNaturalUShort, InputStream::readNaturalUShort),
//        Putters(
//            ByteArray::putNatural,
//            UShort::naturalToByteArray,
//            UShort::naturalToByteArray,
//            ByteBuffer::putNatural,
//            OutputStream::writeNatural,
//        ),
//    )
//    test(
//        TEST_U_BYTE,
//        UByte.SIZE_BYTES,
//        Getters(ByteArray::getNaturalUByte, ByteBuffer::getNaturalUByte, InputStream::readNaturalUByte),
//        Putters(
//            ByteArray::putNatural,
//            UByte::naturalToByteArray,
//            UByte::naturalToByteArray,
//            ByteBuffer::putNatural,
//            OutputStream::writeNatural,
//        ),
//    )
//}
//
//fun main() = testNaturalUnsigned()
