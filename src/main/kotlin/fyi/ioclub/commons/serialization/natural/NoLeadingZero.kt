/* To remove leading zeros */

package fyi.ioclub.commons.serialization.natural

import java.io.OutputStream
import java.math.BigInteger
import java.nio.ByteBuffer


// To get the index of the first non-zero byte

@JvmOverloads
fun ByteArray.getFirstNonZeroIndexIn(from: Int = 0, to: Int = size): Int {
    for (i in from..<to) if (get(i) != ZERO_BYTE) return i
    return to
}

val ByteArray.firstNonZeroIndex get() = getFirstNonZeroIndexIn()

// To get no-leading-zero byte array

@JvmOverloads
fun ByteArray.toNoLeadingZeroBytes(from: Int, to: Int, dst: ByteArray, off: Int = 0): Int =
    getFirstNonZeroIndexIn(from, to).let {
        val len = to - it
        System.arraycopy(this, it, dst, off, to - it)
        len
    }

@JvmOverloads
fun ByteArray.toNoLeadingZeroBytes(from: Int, dst: ByteArray, off: Int = 0) = toNoLeadingZeroBytes(from, size, dst, off)

@JvmOverloads
fun ByteArray.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = toNoLeadingZeroBytes(0, size, dst, off)

@JvmOverloads
fun ByteArray.toNoLeadingZeroBytes(from: Int = 0, to: Int = size): ByteArray {
    val i = getFirstNonZeroIndexIn(from, to)
    val len = to - i
    return ByteArray(len).also { System.arraycopy(this, i, it, 0, len) }
}

//val ByteArray.noLeadingZeroBytes get() = toNoLeadingZeroBytes()

// To put into byte array

private inline fun putNoLeadingZeroBigInteger(value: BigInteger, writer: (ByteArray, Int, Int) -> Unit): Int {
    val src = value.toByteArray()
    val off = if (src[0] == ZERO_BYTE) 0 else 1
    return (src.size - off).also { writer(src, off, it) }
}

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: BigInteger, off: Int = 0) =
    putNoLeadingZeroBigInteger(value) { src, srcOff, len -> System.arraycopy(src, srcOff, this, off, len) }

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: Long, off: Int = 0) = value.naturalToBytes().toNoLeadingZeroBytes(this, off)

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: Int, off: Int = 0) = value.naturalToBytes().toNoLeadingZeroBytes(this, off)

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: Short, off: Int = 0) = value.naturalToBytes().toNoLeadingZeroBytes(this, off)

@JvmOverloads
fun BigInteger.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = dst.putNoLeadingZero(this, off)

@JvmOverloads
fun Long.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = dst.putNoLeadingZero(this, off)

@JvmOverloads
fun Int.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = dst.putNoLeadingZero(this, off)

@JvmOverloads
fun Short.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = dst.putNoLeadingZero(this, off)

fun BigInteger.toNoLeadingZeroBytes() = naturalToBytes() // No leading zeros already
fun Long.toNoLeadingZeroBytes() = naturalToBytes().toNoLeadingZeroBytes()
fun Int.toNoLeadingZeroBytes() = naturalToBytes().toNoLeadingZeroBytes()
fun Short.toNoLeadingZeroBytes() = naturalToBytes().toNoLeadingZeroBytes()
//val BigInteger.noLeadingZeroBytes get() = toNoLeadingZeroBytes()
//val Long.noLeadingZeroBytes get() = toNoLeadingZeroBytes()
//val Int.noLeadingZeroBytes get() = toNoLeadingZeroBytes()
//val Short.noLeadingZeroBytes get() = toNoLeadingZeroBytes()

// To put into byte buffer
fun ByteBuffer.putNoLeadingZero(value: BigInteger) = putNoLeadingZeroBigInteger(value, ::put)
fun ByteBuffer.putNoLeadingZero(value: Long) = value.toNoLeadingZeroBytes().let { put(it); it.size }
fun ByteBuffer.putNoLeadingZero(value: Int) = value.toNoLeadingZeroBytes().let { put(it); it.size }
fun ByteBuffer.putNoLeadingZero(value: Short) = value.toNoLeadingZeroBytes().let { put(it); it.size }

// To put into output stream
fun OutputStream.putNoLeadingZero(value: BigInteger) = putNoLeadingZeroBigInteger(value, ::write)
fun OutputStream.putNoLeadingZero(value: Long) = value.toNoLeadingZeroBytes().let { write(it); it.size }
fun OutputStream.putNoLeadingZero(value: Int) = value.toNoLeadingZeroBytes().let { write(it); it.size }
fun OutputStream.putNoLeadingZero(value: Short) = value.toNoLeadingZeroBytes().let { write(it); it.size }
