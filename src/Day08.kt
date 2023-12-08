import Direction.L

fun main() {

  tailrec fun findDistance(
    curr: String,
    moves: List<Direction>,
    nodes: Map<String, Children>,
    acc: Long = 0,
    predicate: (String) -> Boolean,
  ): Long = when {
    predicate(curr) -> acc
    moves[(acc % moves.size).toInt()] == L -> findDistance(
      nodes[curr]!!.left,
      moves,
      nodes,
      acc + 1,
      predicate,
    )

    else -> findDistance(
      nodes[curr]!!.right,
      moves,
      nodes,
      acc + 1,
      predicate,
    )
  }

  fun parse(input: List<String>) = input.let {
    it[0].map { Direction.valueOf(it.toString()) } to it.drop(2).map {
      it.split(" = ").let {
        it[0] to it[1].toChildren()
      }
    }.toMap()
  }

  fun part1(input: List<String>): Long =
    parse(input).let { (moves, nodes) ->
      findDistance("AAA", moves, nodes) { x: String -> x.endsWith("ZZZ") }
    }

  fun part2(input: List<String>): Long {
    val (moves, nodes) = parse(input)

    fun lcm(a: Long, b: Long): Long {
      tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
      return a * b / gcd(a, b)
    }

    return nodes.filter { it.key.endsWith('A') }
      .keys
      .map { curr -> findDistance(curr, moves, nodes) { it.endsWith('Z') } }
      .reduce(::lcm)
  }

  val day = "Day08"
  val input = readInput(day)

  part1(input).let {
    println("08 Part 1: $it")
    check(it == 14429L)
  }

  part2(input).let {
    println("08 Part 2: $it")
    check(it == 10921547990923)
  }
}

data class Children(val left: String, val right: String)

private fun String.toChildren(): Children =
  split("[^A-Z0-9]+".toRegex()).filterNot(String::isBlank).let { Children(it[0], it[1]) }

enum class Direction {
  L, R
}