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

import fyi.ioclub.commons.datamodel.array.slice.ByteArraySlice
import fyi.ioclub.commons.datamodel.array.slice.asSlice
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertEquals

class NaturalTest {

    @Test
    fun testMisc() {
        assertEquals(0, 1.naturalToByteArraySlice(0).length)    // No exception
        assertEquals(0, 0.toBigInteger().naturalToByteArraySlice(AUTO).length)
        assertEquals(1, 255.toBigInteger().naturalToByteArraySlice(AUTO).length)

        println(65536.naturalToByteArraySlice().contentToString())
    }

    @Test
    fun testBigInteger() = testTmpl(
        TEST_BIG_INTEGER,
        16,
        Getters(
            ByteArraySlice::toNaturalBigInteger, ByteBuffer::getNaturalBigInteger, InputStream::readNaturalBigInteger
        ),
        Putters(
            ByteArraySlice::putNatural,
            BigInteger::naturalTo,
            BigInteger::naturalToByteArraySlice,
            BigInteger::naturalToByteArray,
            ByteBuffer::putNatural,
            OutputStream::writeNatural,
        ),
    )

    @Test
    fun testLong() = testTmpl(
        TEST_LONG,
        Long.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalLong, ByteBuffer::getNaturalLong, InputStream::readNaturalLong),
        Putters(
            ByteArraySlice::putNatural,
            Long::naturalTo,
            Long::naturalToByteArraySlice,
            Long::naturalToByteArray,
            ByteBuffer::putNatural,
            OutputStream::writeNatural,
        ),
    )

    @Test
    fun testInt() = testTmpl(
        TEST_INT,
        Int.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalInt, ByteBuffer::getNaturalInt, InputStream::readNaturalInt),
        Putters(
            ByteArraySlice::putNatural,
            Int::naturalTo,
            Int::naturalToByteArraySlice,
            Int::naturalToByteArray,
            ByteBuffer::putNatural,
            OutputStream::writeNatural,
        ),
    )

    @Test
    fun testShort() = testTmpl(
        TEST_SHORT,
        Short.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalShort, ByteBuffer::getNaturalShort, InputStream::readNaturalShort),
        Putters(
            ByteArraySlice::putNatural,
            Short::naturalTo,
            Short::naturalToByteArraySlice,
            Short::naturalToByteArray,
            ByteBuffer::putNatural,
            OutputStream::writeNatural,
        ),
    )

    @Test
    fun testByte() = testTmpl(
        TEST_BYTE,
        Byte.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalByte, ByteBuffer::getNaturalByte, InputStream::readNaturalByte),
        Putters(
            ByteArraySlice::putNatural,
            Byte::naturalTo,
            Byte::naturalToByteArraySlice,
            Byte::naturalToByteArray,
            ByteBuffer::putNatural,
            OutputStream::writeNatural,
        ),
    )

    companion object {

        private val TEST_BIG_INTEGER = BigInteger.ZERO
        private const val TEST_LONG = 0L
        private const val TEST_INT = 0
        private const val TEST_SHORT: Short = 0
        private const val TEST_BYTE: Byte = 0
    }
}

internal fun <T> testTmpl(
    value: T,
    len: Int,
    getters: Getters<T>,
    putters: Putters<T>,
) {
    with(getters) {
        with(putters) {
            listOf(
                ByteArray(len).asSlice().apply { basPutN(value) }.basToN(),
                ByteArray(len).asSlice().also { value.nTo(it) }.basToN(),
                value.nToBas(len).basToN(),
                value.nToBArr(len).asSlice().basToN(),
                ByteBuffer.allocate(len).run {
                    bufPutN(value, len)
                    flip()
                    bufGetN(len)
                },
                {
                    val o = ByteArrayOutputStream(len)
                    val i = ByteArrayInputStream(o.toByteArray())
                    o.sWriteN(value, len)
                    i.sReadN(len)
                }(),
            ).forEach { assertEquals(value, it) }
        }
    }
}

internal data class Getters<T>(
    val basToN: ByteArraySlice.() -> T,
    val bufGetN: ByteBuffer.(Int) -> T,
    val sReadN: InputStream.(Int) -> T,
)

internal data class Putters<T>(
    val basPutN: ByteArraySlice.(T) -> Int,
    val nTo: T.(ByteArraySlice) -> Int,
    val nToBas: T.(Int) -> ByteArraySlice,
    val nToBArr: T.(Int) -> ByteArray,
    val bufPutN: ByteBuffer.(T, Int) -> Int,
    val sWriteN: OutputStream.(T, Int) -> Int,
)
