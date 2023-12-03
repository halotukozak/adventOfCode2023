import java.io.Serializable

fun main() {
  fun nextCells(coor: Coor): List<Coor> {
    val (row, column) = coor
    return listOf(
      Coor(row - 1, column),
      Coor(row + 1, column),
      Coor(row, column - 1),
      Coor(row, column + 1),
      Coor(row - 1, column + 1),
      Coor(row + 1, column + 1),
      Coor(row - 1, column - 1),
      Coor(row + 1, column - 1),
    )
  }

  fun part1(input: List<String>): Int {
    val markedPoints: Set<Coor> = input.foldIndexed(setOf()) { row, acc, line ->
      acc + (line.foldIndexed(setOf()) { column, innerAcc, char ->
        if (char !in '0'..'9' && char != '.') innerAcc + nextCells(Coor(row, column)) else innerAcc
      })
    }

    return input.foldIndexed(0) { row, result, line ->
      result + line.plus(".").foldIndexed(Acc(0, "", false)) { column, acc, char ->
        val (res, number, isMarked) = acc
        if (char.isDigit()) Acc(
          result = res,
          number = number + char,
          isMarked = Coor(row, column) in markedPoints,
        ) else Acc(
          result = if (isMarked) res + (number.toIntOrNull() ?: 0) else res,
          number = "",
          isMarked = false,
        )
      }.result
    }
  }


  fun part2(input: List<String>): Int {
    val maybeGears = input.foldIndexed(mapOf<Coor, MaybeGear>()) { row, result, line ->
      result + line.foldIndexed(mapOf()) { column, innerResult, char ->
        if (char == '*') innerResult + (Coor(row, column) to MaybeGear())
        else innerResult
      }
    }

    val numbers: List<Number> = input.foldIndexed(listOf()) { row, result, line ->
      result + line.plus(".").foldIndexed(Acc2()) { column, acc, char ->
        val (list, number) = acc
        if (char.isDigit()) {
          Acc2(
            result = list,
            number = number.withDigit(Coor(row, column), char)
          )
        } else if (number != Number()) {
          Acc2(result = list + number)
        } else {
          Acc2(result = list)
        }
      }.result
    }

    numbers.forEach { number ->
      number.digits
        .flatMap { (coor, _) -> nextCells(coor) }
        .forEach { maybeGears[it]?.bag?.add(number) }
    }

    return maybeGears.values.filter { it.bag.size == 2 }.fold(0) { acc, gear ->
      acc + gear.bag.fold(1) { innerAcc, number -> innerAcc * number.value() }
    }
  }

  val day = "Day03"
  val input = readInput(day)

  part1(input).let {
    println("03 Part 1: $it")
    check(it == 532428)
  }

  part2(input).let {
    println("03 Part 2: $it")
    check(it == 84051670)
  }
}

typealias Coor = Pair<Int, Int>

data class Number(val digits: LinkedHashMap<Coor, Int> = linkedMapOf()) {
  fun withDigit(coor: Coor, digit: Char): Number = this.copy(digits = digits.also { it[coor] = digit.digitToInt() })
  fun value(): Int = digits.toList().fold("") { acc, pair -> acc + pair.second }.toInt()
  override fun toString(): String = "Number(" + digits.values.joinToString("") + ")"
}

data class MaybeGear(val bag: MutableSet<Number> = mutableSetOf())
data class Acc(val result: Int, val number: String, val isMarked: Boolean)
data class Acc2(val result: List<Number> = listOf(), val number: Number = Number())
