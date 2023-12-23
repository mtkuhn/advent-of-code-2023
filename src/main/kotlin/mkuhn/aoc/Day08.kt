package mkuhn.aoc

import mkuhn.aoc.util.leastCommonFactor
import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.repeatForever
import mkuhn.aoc.util.takeWhileInclusive

fun main() {
    val input = readInput("Day08")
    println(day08part1(input))
    println(day08part2(input))
}

fun day08part1(input: List<String>): Int {
    val instructions = input.first()
    val map = input.drop(2).map { line ->
        "(\\w+) = \\((\\w+), (\\w+)\\)".toRegex().matchEntire(line)
            ?.destructured?.let { (name, left, right) -> name to listOf(left, right) }
            ?:error("invalid format")
    }.toMap()

    return MapTraveler("AAA", map).moveAndReturnPathUntil(instructions) { it == "ZZZ" }.size
}

fun day08part2(input: List<String>): Long {
    val instructions = input.first()
    val map = input.drop(2).map { line ->
        "(\\w+) = \\((\\w+), (\\w+)\\)".toRegex().matchEntire(line)
            ?.destructured?.let { (name, left, right) -> name to listOf(left, right) }
            ?:error("invalid format")
    }.toMap()

    val startingNodes = map.keys.filter { it.endsWith("A") }
    val repeats = startingNodes.map { sn -> MapTraveler(sn, map).moveAndFindLoopFrequency(instructions).toLong() }
    return repeats.reduce { acc, f -> leastCommonFactor(acc, f) }
}

class MapTraveler(var currentNode: String, val map: Map<String, List<String>>) {

    fun move(direction: Char): String {
        currentNode =
            if(direction == 'L') map[currentNode]?.first()?:error("unknown node")
            else map[currentNode]?.last()?:error("unknown node")
        return currentNode
    }

    fun moveAndReturnPathUntil(instructions: String, predicate: (String) -> Boolean): List<String> {
        return instructions.asSequence().repeatForever()
            .map { move(it) }
            .takeWhileInclusive { !predicate(it) }
    }

    fun moveAndFindLoopFrequency(instructions: String): Long =
        moveAndReturnPathUntil(instructions) { it.endsWith("Z") }.size.toLong()
}

