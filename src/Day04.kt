import kotlin.math.pow

fun main() {

  fun part1(input: List<String>): Int =
    input.fold(0) { acc, line ->
      acc + 2.pow(line.split(':', '|').let {
        it.drop(1).map { it.split(' ').filter(String::isNotEmpty) }
      }.let {
        it[0].intersect(it[1]).size - 1
      })
    }

  fun part2(input: List<String>): Int =
    input.foldIndexed(List(input.size) { 1 }) { row, acc, line ->
      line.split(':', '|')
        .let { it.drop(1).map { it.split(' ').filter(String::isNotEmpty) } }
        .let { it[0].intersect(it[1]).size }
        .let { wonCards -> acc.mapIndexed { i, n -> n + if (row < i && i <= row + wonCards) acc[row] else 0 } }
    }.sum()


  val day = "Day04"
  val input = readInput(day)

  part1(input).let {
    println("04 Part 1: $it")
    check(it == 21919)
  }
  part2(input).let {
    println("04 Part 2: $it")
    check(it == 9881048)
  }
}

fun Int.pow(n: Int): Int = this.toDouble().pow(n.toDouble()).toInt()