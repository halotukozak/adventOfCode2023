package utils

import java.math.BigInteger

data class BigIntegerRange(val start: BigInteger, val endInclusive: BigInteger) : Iterable<BigInteger> {
  operator fun contains(o: BigInteger) = start <= o && o <= endInclusive

  override fun iterator(): Iterator<BigInteger> = object : Iterator<BigInteger> {

    var initValue = start

    override fun hasNext(): Boolean = initValue <= endInclusive

    override fun next(): BigInteger = initValue++

  }

  override fun toString(): String = "$start..$endInclusive"
}
operator fun BigInteger.rangeTo(o: BigInteger) = BigIntegerRange(this, o)
fun bigMin(x: BigInteger, y: BigInteger) = if (x <= y) x else y
