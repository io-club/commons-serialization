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