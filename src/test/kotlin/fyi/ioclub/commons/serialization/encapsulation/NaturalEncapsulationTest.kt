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

import fyi.ioclub.commons.datamodel.array.concat.concat
import fyi.ioclub.commons.datamodel.array.slice.asSlice
import fyi.ioclub.commons.datamodel.container.Container
import fyi.ioclub.commons.serialization.encapsulation.naturalEncapsulateToByteArray
import fyi.ioclub.commons.serialization.natural.NaturalBigInteger
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import fyi.ioclub.commons.datamodel.array.slice.ByteArraySlice as Bas
import fyi.ioclub.commons.serialization.encapsulation.naturalEncapsulateTo as t
import fyi.ioclub.commons.serialization.encapsulation.naturalEncapsulateToByteArray as t
import fyi.ioclub.commons.serialization.encapsulation.naturalEncapsulateToByteArraySlice as t
import fyi.ioclub.commons.serialization.encapsulation.naturalEncapsulateToByteArraySliceList as t
import fyi.ioclub.commons.serialization.encapsulation.putNaturalEncapsulated as w
import fyi.ioclub.commons.serialization.encapsulation.writeNaturalEncapsulated as w
import java.io.InputStream as IS
import java.io.OutputStream as OS
import java.nio.ByteBuffer as BB

class NaturalEncapsulationTest {

    @Test
    fun testMisc() {
        assertEquals(1, (127.naturalEncapsulateToByteArray().size))
        assertEquals(2, (128.naturalEncapsulateToByteArray().size))

        println((127.naturalEncapsulateToByteArray()).map { it.toInt() and 0xFF })
        println((128.naturalEncapsulateToByteArray()).map { it.toInt() and 0xFF })
    }

    @Test
    fun testBigInteger() = with(TEST_BIG_INTEGER) {
        testUnit(
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            Container.Mutable<Bas>::getEncapsulatedNaturalBigInteger,
            BB::getEncapsulatedNaturalBigInteger,
            IS::readEncapsulatedNaturalBigInteger,
        )
    }

    @Test
    fun testULong() = with(TEST_U_LONG) {
        testUnit(
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            Container.Mutable<Bas>::getEncapsulatedNaturalULong,
            BB::getEncapsulatedNaturalULong,
            IS::readEncapsulatedNaturalULong,
        )
    }

    @Test
    fun testUInt() = with(TEST_U_INT) {
        testUnit(
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            Container.Mutable<Bas>::getEncapsulatedNaturalUInt,
            BB::getEncapsulatedNaturalUInt,
            IS::readEncapsulatedNaturalUInt,
        )
    }

    @Test
    fun testUShort() = with(TEST_U_SHORT) {
        testUnit(
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            Container.Mutable<Bas>::getEncapsulatedNaturalUShort,
            BB::getEncapsulatedNaturalUShort,
            IS::readEncapsulatedNaturalUShort,
        )
    }

    @Test
    fun testUByte() = with(TEST_U_BYTE) {
        testUnit(
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            Container.Mutable<Bas>::getEncapsulatedNaturalUByte,
            BB::getEncapsulatedNaturalUByte,
            IS::readEncapsulatedNaturalUByte,
        )
    }

    @Test
    fun testLong() = with(TEST_LONG) {
        testUnit(
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            Container.Mutable<Bas>::getEncapsulatedNaturalLong,
            BB::getEncapsulatedNaturalLong,
            IS::readEncapsulatedNaturalLong,
        )
    }

    @Test
    fun testInt() = with(TEST_INT) {
        testUnit(
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            Container.Mutable<Bas>::getEncapsulatedNaturalInt,
            BB::getEncapsulatedNaturalInt,
            IS::readEncapsulatedNaturalInt,
        )
    }

    @Test
    fun testShort() = with(TEST_SHORT) {
        testUnit(
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            Container.Mutable<Bas>::getEncapsulatedNaturalShort,
            BB::getEncapsulatedNaturalShort,
            IS::readEncapsulatedNaturalShort,
        )
    }

    @Test
    fun testByte() = with(TEST_BYTE) {
        testUnit(
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            ::t,
            Container.Mutable<Bas>::getEncapsulatedNaturalByte,
            BB::getEncapsulatedNaturalByte,
            IS::readEncapsulatedNaturalByte,
        )
    }

    companion object {

        private val TEST_BIG_INTEGER = BigInteger.valueOf(0)
        private const val TEST_U_LONG = 0UL
        private const val TEST_U_INT = 0u
        private const val TEST_U_SHORT: UShort = 0u
        private const val TEST_U_BYTE: UByte = 0u
        private const val TEST_LONG = 0L
        private const val TEST_INT = 0
        private const val TEST_SHORT: Short = 0
        private const val TEST_BYTE: Byte = 0
    }
}

private fun <T> T.testUnit(
    tBArr: () -> ByteArray,
    tBas: () -> Bas,
    tBasL: () -> List<Bas>,
    iTBas: (Bas) -> Unit,
    tBb: (BB) -> Unit,
    tOs: (OS) -> Unit,
    fromBas: Container.Mutable<Bas>.() -> T,
    fromBB: BB.() -> T,
    fromIS: IS.() -> T,
) {
    val bas = tBas()
    assertEquals(this, fromBas(Container.Mutable.of(bas)))
    val arr = tBArr()
    assertContentEquals(bas.toSlicedArray(), arr)
    assertContentEquals(arr, concat(tBasL()))
    basTest { iTBas(it); assertEquals(this, fromBas(Container.Mutable.of(it))) }
    bbTest { tBb(it); assertEquals(this, fromBB(it)) }
    ioSTest { i, o -> tOs(o); assertEquals(this, fromIS(i)) }
}

internal inline fun basTest(tester: (Bas) -> Unit) = ByteArray(SIZE).asSlice().let(tester)
internal inline fun bbTest(tester: (BB) -> Unit) = BB.allocate(SIZE).let(tester)
internal inline fun ioSTest(tester: (IS, OS) -> Unit) =
    ByteArrayOutputStream(SIZE).let { tester(ByteArrayInputStream(it.toByteArray()), it) }

internal const val SIZE = 16

internal fun NaturalBigInteger.t(destination: BB) = let(destination::w)
internal fun ULong.t(destination: BB) = let(destination::w)
internal fun UInt.t(destination: BB) = let(destination::w)
internal fun UShort.t(destination: BB) = let(destination::w)
internal fun UByte.t(destination: BB) = let(destination::w)
internal fun Long.t(destination: BB) = let(destination::w)
internal fun Int.t(destination: BB) = let(destination::w)
internal fun Short.t(destination: BB) = let(destination::w)
internal fun Byte.t(destination: BB) = let(destination::w)

internal fun NaturalBigInteger.t(destination: OS) = let(destination::w)
internal fun ULong.t(destination: OS) = let(destination::w)
internal fun UInt.t(destination: OS) = let(destination::w)
internal fun UShort.t(destination: OS) = let(destination::w)
internal fun UByte.t(destination: OS) = let(destination::w)
internal fun Long.t(destination: OS) = let(destination::w)
internal fun Int.t(destination: OS) = let(destination::w)
internal fun Short.t(destination: OS) = let(destination::w)
internal fun Byte.t(destination: OS) = let(destination::w)
