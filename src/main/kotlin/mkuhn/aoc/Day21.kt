package mkuhn.aoc

import mkuhn.aoc.util.Grid
import mkuhn.aoc.util.Point
import mkuhn.aoc.util.feedbackCycle
import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day21")
    println(day21part1(input))
    println(day21part2(input))
}

fun day21part1(input: List<String>): Int {
    return reachablePlots(input, 64)
}

fun day21part2(input: List<String>): Int =
    2

fun reachablePlots(input: List<String>, steps: Int): Int {
    val grid = Grid(input.map { it.toList() })
    val start = grid.allPoints().first { grid.valueAt(it) == 'S' }

    val positions = setOf(start).feedbackCycle(steps) { points ->
        points.flatMap { p -> grid.cardinalAdjacentTo(p).filter { grid.valueAt(it) != '#' } }.toSet()
    }

    return positions.size
}

fun reachablePlotsInfinite() {

}