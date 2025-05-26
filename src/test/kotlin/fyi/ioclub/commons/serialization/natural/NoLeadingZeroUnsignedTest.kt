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
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class NoLeadingZeroUnsignedTest {

    @Test
    fun testMisc() {
        val bas = byteArrayOf(0, 1).asSlice()
        assertEquals(1, bas.arrayFindFirstNonZeroByte())
        assertContentEquals(byteArrayOf(1), bas.toNoLeadingZeroByteArray())
    }

    @Test
    fun testULong() = testTmpl(
        TEST_U_LONG,
        ULong.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalULong, ByteBuffer::getNaturalULong, InputStream::readNaturalULong),
        AutoPutters(
            ByteArraySlice::putNaturalNoLeadingZero,
            ULong::naturalToNoLeadingZero,
            ULong::naturalToNoLeadingZeroByteArraySlice,
            ULong::naturalToNoLeadingZeroByteArray,
            ByteBuffer::putNaturalNoLeadingZero,
            OutputStream::writeNaturalNoLeadingZero,
        ),
    )

    @Test
    fun testUInt() = testTmpl(
        TEST_U_INT,
        UInt.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalUInt, ByteBuffer::getNaturalUInt, InputStream::readNaturalUInt),
        AutoPutters(
            ByteArraySlice::putNaturalNoLeadingZero,
            UInt::naturalToNoLeadingZero,
            UInt::naturalToNoLeadingZeroByteArraySlice,
            UInt::naturalToNoLeadingZeroByteArray,
            ByteBuffer::putNaturalNoLeadingZero,
            OutputStream::writeNaturalNoLeadingZero,
        ),
    )

    @Test
    fun testUShort() = testTmpl(
        TEST_U_SHORT,
        UShort.SIZE_BYTES,
        Getters(ByteArraySlice::toNaturalUShort, ByteBuffer::getNaturalUShort, InputStream::readNaturalUShort),
        AutoPutters(
            ByteArraySlice::putNaturalNoLeadingZero,
            UShort::naturalToNoLeadingZero,
            UShort::naturalToNoLeadingZeroByteArraySlice,
            UShort::naturalToNoLeadingZeroByteArray,
            ByteBuffer::putNaturalNoLeadingZero,
            OutputStream::writeNaturalNoLeadingZero,
        ),
    )

    companion object {

        private const val TEST_U_LONG = 0UL
        private const val TEST_U_INT = 0U
        private const val TEST_U_SHORT: UShort = 0U
    }
}
