package mkuhn.aoc

import mkuhn.aoc.util.*
import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    val input = readInput("Day18")
    println(day18part1(input))
    println(day18part2(input))
}

fun day18part1(input: List<String>): Long {
    val lines = input.mapNotNull { TrenchInstruction.fromInput(it) }.toLines()
    return lines.getArea()
}

fun day18part2(input: List<String>): Long {
    val lines = input.mapNotNull { TrenchInstruction.fromInput2(it) }.toLines()
    return lines.getArea()
}

data class TrenchInstruction(val moveType: Char, val moveAmount: Int, val colorCode: String) {
    companion object {
        fun fromInput(input: String) =
            """^(.) (\d+) \(#([0-9a-f]+)\)$""".toRegex().matchEntire(input)?.destructured?.let { (move, count, color) ->
                TrenchInstruction(move.first(), count.toInt(), color) }

        fun fromInput2(input: String) =
            """^(.) (\d+) \(#([0-9a-f]+)\)$""".toRegex().matchEntire(input)?.destructured?.let { (_, color, count) ->
                TrenchInstruction(count.last(), count.substring(0, 5).toInt(16), color) }
    }
}

data class Line(val a: Point, val b: Point) {
    fun length() = abs((b.x-a.x) + (b.y-a.y))
}

fun List<TrenchInstruction>.toLines(): List<Line> =
    this.fold(emptyList()) { list, instruction ->
        val start = list.lastOrNull()?.b?:Point(0,0)
        val end = when(instruction.moveType) {
            'U' -> Point(start.x, start.y+(instruction.moveAmount))
            'D' -> Point(start.x, start.y-(instruction.moveAmount))
            'L' -> Point(start.x-(instruction.moveAmount), start.y)
            'R' -> Point(start.x+(instruction.moveAmount), start.y)
            '3' -> Point(start.x, start.y+(instruction.moveAmount))
            '1' -> Point(start.x, start.y-(instruction.moveAmount))
            '2' -> Point(start.x-(instruction.moveAmount), start.y)
            '0' -> Point(start.x+(instruction.moveAmount), start.y)
            else -> error("invalid instruction")
        }
        list + Line(start, end)
    }

fun List<Line>.getArea(): Long {
    val boundaryArea = this.sumOf { it.length() }
    val shoelace = this.sumOf { line ->
        (line.a.x.toLong()*line.b.y.toLong()) - (line.b.x.toLong()*line.a.y.toLong())
    }.absoluteValue/2
    return shoelace + (boundaryArea/2) + 1
}