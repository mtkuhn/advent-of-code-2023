package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day03")
    println(day3part1(input))
    println(day3part2(input))
}

fun day3part1(input: List<String>): Int {
    val numberSurroundingRanges = input.findAllMatchesWithSurroundingBounds("\\d+".toRegex())
    val symbolCoords = input.findAllMatchesWithCoords("[^.\\d]".toRegex())

    return numberSurroundingRanges.filter { nums ->
        symbolCoords.any { nums.second.contains(it) }
    }.sumOf { it.first }
}

fun day3part2(input: List<String>): Int {
    val numberSurroundingRanges = input.findAllMatchesWithSurroundingBounds("\\d+".toRegex())
    val asteriskCoords = input.findAllMatchesWithCoords("\\*".toRegex())

    val gears = asteriskCoords.mapNotNull { ast ->
        val nums = numberSurroundingRanges.filter { it.second.contains(ast) }
        if(nums.size == 2) nums.first().first to nums.last().first
        else null
    }

    return gears.sumOf { it.first * it.second }
}

fun List<String>.findAllMatchesWithSurroundingBounds(regex: Regex): List<Pair<Int, IntRange2D>> =
    this.flatMapIndexed { lineIdx, line ->
        regex.findAll(line).map {
            it.value.toInt() to IntRange2D(it.range.surroundingRange(line.indices), lineIdx.surroundingRange(this.indices))
        }
    }

fun List<String>.findAllMatchesWithCoords(regex: Regex) =
    this.flatMapIndexed { lineIdx, line ->
        regex.findAll(line).map { it.range.first to lineIdx }
    }

fun Int.surroundingRange(bounds: IntRange): IntRange =
    ((this-1) .. (this+1)).coerceWithin(bounds)

fun IntRange.surroundingRange(bounds: IntRange): IntRange =
    ((this.first-1) .. (this.last+1)).coerceWithin(bounds)

fun IntRange.coerceWithin(bounds: IntRange): IntRange =
    (start).coerceAtLeast(bounds.first) .. (endInclusive).coerceAtMost(bounds.last)

data class IntRange2D(val xRange: IntRange, val yRange: IntRange) {
    fun contains(coord: Pair<Int, Int>): Boolean = coord.first in xRange && coord.second in yRange
}