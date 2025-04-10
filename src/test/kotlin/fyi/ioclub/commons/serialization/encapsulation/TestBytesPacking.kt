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

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

fun testBytesEncapsulating() {
    ByteArray(4).let {
        ByteArray(32).apply { encapsulate(0, it);encapsulate(it) }
        ByteBuffer.allocate(32).apply { encapsulate(it) }.unencapsulate(it)
        ByteArrayInputStream(ByteArrayOutputStream().apply { encapsulate(it) }.toByteArray()).unencapsulate(it)
    }
    with(ByteArray(4)) {
        ByteArray(32).also { encapsulateToBytes(it) }
        ByteBuffer.allocate(32).also { encapsulateToBytes(it) }.unencapsulate()
        ByteArrayInputStream(ByteArrayOutputStream().also { encapsulateToBytes(it) }.toByteArray()).unencapsulate()
    }
}

fun main() = testBytesEncapsulating()