package mkuhn.aoc

import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.splitList
import mkuhn.aoc.util.transpose

fun main() {
    val input = readInput("Day14")
    println(day14part1(input))
    println(day14part2(input))
}

fun day14part1(input: List<String>): Int =
    input.map { it.toList() }.transpose()
        .map { col -> col.splitList('#').flatMap { it.sortedDescending().plus('#') }.dropLast(1) }
        .map { it.toList() }.transpose()
        .reversed().mapIndexed { idx, c -> (idx+1) * c.count { it == 'O' } }
        .sum()

fun day14part2(input: List<String>): Int =
    2