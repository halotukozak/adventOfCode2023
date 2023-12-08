import Day05.Acc
import kotlin.math.min

fun main() {

  fun getLayers(tail: List<String>) =
    tail
      .fold<String, List<List<Layer>>>(listOf()) { acc, line ->
        when {
          line.isBlank() -> acc with emptyList()
          line.endsWith("map:") -> acc
          else -> {
            val (list, e) = acc.dropLast(1) to acc.last()
            list with (e + line.split(' ').map(String::toLong).let { Layer(it[0], it[1], it[2]) })
          }
        }
      }
      .mapNotNull { it.reduceOrNull(Layer::merge) }

  fun part1(input: List<String>): Long =
    input.headTail().let { (head, tail) ->
      val seeds = head.split(' ').mapNotNull(String::toLongOrNull)
      val layers = getLayers(tail)

      seeds
        .map { layers.fold(it) { acc, conversion -> conversion.convert(acc) } }
        .reduce(::min)
    }

  fun part2(input: List<String>): Long =
    input
      .headTail()
      .let { (head, tail) ->
        val seeds =
          head
            .split(' ')
            .mapNotNull(String::toLongOrNull)
            .chunked(2)
            .map { (start, size) -> Range(start, start + size - 1) }

        val layers = getLayers(tail)

        seeds
          .flatMap { layers.fold(listOf(it)) { acc, layer -> layer.convert(acc) } }
          .map(Range::first)
          .reduce(::min)
      }

  val day = "Day05"
  val input = readInput(day)

  part1(input).let {
    println("05 Part 1: $it")
    check(it == 331445006L)
  }
  part2(input).let {
    println("05 Part 2: $it")
    check(it == 6472060L)
  }
}

data class Layer(val conversions: List<Pair<(Long) -> Long, Range>>) {
  constructor(destination: Long, source: Long, size: Long) : this(
    listOf({ v: Long -> v + destination - source } to Range(source, (source + size - 1)))
  )

  fun convert(e: Long): Long = conversions.find { (_, range) -> e in range }?.let { (f, _) -> f(e) } ?: e

  fun convert(elements: List<Range>): List<Range> =
    conversions
      .fold(Acc(elements)) { acc, (f, range) ->
        acc
          .initial
          .fold(Acc(emptyList(), acc.converted)) { innerAcc, initial ->
            (initial / range)
              ?.let { (intersection, disjunctions) ->
                Acc(
                  innerAcc.initial + disjunctions - initial,
                  innerAcc.converted with intersection?.map(f),
                )
              }
              ?: Acc(innerAcc.initial + initial, innerAcc.converted)
          }
      }
      .let { it.initial + it.converted }

  fun merge(o: Layer): Layer = Layer(this.conversions + o.conversions)
}

infix fun <T, U : Collection<T>> U.with(e: T?): List<T> = if (e == null) this.toList() else this + (e)
fun <T> List<T>.headTail() = first() to drop(1)

data class Range(val first: Long, val last: Long) {

  companion object {
    fun safe(start: Long, endInclusive: Long): Range? = Range(start, endInclusive).takeIf { start <= endInclusive }
  }

  data class Splitted(val intersection: Range? = null, val disjunctions: Set<Range> = setOf())

  operator fun div(other: Range): Splitted? {
    val (x0, x1) = this
    val (y0, y1) = other

    return when {
      x1 < y0 || y1 < x0 -> null
      this in other -> Splitted(this)
      other in this -> Splitted(
        other,
        setOfNotNull(
          safe(x0, y0 - 1),
          safe(y1 + 1, x1),
        ),
      )

      y0 < x0 -> Splitted(
        Range(x0, y1),
        setOfNotNull(
          safe(y1 + 1, x1)
        )
      )

      else -> Splitted(
        Range(y0, x1),
        setOfNotNull(
          safe(x0, y0 - 1)
        )
      )
    }
  }

  fun map(f: (Long) -> Long) = Range(f(first), f(last))

  operator fun contains(other: Range) = this.first <= other.first && this.last >= other.last
  operator fun contains(e: Long) = this.first <= e && e <= this.last

}

object Day05 {
  data class Acc(val initial: List<Range> = emptyList(), val converted: List<Range> = emptyList())
}