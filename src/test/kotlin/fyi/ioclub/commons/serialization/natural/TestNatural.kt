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

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.nio.ByteBuffer

private val TEST_BIG_INTEGER = BigInteger.ZERO
private const val TEST_LONG = 0L
private const val TEST_INT = 0
private const val TEST_SHORT: Short = 0
private const val TEST_BYTE: Byte = 0

//internal data class Getters<T>(
//    val arrGet: ByteArray.() -> T,
//    val bufGet: ByteBuffer.() -> T,
//    val sRead: InputStream.() -> T,
//)
//
//internal data class Putters<T>(
//    val arrPutNaturalBytes: ByteArray.(T) -> Unit,
//    val naturalBytesTo: T.(ByteArray) -> Unit,
//    val naturalBytes: T.() -> ByteArray,
//    val bufPut: ByteBuffer.(T) -> Unit,
//    val sWrite: OutputStream.(T, Int) -> Unit,
//)
//
//internal inline fun <reified T> test(
//    value: T,
//    len: Int,
//    getters: Getters<T>,
//    putters: Putters<T>,
//) {
//    with(getters) {
//        with(putters) {
//            arrayOf(
//                ByteArray(len).apply { arrPutNaturalBytes(value) }.arrGet(),
//                ByteArray(len).also { value.naturalBytesTo(it) }.arrGet(),
//                naturalBytes(value).arrGet(),
//                ByteBuffer.allocate(len).run {
//                    bufPut(value)
//                    flip()
//                    bufGet()
//                },
//                {
//                    val o = ByteArrayOutputStream(len)
//                    val i = ByteArrayInputStream(o.toByteArray())
//                    o.sWrite(value, len)
//                    i.sRead()
//                }(),
//            ).forEach { assert(it == value) }
//        }
//    }
//}
//
//fun testNatural() {
//    test(
//        TEST_BIG_INTEGER,
//        16,
//        Getters(ByteArray::getNaturalBigInteger, ByteBuffer::getNaturalBigInteger, InputStream::readNaturalBigInteger),
//        Putters(
//            ByteArray::putNatural,
//            BigInteger::naturalToByteArray,
//            BigInteger::naturalToByteArray,
//            ByteBuffer::putNatural,
//            OutputStream::writeNatural,
//        ),
//    )
//    test(
//        TEST_LONG,
//        Long.SIZE_BYTES,
//        Getters(ByteArray::getNaturalLong, ByteBuffer::getNaturalLong, InputStream::readNaturalLong),
//        Putters(
//            ByteArray::putNatural,
//            Long::naturalToByteArray,
//            Long::naturalToByteArray,
//            ByteBuffer::putNatural,
//            OutputStream::writeNatural,
//        ),
//    )
//    test(
//        TEST_INT,
//        Int.SIZE_BYTES,
//        Getters(ByteArray::getNaturalInt, ByteBuffer::getNaturalInt, InputStream::readNaturalInt),
//        Putters(
//            ByteArray::putNatural,
//            Int::naturalToByteArray,
//            Int::naturalToByteArray,
//            ByteBuffer::putNatural,
//            OutputStream::writeNatural,
//        ),
//    )
//    test(
//        TEST_SHORT,
//        Short.SIZE_BYTES,
//        Getters(ByteArray::getNaturalShort, ByteBuffer::getNaturalShort, InputStream::readNaturalShort),
//        Putters(
//            ByteArray::putNatural,
//            Short::naturalToByteArray,
//            Short::naturalToByteArray,
//            ByteBuffer::putNatural,
//            OutputStream::writeNatural,
//        ),
//    )
//    test(
//        TEST_BYTE,
//        Byte.SIZE_BYTES,
//        Getters(ByteArray::getNaturalByte, ByteBuffer::getNaturalByte, InputStream::readNaturalByte),
//        Putters(
//            ByteArray::putNatural,
//            Byte::naturalToByteArray,
//            Byte::naturalToByteArray,
//            ByteBuffer::putNatural,
//            OutputStream::writeNatural,
//        ),
//    )
//}
//
//fun main() = testNatural()