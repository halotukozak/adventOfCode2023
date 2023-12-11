import Day07.Card
import Day07.Joker
import HandType.*
import kotlin.reflect.KClass

fun main() {

  fun count(input: List<String>, jokersEnabled: Boolean) =
    input
      .map { it.split(' ').let { Hand(it[0].map { Card.valueOf(it, jokersEnabled) }) to it[1].toInt() } }
      .sortedBy { (hand, _) -> hand }
      .foldIndexed(0) { i, acc, (_, gid) -> acc + (i + 1) * gid }

  fun part1(input: List<String>) = count(input, false)

  fun part2(input: List<String>) = count(input, true)

  val day = "Day07"
  val input = readInput(day)

  part1(input).let {
    println("07 Part 1: $it")
    check(it == 241344943)
  }

  part2(input).let {
    println("07 Part 2: $it")
    check(it == 243101568)
  }
}

enum class HandType {
  HighCard, OnePair, TwoPairs, ThreeOfAKind, FullHouse, FourOfAKind, FiveOfAKind;
}

data class Hand(val type: HandType, val cards: List<Card>) : Comparable<Hand> {
  constructor(cards: List<Card>) : this(
    cards
      .filterNot { it is Joker }
      .groupBy { it::class }
      .values
      .sortedBy { it.size }
      .let {
        when (it.size) {
          0, 1 -> FiveOfAKind
          2 -> if (it[0].size == 1) FourOfAKind else FullHouse
          3 -> if (it[1].size == 2) TwoPairs else ThreeOfAKind
          4 -> OnePair
          5 -> HighCard
          else -> throw Exception("wtf")
        }
      },
    cards,
  )

  override fun compareTo(other: Hand): Int {
    if (type != other.type) return type.compareTo(other.type)
    cards
      .zip(other.cards)
      .forEach { (c, o) ->
        if (c != o) return c.compareTo(o)
      }
    return 0
  }
}

object Day07 {
  sealed class Card(private val ordinal: Int, val symbol: Char) : Comparable<Card> {


    constructor(ordinal: Int) : this(ordinal, ordinal.digitToChar())

    companion object {

      private val values = Card::class.sealedSubclasses.mapNotNull(KClass<out Card>::objectInstance)

      fun valueOf(c: Char, jokersEnabled: Boolean = false): Card =
        if (jokersEnabled && c == Joker.symbol) Joker
        else values.find { it.symbol == c && it !is Joker }
          ?: throw IllegalArgumentException("Provide one of the valid values: ${values.map(Card::symbol)}")
    }

    override fun compareTo(other: Card): Int = this.ordinal.compareTo(other.ordinal)

  }

  data object Two : Card(2)
  data object Three : Card(3)
  data object Four : Card(4)
  data object Five : Card(5)
  data object Six : Card(6)
  data object Seven : Card(7)
  data object Eight : Card(8)
  data object Nine : Card(9)
  data object Ten : Card(10, 'T')
  data object Jack : Card(11, 'J')
  data object Queen : Card(12, 'Q')
  data object King : Card(13, 'K')
  data object Ace : Card(14, 'A')
  data object Joker : Card(0, 'J')
}