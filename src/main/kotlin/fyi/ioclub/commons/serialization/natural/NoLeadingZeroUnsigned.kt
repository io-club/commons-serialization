/* For unsigned integrals */

package fyi.ioclub.commons.serialization.natural

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: ULong, off: Int = 0) = putNoLeadingZero(value.toLong(), off)

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: UInt, off: Int = 0) = putNoLeadingZero(value.toInt(), off)

@JvmOverloads
fun ByteArray.putNoLeadingZero(value: UShort, off: Int = 0) = putNoLeadingZero(value.toShort(), off)

@JvmOverloads
fun ULong.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = toLong().toNoLeadingZeroBytes(dst, off)

@JvmOverloads
fun UInt.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = toInt().toNoLeadingZeroBytes(dst, off)

@JvmOverloads
fun UShort.toNoLeadingZeroBytes(dst: ByteArray, off: Int = 0) = toShort().toNoLeadingZeroBytes(dst, off)
fun ULong.toNoLeadingZeroBytes() = toLong().toNoLeadingZeroBytes()
fun UInt.toNoLeadingZeroBytes() = toInt().toNoLeadingZeroBytes()
fun UShort.toNoLeadingZeroBytes() = toShort().toNoLeadingZeroBytes()
val ULong.noLeadingZeroBytes get() = toNoLeadingZeroBytes()
val UInt.noLeadingZeroBytes get() = toNoLeadingZeroBytes()
val UShort.noLeadingZeroBytes get() = toNoLeadingZeroBytes()
