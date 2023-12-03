package mkuhn.aoc

import mkuhn.aoc.util.readInput
import java.lang.Integer.max

fun main() {
    val input = readInput("Day03")
    println(day3part1(input))
    println(day3part2(input))
}

fun day3part1(input: List<String>): Int {
    val numberSurroundingRanges = input.flatMapIndexed { lineIdx, line ->
        "\\d+".toRegex().findAll(line).map {
            it.value to (it.range.surroundingRange(line.indices) to lineIdx.surroundingRange(input.indices))
        }
    }

    val symbolCoords = input.flatMapIndexed { lineIdx, line ->
        "[^\\.\\d]".toRegex().findAll(line).map { it.range.first to lineIdx }
    }

    return numberSurroundingRanges.filter { nums ->
        symbolCoords.any { it.coordInLineAndRange(nums.second) }
    }.sumOf { it.first.toInt() }
}

fun day3part2(input: List<String>): Int {
    val numberSurroundingRanges = input.flatMapIndexed { lineIdx, line ->
        "\\d+".toRegex().findAll(line).map {
            it.value to (it.range.surroundingRange(line.indices) to lineIdx.surroundingRange(input.indices))
        }
    }

    val asteriskCoords = input.flatMapIndexed { lineIdx, line ->
        "\\*".toRegex().findAll(line).map { it.range.first to lineIdx }
    }

    val gears = asteriskCoords.mapNotNull { ast ->
        val nums = numberSurroundingRanges.filter { ast.coordInLineAndRange(it.second) }
        if(nums.size == 2) nums
        else null
    }

    return gears.sumOf { it.first().first.toInt() * it.last().first.toInt() }
}

fun Int.surroundingRange(bounds: IntRange): IntRange =
    ((this-1) .. (this+1)).coerceWithin(bounds)

fun IntRange.surroundingRange(bounds: IntRange): IntRange =
    ((this.first-1) .. (this.last+1)).coerceWithin(bounds)

fun IntRange.coerceWithin(bounds: IntRange): IntRange =
    (start).coerceAtLeast(bounds.first) .. (endInclusive).coerceAtMost(bounds.last)

fun Pair<Int, Int>.coordInLineAndRange(boundingBox: Pair<IntRange, IntRange>): Boolean =
    this.first in boundingBox.first && this.second in boundingBox.second