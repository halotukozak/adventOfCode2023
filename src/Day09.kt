fun main() {
  fun part1(input: List<String>): Int =
    input.fold(0) { acc, line ->

      fun count(numbers: List<Int>): Int =
        if (numbers.all { it == 0 }) 0
        else numbers.zipWithNext { a, b -> b - a }.let { it.last() + count(it) }

      val numbers = line.split(' ').map { it.toInt() }
      acc + numbers.last() + count(numbers)
    }


  fun part2(input: List<String>): Int =
    input.fold(0) { acc, line ->

      tailrec fun count(numbers: List<Int>, result: List<Int> = emptyList()): List<Int> =
        if (numbers.any { it != 0 }) {
          val converted = numbers.zipWithNext { a, b -> b - a }
          count(converted, result + converted.first())
        } else result

      val numbers = line.split(' ').map { it.toInt() }
      acc + count(numbers, listOf(numbers.first()))
        .reversed()
        .reduce { a, b -> b - a }
    }

  val day = "Day09"
  val input = readInput(day)

  part2(
    listOf(
      "0 3 6 9 12 15",
      "1 3 6 10 15 21",
      "10 13 16 21 30 45"
    )
  )

  part1(input).let {
    println("09 Part 1: $it")
    check(it == 1842168671)
  }
  part2(input).let {
    println("09 Part 2: $it")
    check(it == 903)
  }
}
