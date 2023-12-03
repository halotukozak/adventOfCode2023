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
          res,
          number + char,
          Coor(row, column) in markedPoints,
        ) else Acc(
          res + (number.takeIf { _ -> isMarked }?.toIntOrNull() ?: 0),
          "",
          false,
        )
      }.result
    }
  }

  fun part2(input: List<String>): Int {
    val numbers: List<Number> = input.foldIndexed(listOf()) { row, result, line ->
      result + line.plus(".").foldIndexed(Acc2()) { column, acc, char ->
        val (list, number) = acc
        when {
          char.isDigit() -> Acc2(list, number.withDigit(Coor(row, column), char))
          number != Number() -> Acc2(list + number)
          else -> Acc2(list)
        }
      }.result
    }

    val maybeGears = input.foldIndexed(mapOf<Coor, MaybeGear>()) { row, result, line ->
      result + line.foldIndexed(mapOf()) { column, innerResult, char ->
        if (char == '*') innerResult + (Coor(row, column) to MaybeGear())
        else innerResult
      }
    }

    val gears = numbers
      .flatMap { number -> number.digits.keys.map { it to number } }
      .flatMap { (coor, number) -> nextCells(coor).map { it to number } }
      .fold(maybeGears) { acc, pair ->
        val (coor, number) = pair
        acc.mapValues { (k, v) ->
          if (k == coor) MaybeGear(v.bag + number)
          else v
        }
      }
      .values

    return gears
      .filter { it.bag.size == 2 }
      .fold(0) { acc, gear ->
        acc + gear.bag.fold(1) { innerAcc, number -> innerAcc * number.value() }
      }
  }

  val day = "Day03"
  val input = readInput(day)

  part1(input).let {
    println("03 Part 1: $it")
    check(it == 309128)
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

data class MaybeGear(val bag: Set<Number> = setOf())
data class Acc(val result: Int, val number: String, val isMarked: Boolean)
data class Acc2(val result: List<Number> = listOf(), val number: Number = Number())