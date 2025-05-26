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
import fyi.ioclub.commons.datamodel.array.slice.ByteArraySlice
import fyi.ioclub.commons.datamodel.array.slice.asIterable
import fyi.ioclub.commons.datamodel.array.slice.asSlice
import fyi.ioclub.commons.datamodel.array.slice.asSliceTo
import fyi.ioclub.commons.datamodel.container.Container
import fyi.ioclub.commons.datamodel.container.getValue
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataEncapsulationTest {

    @Test
    fun testByteArraySlice() = with(ByteArray(32) { 0 }.asSlice()) {
        val l = putEncapsulated(data)
        val c = Container.Mutable.of(asSliceTo(l))
        assertContentEquals(arr, c.getEncapsulatedByteArray())
        val s2 by c
        assertTrue { s2.asIterable().all { it == 0.toByte() } }
        val s1 = this.asSliceTo(s2.offset)
        assertTrue { s1.asIterable().all { it != 0.toByte() } }

        val t = s1.toSlicedArray()
        val lT = data.encapsulateTo(this)
        assertContentEquals(t, this.asSliceTo(lT).toSlicedArray())

        assertContentEquals(data.encapsulateToByteArray(), concat(data.encapsulateToByteArraySliceList()))
        assertContentEquals(
            data.encapsulateToByteArray(),
            concat(arr.encapsulateToByteArrayList().map(ByteArray::asSlice))
        )

        println(data.encapsulateToByteArray().contentToString())
        println(data.encapsulateToByteArraySliceList().map(ByteArraySlice::contentToString))

        assertTrue { data contentEquals Container.Mutable.of(this).getEncapsulatedByteArraySlice() }
    }

    @Test
    fun testByteBuffer() = with(ByteBuffer.allocate(32)) {
        val i = putEncapsulated(data)
        rewind() // To make sure the encapsulation works: must not `flip` here

        assertContentEquals(arr, getEncapsulatedByteArray())
        assertEquals(i, position())
        rewind()

        val s = ByteArray(32).asSlice()
        val l = this.getEncapsulated(s)
        assertTrue { data contentEquals s.asSliceTo(l) }
        rewind()

        val s2 = this.getEncapsulatedByteArraySlice()
        assertTrue { data contentEquals s2 }
        assertEquals(this.array(), s2.array)
        rewind(); Unit
    }

    @Test
    fun testIOStream() = with(ByteArrayInputStream(ByteArrayOutputStream().apply {
        writeEncapsulated(data)
    }.toByteArray())) {
        mark(0)
        assertContentEquals(arr, readEncapsulatedByteArray())
        reset()

        val s = ByteArray(32).asSlice()
        mark(0)
        val l = this.readEncapsulated(s)
        reset()
        assertTrue { data contentEquals s.asSliceTo(l) }

        val s2 = this.readEncapsulatedByteArraySlice()
        assertTrue { data contentEquals s2 }
    }

    companion object {
        private val arr = ByteArray(4) { -1 }
        private val data = arr.asSlice()
    }
}
