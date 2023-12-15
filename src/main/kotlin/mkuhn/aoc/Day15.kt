package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day15")
    println(day15part1(input))
    println(day15part2(input))
}

fun day15part1(input: List<String>): Int = input.first().split(",").sumOf { it.holidayHASH() }

fun day15part2(input: List<String>): Int =
    2

fun String.holidayHASH() = fold(0) { acc, c -> ((acc+c.code)*17)%256 }