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

import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

fun ByteArray.encapsulateToList(): List<ByteArray> = listOf(size.naturalEncapsulateToCompressed(), this)

/**
 * Write the size of data and the data to the byte array.
 *
 * @param off    destination offset
 * @param src    source byte array
 * @param srcOff source offset
 * @param srcLen source length
 * @return the number of bytes written
 */
fun ByteArray.encapsulate(off: Int, src: ByteArray, srcOff: Int, srcLen: Int): Int =
    encapsulateNaturalCompressed(srcLen, off).let {
        System.arraycopy(src, srcOff, this, off + it, srcLen)
        it + srcLen
    }

/**
 * Write the size of data and the data to the byte array.
 *
 * @param off    destination offset
 * @param src    source byte array
 * @param srcLen source length
 * @return the number of bytes written
 */
@JvmOverloads
fun ByteArray.encapsulate(off: Int, src: ByteArray, srcLen: Int = src.size) = encapsulate(off, src, 0, srcLen)

/**
 * Write the size of data and the data to the byte array.
 *
 * @param src    source byte array
 * @param srcOff source offset
 * @param srcLen source length
 * @return the number of bytes written
 */
fun ByteArray.encapsulate(src: ByteArray, srcOff: Int, srcLen: Int) = encapsulate(0, src, srcOff, srcLen)

/**
 * Write the size of data and the data to the byte array.
 *
 * @param src    source byte array
 * @param srcLen source length
 * @return the number of bytes written
 */
fun ByteArray.encapsulate(src: ByteArray, srcLen: Int = src.size) = encapsulate(src, 0, srcLen)

/**
 * Write the size of data and the data to the byte buffer.
 *
 * @param src    source byte array
 * @param srcOff source offset
 * @param srcLen length
 * @return the number of bytes written
 */
fun ByteBuffer.encapsulate(src: ByteArray, srcOff: Int, srcLen: Int) =
    encapsulateNaturalCompressed(srcLen).let { put(src, srcOff, srcLen); it + srcLen }

/**
 * Write the size of data and the data to the byte buffer.
 *
 * @param src    source byte array
 * @param srcLen length
 * @return the number of bytes written
 */
@JvmOverloads
fun ByteBuffer.encapsulate(src: ByteArray, srcLen: Int = src.size) = encapsulate(src, 0, srcLen)

/**
 * Write the size of data and the data to the output stream.
 *
 * @param src    source byte array
 * @param srcOff source offset
 * @param srcLen length
 * @return the number of bytes written
 */
fun OutputStream.encapsulate(src: ByteArray, srcOff: Int, srcLen: Int) =
    encapsulateNaturalCompressed(srcLen).let { write(src, srcOff, srcLen); it + srcLen }

/**
 * Write the size of data and the data to the output stream.
 *
 * @param src    source byte array
 * @param srcLen length
 * @return the number of bytes written
 */
fun OutputStream.encapsulate(src: ByteArray, srcLen: Int = src.size): Int = encapsulate(src, 0, srcLen)

/**
 * Write the size of data and the data to a byte array.
 *
 * @param off    source offset
 * @param len    source length
 * @param dst    destination byte array
 * @param dstOff destination offset
 * @return the number of bytes written
 */
@JvmOverloads
fun ByteArray.encapsulateToBytes(off: Int, len: Int, dst: ByteArray, dstOff: Int = 0) =
    dst.encapsulate(dstOff, this, off, len)

/**
 * Write data size and data to byte array.
 *
 * @param len    source length
 * @param dst    destination byte array
 * @param dstOff destination offset
 * @return the number of bytes written
 */
@JvmOverloads
fun ByteArray.encapsulateToBytes(len: Int, dst: ByteArray, dstOff: Int = 0) = encapsulateToBytes(0, len, dst, dstOff)

/**
 * Write the size of data and the data to a byte array.
 *
 * @param dst    destination byte array
 * @param dstOff destination offset
 * @return the number of bytes written
 */
@JvmOverloads
fun ByteArray.encapsulateToBytes(dst: ByteArray, dstOff: Int = 0) = encapsulateToBytes(size, dst, dstOff)

/**
 * Write the size of data and the data to a byte buffer.
 *
 * @param off    source offset
 * @param len    source length
 * @param dst    destination byte buffer
 * @return the number of bytes written
 */
fun ByteArray.encapsulateToBytes(off: Int, len: Int, dst: ByteBuffer) = dst.encapsulate(this, off, len)

/**
 * Write the size of data and the data to a byte buffer.
 *
 * @param len    source length
 * @param dst    destination byte buffer
 * @return the number of bytes written
 */
fun ByteArray.encapsulateToBytes(len: Int, dst: ByteBuffer) = encapsulateToBytes(0, len, dst)

/**
 * Write the size of data and the data to a byte buffer.
 *
 * @param dst    destination byte buffer
 * @return the number of bytes written
 */
fun ByteArray.encapsulateToBytes(dst: ByteBuffer) = encapsulateToBytes(size, dst)

/**
 * Write the size of data and the data to an output stream.
 *
 * @param off    source offset
 * @param len    source length
 * @param dst    destination output stream
 * @return the number of bytes written
 */
fun ByteArray.encapsulateToBytes(off: Int, len: Int, dst: OutputStream) = dst.encapsulate(this, off, len)

/**
 * Write the size of data and the data to an output stream.
 *
 * @param len    source length
 * @param dst    destination output stream
 * @return the number of bytes written
 */
fun ByteArray.encapsulateToBytes(len: Int, dst: OutputStream) = encapsulateToBytes(0, len, dst)

/**
 * Write the size of data and the data to an output stream.
 *
 * @param dst    destination output stream
 * @return the number of bytes written
 */
fun ByteArray.encapsulateToBytes(dst: OutputStream) = encapsulateToBytes(size, dst)

/**
 * Write the size of data and the data to a byte array.
 *
 * @param off source position
 * @param len length
 * @return the result byte array
 */
fun ByteArray.encapsulateToBytes(off: Int, len: Int): ByteArray {
    val src0 = len.naturalEncapsulateToCompressed()
    val len0 = src0.size
    return ByteArray(len0 + len).also {
        System.arraycopy(src0, 0, it, 0, len0)
        System.arraycopy(this, off, it, len0, len)
    }
}

/**
 * Write the size of data and the data to a byte array.
 *
 * @param len length
 * @return the result byte array
 */
@JvmOverloads
fun ByteArray.encapsulateToBytes(len: Int = size) = encapsulateToBytes(0, size)
//val ByteArray.encapsulatedBytes get() = encapsulateToBytes()

/**
 * Read packed data from buffer by its size indicated in the byte buffer.
 *
 * @param dst    destination array
 * @param dstOff destination offset
 * @return the number of bytes written
 */
@JvmOverloads
fun ByteBuffer.unencapsulate(dst: ByteArray, dstOff: Int = 0) =
    ByteArray(unencapsulateInt()).also { get(dst, dstOff, it.size) }

/**
 * Read packed data from buffer by its size indicated in the input stream.
 *
 * @param dst    destination array
 * @param dstOff destination offset
 * @return the number of bytes written
 */
@JvmOverloads
fun InputStream.unencapsulate(dst: ByteArray, dstOff: Int = 0) =
    ByteArray(unencapsulateInt()).also { read(dst, dstOff, it.size) }

/**
 * Read packed data from buffer by its size indicated in the byte buffer.
 *
 * @return the result bytes
 */
fun ByteBuffer.unencapsulate() = ByteArray(unencapsulateInt()).also(::get)

/**
 * Read packed data from buffer by its size indicated in the input stream.
 *
 * @return the result bytes
 */
fun InputStream.unencapsulate() = ByteArray(unencapsulateInt()).also(::read)