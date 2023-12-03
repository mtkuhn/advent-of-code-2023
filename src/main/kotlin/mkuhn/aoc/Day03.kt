package mkuhn.aoc

import mkuhn.aoc.util.readInput
import java.lang.Integer.max

fun main() {
    val input = readInput("Day03")
    println(day3part1(input))
    println(day3part2(input))
}

fun day3part1(input: List<String>): Int {
    return input.flatMapIndexed { lineIdx, line ->
        val numberMatches = "\\d+".toRegex().findAll(line).toList()
        numberMatches.filter { m ->
            val symbolRange = ((m.range.first-1) .. (m.range.last+1)).coerceWithin(line.indices)
            val lineRange = (lineIdx-1 .. lineIdx+1).coerceWithin(input.indices)
            lineRange.map { i -> input[i].substring(symbolRange) }
                .any { it.contains("[^\\.0-9]".toRegex()) }
        }
    }.sumOf { it.value.toInt() }
}

fun day3part2(input: List<String>): Int {
    return 0
}

fun IntRange.coerceWithin(bounds: IntRange): IntRange =
    (start).coerceAtLeast(bounds.first) .. (endInclusive).coerceAtMost(bounds.last)