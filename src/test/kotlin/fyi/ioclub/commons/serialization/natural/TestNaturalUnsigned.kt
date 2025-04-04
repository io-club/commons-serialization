package fyi.ioclub.commons.serialization.natural

import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

private const val TEST_U_LONG = 0UL
private const val TEST_U_INT = 0u
private const val TEST_U_SHORT: UShort = 0u
private const val TEST_U_BYTE: UByte = 0u

fun testNaturalUnsigned() {
    test(
        TEST_U_LONG,
        ULong.SIZE_BYTES,
        Getters(ByteArray::getNaturalULong, ByteBuffer::getNaturalULong, InputStream::readNaturalULong),
        Putters(
            ByteArray::putNatural,
            ULong::naturalToBytes,
            ULong::naturalToBytes,
            ByteBuffer::putNatural,
            OutputStream::writeNatural,
        ),
    )
    test(
        TEST_U_INT,
        UInt.SIZE_BYTES,
        Getters(ByteArray::getNaturalUInt, ByteBuffer::getNaturalUInt, InputStream::readNaturalUInt),
        Putters(
            ByteArray::putNatural,
            UInt::naturalToBytes,
            UInt::naturalToBytes,
            ByteBuffer::putNatural,
            OutputStream::writeNatural,
        ),
    )
    test(
        TEST_U_SHORT,
        UShort.SIZE_BYTES,
        Getters(ByteArray::getNaturalUShort, ByteBuffer::getNaturalUShort, InputStream::readNaturalUShort),
        Putters(
            ByteArray::putNatural,
            UShort::naturalToBytes,
            UShort::naturalToBytes,
            ByteBuffer::putNatural,
            OutputStream::writeNatural,
        ),
    )
    test(
        TEST_U_BYTE,
        UByte.SIZE_BYTES,
        Getters(ByteArray::getNaturalUByte, ByteBuffer::getNaturalUByte, InputStream::readNaturalUByte),
        Putters(
            ByteArray::putNatural,
            UByte::naturalToBytes,
            UByte::naturalToBytes,
            ByteBuffer::putNatural,
            OutputStream::writeNatural,
        ),
    )
}

fun main() = testNaturalUnsigned()
