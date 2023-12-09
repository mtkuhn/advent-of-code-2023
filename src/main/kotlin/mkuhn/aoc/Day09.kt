package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day09")
    println(day09part1(input))
    println(day09part2(input))
}

fun day09part1(input: List<String>): Int =
    input.sumOf { line -> line.split(" ").map { it.toInt() }.extrapolateNextReading() }

fun day09part2(input: List<String>): Int =
    input.sumOf { line -> line.split(" ").map { it.toInt() }.extrapolatePreviousReading() }

fun List<Int>.extrapolateNextReading(): Int = getAllDifferenceSequences().sumOf { it.last() }

fun List<Int>.extrapolatePreviousReading(): Int =
    getAllDifferenceSequences().map { it.first() }.reversed().fold(0) { acc, d -> (d-acc) }

fun List<Int>.getAllDifferenceSequences(): List<List<Int>> =
    generateSequence(this) { it.differencesBetweenSteps() }.takeWhile { d -> d.any { it != 0 } }.toList()

fun List<Int>.differencesBetweenSteps(): List<Int> = this.windowed(2).map { it.last()-it.first() }