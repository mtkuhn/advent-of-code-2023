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
    val mirrors = grids.flatMap { it.findMirrors() }
    return mirrors.filter { it.second == 'V' }.sumOf { it.first } + (mirrors.filter { it.second == 'H' }.sumOf { it.first }*100)
}

fun day13part2(input: List<String>): Int {
    val grids = input.splitList("").map { Grid(it.map { s -> s.toList() }) }
    val mirrors = grids.flatMap { it.findSmudgedMirrors().toSet() - it.findMirrors().toSet() }
    return mirrors.filter { it.second == 'V' }.sumOf { it.first } + (mirrors.filter { it.second == 'H' }.sumOf { it.first }*100)
}

fun Grid<Char>.findSmudgedMirrors(): List<Pair<Int, Char>> =
    this.allPoints().flatMap { p -> this.withUpdatedValueAt(p, if(this.valueAt(p) == '#') '.' else '#').findMirrors() }

fun Grid<Char>.findMirrors(): List<Pair<Int, Char>> =
    this.findHorizontalMirrors().map { it to 'H' } + findVerticalMirrors().map { it to 'V' }

fun Grid<Char>.findVerticalMirrors(): List<Int> =
    xBounds().fold(yBounds().toList()) { acc, i -> this.row(i).findMirrorAfterIndexes(acc) }

fun Grid<Char>.findHorizontalMirrors(): List<Int> =
    yBounds().fold(xBounds().toList()) { acc, i -> this.col(i).findMirrorAfterIndexes(acc) }

fun List<Char>.findMirrorAfterIndexes(possMirrorAfterIndexes: List<Int>): List<Int> =
    possMirrorAfterIndexes.filter { m ->
        val len = minOf(m, size-m)
        len > 0 && subList(m-len, m) == subList(m, m+len).reversed() }