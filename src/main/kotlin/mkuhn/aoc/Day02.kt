package mkuhn.aoc

import mkuhn.aoc.util.readInput
import java.lang.Integer.max

fun main() {
    val input = readInput("Day02")
    println(day2part1(input))
    println(day2part2(input))
}

fun day2part1(input: List<String>): Int {
    return input.map { CubeGame.fromLine(it) }
        .filter { it.isPossibleWithCounts(12, 13, 14) }
        .sumOf { it.id }
}

fun day2part2(input: List<String>): Int {
    return input.map { CubeGame.fromLine(it) }
        .sumOf { it.getMinimumCubeCount().power() }
}


data class CubeGame (val id: Int, val reveals: List<CubeCount>) {

    fun isPossibleWithCounts(red: Int, green: Int, blue: Int): Boolean =
        reveals.all { it.red <= red && it.green <= green && it.blue <= blue }

    fun getMinimumCubeCount(): CubeCount =
        reveals.reduce { acc, cc ->
            CubeCount(max(acc.red, cc.red), max(acc.green, cc.green), max(acc.blue, cc.blue))
        }

    companion object {
        fun fromLine(line: String): CubeGame {
            val parts = line.split(": ", "; ")
            val id = parts[0].drop(5).toInt()
            val reveals = parts.drop(1).map {
                CubeCount.fromString(it)
            }
            return CubeGame(id, reveals)
        }
    }
}

data class CubeCount(val red: Int, val green: Int, val blue: Int) {

    fun power() = red*green*blue

    companion object {
        fun fromString(input: String): CubeCount {
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
            return CubeCount(r, g, b)
        }
    }
}