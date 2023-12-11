import kotlin.math.max
import kotlin.math.min

fun main() {

  fun resolve(input: List<String>, multiplication: Long): Long {
    val widerRows: List<Int> = input.foldIndexed(emptyList()) { i, acc, line ->
      if (line.all { it.isNotGalaxy() }) acc + i else acc
    }

    val widerColumns: List<Int> = (0..<input[0].length).fold(listOf()) { acc, i ->
      if (input.all { it[i].isNotGalaxy() }) acc + i else acc
    }

    val galaxiesCoors: List<Coor> =
      input
        .foldIndexed(listOf()) { row, acc, line ->
          acc + line.foldIndexed(listOf()) { col, innerAcc, char ->
            if (char.isNotGalaxy()) innerAcc
            else innerAcc with Coor(row, col)
          }
        }

    return galaxiesCoors
      .flatMap { a -> galaxiesCoors.map { b -> a to b } }
      .sumOf { (coor1, coor2) ->

        val (x0, x1) = min(coor1.first, coor2.first) to max(coor1.first, coor2.first)
        val (y0, y1) = min(coor1.second, coor2.second) to max(coor1.second, coor2.second)

        val horizontalDistance = (x0..x1)
          .sumOf { i -> if (i in widerRows) multiplication else 1 } - 1
        val verticalDistance = (y0..y1)
          .sumOf { i -> if (i in widerColumns) multiplication else 1 } - 1
        horizontalDistance + verticalDistance
      } / 2
  }

  fun part1(input: List<String>) = resolve(input, 2)

  fun part2(input: List<String>) = resolve(input, 1000000)

  val day = "Day11"
  val input = readInput(day)

  part1(input).let {
    println("11 Part 1: $it")
    check(it == 10494813L)
  }
  part2(input).let {
    println("11 Part 2: $it")
    check(it == 840988812853)
  }
}

private fun Char.isNotGalaxy() = this == '.'
