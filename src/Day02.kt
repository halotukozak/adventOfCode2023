import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int = input.fold(0) { acc: Int, game: String ->
        val (header, sets) = game.split(": ")
        sets.splitToSequence("; ").forEach { set ->
            val availableCubes = mutableListOf(/*red*/12, /*green*/13,/*blue*/14)
            val cubes = set.splitToSequence(", ")
            cubes.forEach { cube ->
                val (number, color) = cube.split(' ')
                val index = when (color) {
                    "red" -> 0
                    "green" -> 1
                    "blue" -> 2
                    else -> return@fold acc
                }
                availableCubes[index] = availableCubes[index] - number.toInt()
                if (availableCubes.any { it < 0 }) return@fold acc
            }
        }
        val id = header.split("Game ")[1].toInt()
        acc + id
    }

    fun part2(input: List<String>): Int = input.fold(0) { acc: Int, game: String ->
        val (_, sets) = game.split(": ")
        var minimalRequiredCubes = List(3) { 0 }
        sets.splitToSequence("; ").forEach { set ->
            val requiredCubes = MutableList(3) { 0 }
            val cubes = set.splitToSequence(", ")
            cubes.forEach { cube ->
                val (number, color) = cube.split(' ')
                val index = when (color) {
                    "red" -> 0
                    "green" -> 1
                    "blue" -> 2
                    else -> return@fold acc
                }
                requiredCubes[index] = requiredCubes[index] + number.toInt()
            }
            minimalRequiredCubes = minimalRequiredCubes.zip(requiredCubes).map { max(it.first, it.second) }
        }
        acc + minimalRequiredCubes.reduce(Int::times)
    }

    val day = "Day02"
    val input = readInput(day)

    part1(input).let {
        check(it == 2317)
        println("02 Part 1: $it")
    }
    part2(input).let {
        check(it == 74804)
        println("02 Part 2: $it")
    }
}
