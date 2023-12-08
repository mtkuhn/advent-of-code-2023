package mkuhn.aoc

import mkuhn.aoc.util.readInput

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

    val mapTraveler = MapTraveler("AAA", map)
    return mapTraveler.moveUntilNode(instructions, "ZZZ").size
}

fun day08part2(input: List<String>): Int =
    2

//Thank you, stack overflow
fun <T> Sequence<T>.repeatForever() = generateSequence(this) { it }.flatten()

data class MapTraveler(var currentNode: String, val map: Map<String, List<String>>) {

    fun move(direction: Char): String {
        val newNode =
            if(direction == 'L') map[currentNode]?.first()?:error("unknown node")
            else map[currentNode]?.last()?:error("unknown node")

        //todo: println("move from $currentNode to $newNode")

        currentNode = newNode

        return newNode
    }

    fun moveUntilNode(instructions: String, endNode: String): List<String> {
        return instructions.asSequence().repeatForever().map { move(it) }.takeWhile { it != "ZZZ" }.toList() + "ZZZ"
    }
}

