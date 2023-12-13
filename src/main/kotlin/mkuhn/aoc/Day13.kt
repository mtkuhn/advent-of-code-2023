package mkuhn.aoc

import mkuhn.aoc.util.Grid
import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.splitList

fun main() {
    val input = readInput("Day13")
    println(day13part1(input))
    println(day13part2(input))
}

fun day13part1(input: List<String>): Int {
    val grids = input.splitList("").map { Grid(it.map { s -> s.toList() }) }
    val verticals = grids.mapNotNull { it.findVerticalMirror() }
    val horizontals = grids.mapNotNull { it.findHorizontalMirror() }
    return verticals.sum() + (horizontals.sum()*100)
}

fun day13part2(input: List<String>): Int =
    2

fun Grid<Char>.findVerticalMirror(): Int? =
    xBounds().fold(yBounds().toList()) { acc, i ->
        this.row(i).findMirrorAfterIndexes(acc)
    }.firstOrNull()

fun Grid<Char>.findHorizontalMirror(): Int? =
    yBounds().fold(xBounds().toList()) { acc, i ->
        this.col(i).findMirrorAfterIndexes(acc)
    }.firstOrNull()

fun List<Char>.findMirrorAfterIndexes(possMirrorAfterIndexes: List<Int>): List<Int> =
    possMirrorAfterIndexes.filter { m ->
        val len = minOf(m, size-m)
        len > 0 && subList(m-len, m) == subList(m, m+len).reversed()
    }