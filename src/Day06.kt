import utils.rangeTo
import java.math.BigInteger

fun main() {
  fun part1(input: List<String>): Int =
    input
      .map {
        it.split(' ')
          .drop(1)
          .mapNotNull(String::toIntOrNull)
      }
      .let { it[0].zip(it[1]) }
      .fold(1) { acc, (time, distance) ->
        acc * (0..time)
          .fold(0) { innerAcc, holdingTime ->
            if (holdingTime * (time - holdingTime) > distance) innerAcc + 1
            else innerAcc
          }
      }

  fun part2(input: List<String>): Int =
    input
      .map {
        it.filter(Char::isDigit)
          .toBigInteger()
      }
      .let { it[0] to (it[1]) }
      .let { (time, distance) ->
        (BigInteger.ZERO..time)
          .fold(0) { innerAcc, holdingTime ->
            if (holdingTime * (time - holdingTime) > distance) innerAcc + 1
            else innerAcc
          }
      }


  val day = "Day06"
  val input = readInput(day)

  part1(input).let {
    println("06 Part 1: $it")
    check(it == 131376)
  }
  part2(input).let {
    println("06 Part 2: $it")
    check(it == 34123437)
  }
}