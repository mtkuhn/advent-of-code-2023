package mkuhn.aoc

import mkuhn.aoc.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInput("Day18")
    println(day18part1(input))
    println(day18part2(input))
}

fun day18part1(input: List<String>): Int {
    val lines = input.mapNotNull { TrenchInstruction.fromInput(it) }.toLines()
    return lines.getArea()
}

fun day18part2(input: List<String>): Int =
    2

data class TrenchInstruction(val moveType: Char, val moveAmount: Int, val colorCode: String) {
    companion object {
        fun fromInput(input: String) =
            """^(.) (\d+) \(#([0-9a-f]+)\)$""".toRegex().matchEntire(input)?.destructured?.let { (move, count, color) ->
                TrenchInstruction(move.first(), count.toInt(), color) }
    }
}

data class ColoredLine(val a: Point, val b: Point, val color: String) {
    fun intersectsY(y: Int): Boolean = y in this.a.y.progressBetween(this.b.y)
    fun intersectsX(x: Int): Boolean = x in this.a.x.progressBetween(this.b.x)
    fun getPoints() = (a.x.progressBetween(b.x)).flatMap { x -> (a.y.progressBetween(b.y)).map { y -> Point(x, y) } }
    fun isVertical() = (a.x == b.x)
    fun isHorizontal() = (a.y == b.y)
}

fun List<TrenchInstruction>.toLines(): List<ColoredLine> =
    this.fold(emptyList()) { list, instruction ->
        val start = list.lastOrNull()?.b?:Point(0,0)
        val end = when(instruction.moveType) {
            'U' -> Point(start.x, start.y-(instruction.moveAmount))
            'D' -> Point(start.x, start.y+(instruction.moveAmount))
            'L' -> Point(start.x-(instruction.moveAmount), start.y)
            'R' -> Point(start.x+(instruction.moveAmount), start.y)
            else -> error("invalid instruction")
        }
        list + ColoredLine(start, end, instruction.colorCode)
    }

fun List<ColoredLine>.getNorthFacingCorners(): List<Point> =
    this.filter { it.isVertical() }.map { Point(it.a.x, min(it.a.y, it.b.y)) }

fun List<ColoredLine>.getSouthFacingCorners(): List<Point> =
    this.filter { it.isVertical() }.map { Point(it.a.x, max(it.a.y, it.b.y)) }

fun List<ColoredLine>.getArea(): Int {
    val vertical = this.filter { it.isVertical() }
    val checkPoints = vertical.flatMap { it.getPoints() } - vertical.getSouthFacingCorners().toSet()

    val inner = checkPoints
        .groupBy { it.y }
        .flatMap { gr ->
            gr.value.sortedBy { it.x }
                .chunked(2)
                .map { it.first().x .. it.last().x }
                .flatMap { v -> v.map { Point(it, gr.key) } }
        }.toSet()
    val outer = this.flatMap { it.getPoints() }.toSet()

    return (inner + outer).toSet().count()
}

fun List<Point>.mergePointsUntilSouthFacing(southCorners: List<Point>) =
    this.sortedBy { it.x }
        .fold(mutableListOf<MutableList<Point>>()) { acc, p ->
            if(acc.isEmpty()) acc += mutableListOf(p)
            else if (acc.last().isEmpty()) acc[acc.lastIndex] += p
            else if(acc.last().last() !in southCorners) acc[acc.lastIndex] += p
            else {
                acc[acc.lastIndex] += p
                acc += mutableListOf<Point>()
            }
            acc
        }.filter { it.isNotEmpty() }

fun List<ColoredLine>.getXBounds() = this.minOf { min(it.a.x, it.b.x) } .. this.maxOf { max(it.a.x, it.b.x) }
fun List<ColoredLine>.getYBounds() = this.minOf { min(it.a.y, it.b.y) } .. this.maxOf { max(it.a.y, it.b.y) }
fun List<IntRange>.mergeConnected() =
    this.fold(mutableListOf<IntRange>()) { acc, r ->
        if(acc.isEmpty()) acc.add(r)
        else if(acc.last().last == r.first) {
            val new = acc.last().first .. r.last
            acc.removeLast()
            acc.add(new)
        }
        else acc.add(r)
        acc
    }

fun List<ColoredLine>.getAreaX(): Int =
    getYBounds().sumOf { y ->
        val ranges = this
            .filter { it.intersectsY(y) }
            .map { it.a.x.orderedRangeBetween(it.b.x) }
            .sortedWith(compareBy({it.first}, { it.last}))
            .mergeConnected()
            .chunked(2)
            .map { it.first().first..it.last().last }
        ranges.sumOf { it.last - it.first + 1 }
    }