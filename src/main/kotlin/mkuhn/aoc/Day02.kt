package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day02")
    println(day2part1(input))
    println(day2part2(input))
}

fun day2part1(input: List<String>): Int {
    val games = input.map { CubeGame.fromLine(it) }
    return games.filter { it.isPossibleWithCounts(12, 13, 14) }
        .apply { this.forEach { println(it) } }
        .sumOf { it.id }
}

fun day2part2(input: List<String>): Int =
    0


data class CubeGame (val id: Int, val reveals: List<CubeGameReveal>) {

    fun isPossibleWithCounts(red: Int, green: Int, blue: Int): Boolean =
        reveals.all { it.red <= red && it.green <= green && it.blue <= blue }

    companion object {
        fun fromLine(line: String): CubeGame {
            val parts = line.split(": ", "; ")
            val id = parts[0].drop(5).toInt()
            val reveals = parts.drop(1).map {
                CubeGameReveal.fromString(it)
            }
            return CubeGame(id, reveals)
        }
    }
}

data class CubeGameReveal(val red: Int, val green: Int, val blue: Int) {
    companion object {
        fun fromString(input: String): CubeGameReveal {
            var r = 0
            var g = 0
            var b = 0
            input.split(", ").forEach { colorCount ->
                when(colorCount.substringAfter(" ")) {
                    "red" -> r = colorCount.substringBefore(" ").trim().toInt()
                    "green" -> g = colorCount.substringBefore(" ").trim().toInt()
                    "blue" -> b = colorCount.substringBefore(" ").trim().toInt()
                }
            }
            return CubeGameReveal(r, g, b)
        }
    }
}