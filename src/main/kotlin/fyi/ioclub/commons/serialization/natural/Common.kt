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
import java.math.BigInteger

const val NATURAL_BIG_INTEGER_AUTO_LEAST_LENGTH = -1

internal const val AUTO = NATURAL_BIG_INTEGER_AUTO_LEAST_LENGTH

internal const val LONG_0: Long = 0
internal const val INT_0: Int = 0
internal const val SHORT_0: Short = 0
internal const val BYTE_0: Byte = 0

/** Natural big integer. Whose [BigInteger.signum] returns `0` or `1`. */
typealias NaturalBigInteger = BigInteger

internal fun NaturalBigInteger(bas: ByteArraySlice): NaturalBigInteger =
    bas.run { BigInteger(1, array, offset, length) }

fun requireNatural(value: BigInteger): NaturalBigInteger {
    if (value.signum() < 0) throw IllegalArgumentException("Big integer $value is not a natural number")
    return value
}
