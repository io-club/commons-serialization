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
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class NoLeadingZeroTest {

    @Test
    fun testMisc() {
        val bas = byteArrayOf(0, 1).asSlice()
        assertEquals(1, bas.arrayFindFirstNonZeroByte())
        assertContentEquals(byteArrayOf(1), bas.toNoLeadingZeroByteArray())

        println(65536.naturalToNoLeadingZeroByteArraySlice().contentToString())
    }

    @Test
    fun testBigInteger() = testTmpl(
        TEST_BIG_INTEGER,
        16,
        Getters(
            ByteArraySlice::toNaturalBigInteger, ByteBuffer::getNaturalBigInteger, InputStream::readNaturalBigInteger
        ),
        AutoPutters(
            ByteArraySlice::putNaturalNoLeadingZero,
            BigInteger::naturalToNoLeadingZero,
            BigInteger::naturalToNoLeadingZeroByteArraySlice,
            BigInteger::naturalToNoLeadingZeroByteArray,
            ByteBuffer::putNaturalNoLeadingZero,
            OutputStream::writeNaturalNoLeadingZero,
        ),
    )

    @Test
    fun testLong() = testTmpl(
        TEST_LONG,
        Long.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalLong, ByteBuffer::getNaturalLong, InputStream::readNaturalLong),
        AutoPutters(
            ByteArraySlice::putNaturalNoLeadingZero,
            Long::naturalToNoLeadingZero,
            Long::naturalToNoLeadingZeroByteArraySlice,
            Long::naturalToNoLeadingZeroByteArray,
            ByteBuffer::putNaturalNoLeadingZero,
            OutputStream::writeNaturalNoLeadingZero,
        ),
    )

    @Test
    fun testInt() = testTmpl(
        TEST_INT,
        Int.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalInt, ByteBuffer::getNaturalInt, InputStream::readNaturalInt),
        AutoPutters(
            ByteArraySlice::putNaturalNoLeadingZero,
            Int::naturalToNoLeadingZero,
            Int::naturalToNoLeadingZeroByteArraySlice,
            Int::naturalToNoLeadingZeroByteArray,
            ByteBuffer::putNaturalNoLeadingZero,
            OutputStream::writeNaturalNoLeadingZero,
        ),
    )

    @Test
    fun testShort() = testTmpl(
        TEST_SHORT,
        Short.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalShort, ByteBuffer::getNaturalShort, InputStream::readNaturalShort),
        AutoPutters(
            ByteArraySlice::putNaturalNoLeadingZero,
            Short::naturalToNoLeadingZero,
            Short::naturalToNoLeadingZeroByteArraySlice,
            Short::naturalToNoLeadingZeroByteArray,
            ByteBuffer::putNaturalNoLeadingZero,
            OutputStream::writeNaturalNoLeadingZero,
        ),
    )

    companion object {

        private val TEST_BIG_INTEGER = BigInteger.ZERO
        private const val TEST_LONG = 0L
        private const val TEST_INT = 0
        private const val TEST_SHORT: Short = 0
    }
}

internal fun <T> testTmpl(
    value: T,
    cap: Int,
    getters: Getters<T>,
    putters: AutoPutters<T>,
) {
    with(getters) {
        with(putters) {
            listOf(
                ByteArray(cap).asSlice().apply { basPutN(value) }.basToN(),
                ByteArray(cap).asSlice().also { value.nTo(it) }.basToN(),
                value.nToBas().basToN(),
                value.nToBArr().asSlice().basToN(),
                ByteBuffer.allocate(cap).run {
                    val len = bufPutN(value)
                    flip()
                    bufGetN(len)
                },
                {
                    val o = ByteArrayOutputStream(cap)
                    val i = ByteArrayInputStream(o.toByteArray())
                    val len = o.sWriteN(value)
                    i.sReadN(len)
                }(),
            ).forEach { assertEquals(value, it) }
        }
    }
}

internal data class AutoPutters<T>(
    val basPutN: ByteArraySlice.(T) -> Int,
    val nTo: T.(ByteArraySlice) -> Int,
    val nToBas: T.() -> ByteArraySlice,
    val nToBArr: T.() -> ByteArray,
    val bufPutN: ByteBuffer.(T) -> Int,
    val sWriteN: OutputStream.(T) -> Int,
)
