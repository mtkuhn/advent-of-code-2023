package mkuhn.aoc.util

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    fun manhattanDistance(p: Point) = abs(this.x-p.x) + abs(this.y-p.y)
}

data class LongPoint(val x: Long, val y: Long)

open class Grid<T>(private val grid: List<List<T>>) {

    fun allNorth(pos: Point) =
        (0 until pos.y).map { Point(pos.x, it) }.sortedByDescending { it.y }

    fun allSouth(pos: Point) =
        (pos.y+1 until grid.first().size).map { Point(pos.x, it) }.sortedBy { it.y }

    fun allWest(pos: Point) =
        (0 until pos.x).map { Point(it, pos.y) }.sortedByDescending { it.x }

    fun allEast(pos: Point) =
        (pos.x+1 until grid.size).map { Point(it, pos.y) }.sortedBy { it.x }

    fun row(i: Int): List<T> = grid[i]

    fun col(i: Int): List<T> = grid.map { it[i] }

    fun isInBounds(pos: Point) = pos.x in xBounds() && pos.y in yBounds()

    fun isEdge(pos: Point) =
        when {
            pos.x == 0 -> true
            pos.y == 0 -> true
            pos.x == xBounds().last -> true
            pos.y == yBounds().last -> true
            else -> false
        }

    fun allPoints() = grid.indices.flatMap { x ->
        (grid.first().indices).map { y -> Point(x, y) }
    }

    fun cardinalAdjacentTo(pos: Point) =
        listOf(Point(pos.x-1, pos.y), Point(pos.x+1, pos.y), Point(pos.x, pos.y-1), Point(pos.x, pos.y+1))
            .filter { isInBounds(it) }

    fun valueAt(pos: Point) = grid[pos.x][pos.y]

    fun xBounds() = grid.indices

    fun yBounds() = grid.first().indices

    fun withUpdatedValueAt(pos: Point, new: T): Grid<T> =
        Grid(grid.map { it.toMutableList() }.toMutableList().apply { this[pos.x][pos.y] = new })
}