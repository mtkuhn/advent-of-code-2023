package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day06")
    println(day6part1(input))
    println(day6part2(input))
}

fun day6part1(input: List<String>): Int {
    val times = input.first().substringAfter(":").trim().split("\\s+".toRegex()).map { it.toLong() }
    val distances = input.last().substringAfter(":").trim().split("\\s+".toRegex()).map { it.toLong() }
    return times.zip(distances).map { pair ->
        (1 until pair.first).count { pressTime -> distanceMoved(pair.first, pressTime) > pair.second }
    }.reduce { acc, c -> acc*c }
}

fun day6part2(input: List<String>): Int {
    val time = input.first().substringAfter(":").replace(" ", "").toLong()
    val distance = input.last().substringAfter(":").replace(" ", "").toLong()
    return (1 until time).count { pressTime -> distanceMoved(time, pressTime) > distance }
}

fun distanceMoved(raceTime: Long, pressTime: Long) = (raceTime-pressTime)*pressTime