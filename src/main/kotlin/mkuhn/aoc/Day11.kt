package mkuhn.aoc

import mkuhn.aoc.util.Point
import mkuhn.aoc.util.progressBetween
import mkuhn.aoc.util.readInput
import kotlin.math.abs

fun main() {
    val input = readInput("Day11")
    println(day11part1(input))
    println(day11part2(input))
}

fun day11part1(input: List<String>): Long = input.findDistanceSums(2)

fun day11part2(input: List<String>): Long = input.findDistanceSums(1000000)

fun day11part2test(input: List<String>): Long = input.findDistanceSums(100)

fun List<String>.findDistanceSums(expansionFactor: Long): Long {
    val emptyRowIdxs = this.indices.filter { idx -> this[idx].all { it == '.' } }
    val emptyColIdxs = this.first().indices.filter { idx -> this.map { it[idx] }.all { it == '.' } }
    val galaxyPointPairs = this.allPoints().filter { this[it.x][it.y] == '#' }.toSet().joinToSelf()
    return galaxyPointPairs.sumOf {
        it.first().manhattanDistWithExpansion(it.last(), emptyRowIdxs, emptyColIdxs, expansionFactor)
    }
}

fun List<String>.allPoints() = this.indices.flatMap { x -> (this.first().indices).map { y -> Point(x, y) } }

fun <T> Set<T>.joinToSelf() = this.flatMap { a -> this.filter { it != a }.map { setOf(it, a) } }.toSet()

fun Point.manhattanDistWithExpansion(p: Point, expRowIdxs: List<Int>, expColIdxs: List<Int>, expFactor: Long): Long {
    return abs(this.x-p.x) + abs(this.y-p.y) +
            (expRowIdxs.count { it in (this.x.progressBetween(p.x)) } * (expFactor-1)) +
            (expColIdxs.count { it in (this.y.progressBetween(p.y)) } * (expFactor-1))
}