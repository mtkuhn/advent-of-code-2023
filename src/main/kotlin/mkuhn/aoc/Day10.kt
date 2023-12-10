package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day10")
    println(day10part1(input))
    println(day10part2(input))
}

fun day10part1(input: List<String>): Int {
    val grid = input.map { it.toList() }
    val directionalMap = PipeTypes.values().flatMap { t ->
        t.directions.map { d -> (t.char to d) to t.directions.first { it != d } }
    }.toMap()
    val start = input.indexOfFirst { l -> l.contains('S') }
        .let { i -> i to grid[i].indexOf('S') }

    val pipeSequence = generateSequence(grid.adjacentTo(start).first()) { p ->
        val dir = directionalMap.get(p.char to p.fromDirection)!!
        val newPos = dir.moveToward(p.position)
        Pipe(grid.get(newPos), newPos, dir.getOpposite())
    }

    val path = pipeSequence.takeWhile { it.position != start }.toList()

    return (path.size/2) + 1
}

fun day10part2(input: List<String>): Int =
    2

data class Pipe(val char: Char, val position: Pair<Int, Int>, val fromDirection: Direction)

enum class Direction(val allowedFrom: String, val allowedInto: String, val moveToward: (Pair<Int,Int>) -> Pair<Int, Int>) {
    NORTH("|LJ", "|7FS", {p -> p.first-1 to p.second}),
    SOUTH("|7F", "|LJS", {p -> p.first+1 to p.second}),
    EAST("-LF", "-J7S", {p -> p.first to p.second+1}),
    WEST("-J7", "-LFS", {p -> p.first to p.second-1});

    fun getOpposite(): Direction =
        when(this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }

}

enum class PipeTypes(val char: Char, val directions: List<Direction>) {
    VERTICAL('|', listOf(Direction.NORTH, Direction.SOUTH)),
    HORIZONTAL('-', listOf(Direction.EAST, Direction.WEST)),
    SOUTHWEST('L', listOf(Direction.NORTH, Direction.EAST)),
    SOUTHEAST('J', listOf(Direction.NORTH, Direction.WEST)),
    NORTHEAST('7', listOf(Direction.SOUTH, Direction.WEST)),
    NORTHWEST('F', listOf(Direction.SOUTH, Direction.EAST))
}

fun List<List<Char>>.adjacentTo(point: Pair<Int, Int>): List<Pipe> =
    Direction.values().mapNotNull { dir ->
        dir.moveToward(point).takeIf { isValidPipe(it, dir.allowedInto) }?.let { Pipe(this.get(it), it, dir.getOpposite()) }
    }

fun List<List<Char>>.get(point: Pair<Int, Int>): Char = this[point.first][point.second]

fun List<List<Char>>.isValidPipe(point: Pair<Int, Int>, allowedTypes: String): Boolean =
    getOrNull(point.first)?.getOrNull(point.second) != null && allowedTypes.contains(this[point.first][point.second])
