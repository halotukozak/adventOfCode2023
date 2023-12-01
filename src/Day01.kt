fun main() {
    fun part1(input: List<String>): Int = input.fold(0) { acc, e ->
        acc + "${e.first(Char::isDigit)}${e.last(Char::isDigit)}".toInt()
    }

    fun part2(input: List<String>): Int {
        fun String.findFirstDigit(reversed: Boolean = false): Int {
            val predicate: (String.(String) -> Boolean) = if (reversed) String::endsWith else String::startsWith

            return when {
                predicate("one") || predicate("1") -> 1
                predicate("two") || predicate("2") -> 2
                predicate("three") || predicate("3") -> 3
                predicate("four") || predicate("4") -> 4
                predicate("five") || predicate("5") -> 5
                predicate("six") || predicate("6") -> 6
                predicate("seven") || predicate("7") -> 7
                predicate("eight") || predicate("8") -> 8
                predicate("nine") || predicate("9") -> 9
                else -> (if (reversed) dropLast(1) else drop(1)).findFirstDigit(reversed)
            }
        }

        return input.fold(0) { acc, e ->
            acc + "${e.findFirstDigit()}${e.findFirstDigit(true)}".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 1)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
