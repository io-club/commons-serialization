package fyi.ioclub.commons.serialization.encapsulation

import fyi.ioclub.commons.serialization.encapsulation.naturalEncapsulateToCompressed
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.math.BigInteger
import fyi.ioclub.commons.serialization.encapsulation.naturalEncapsulateToCompressed as ec
import java.io.InputStream as IS
import java.nio.ByteBuffer as BB

private val TEST_BIG_INTEGER = BigInteger.valueOf(0)
private const val TEST_U_LONG = 0UL
private const val TEST_U_INT = 0u
private const val TEST_U_SHORT: UShort = 0u
private const val TEST_U_BYTE: UByte = 0u
private const val TEST_LONG = 0L
private const val TEST_INT = 0
private const val TEST_SHORT: Short = 0
private const val TEST_BYTE: Byte = 0

const val SIZE = 16

internal inline fun byteArrayTest(tester: (ByteArray) -> Unit) = ByteArray(SIZE).let(tester)
internal inline fun byteBufferTest(tester: (BB) -> Unit) = BB.allocate(SIZE).let(tester)
internal inline fun ioStreamTest(tester: (IS, OutputStream) -> Unit) =
    ByteArrayOutputStream(SIZE).let { tester(ByteArrayInputStream(it.toByteArray()), it) }

private fun <T> T.testUnit(
    toByteArray: (ByteArray) -> Unit,
    toBB: (BB) -> Unit,
    toOutputStream: (OutputStream) -> Unit,
    fromBB: (BB) -> T,
    fromIS: (IS) -> T,
) {
    byteArrayTest { toByteArray(it) }
    byteBufferTest { toBB(it); assert(fromBB(it) == this) }
    ioStreamTest { i, o -> toOutputStream(o); assert(fromIS(i) == this) }
}

fun testNaturalEncapsulating() {
    TEST_BIG_INTEGER.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateBigInteger, IS::unencapsulateBigInteger) }
    TEST_U_LONG.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateULong, IS::unencapsulateULong) }
    TEST_U_INT.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateUInt, IS::unencapsulateUInt) }
    TEST_U_SHORT.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateUShort, IS::unencapsulateUShort) }
    TEST_U_BYTE.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateUByte, IS::unencapsulateUByte) }
    TEST_LONG.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateLong, IS::unencapsulateLong) }
    TEST_INT.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateInt, IS::unencapsulateInt) }
    TEST_SHORT.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateShort, IS::unencapsulateShort) }
    TEST_BYTE.run { ec(); testUnit(::ec, ::ec, ::ec, BB::unencapsulateByte, IS::unencapsulateByte) }
    (1).toByte().naturalEncapsulateToCompressed()
}

fun main() = testNaturalEncapsulating()