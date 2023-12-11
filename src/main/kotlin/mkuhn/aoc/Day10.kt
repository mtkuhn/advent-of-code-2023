package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day10")
    println(day10part1(input))
    println(day10part2(input))
}

fun day10part1(input: List<String>): Int {
    val grid = input.map { it.toList() }
    val startPos = input.indexOfFirst { l -> l.contains('S') }.let { i -> i to grid[i].indexOf('S') }
    val initPath = grid.adjacentTo(startPos).first()
    val path = grid.pipeLoopSequence(initPath).takeWhile { it.position != startPos }.toList()
    return (path.size/2) + 1
}

fun day10part2(input: List<String>): Int {
    val grid = input.map { it.toList() }
    val startPos = input.indexOfFirst { l -> l.contains('S') }.let { i -> i to grid[i].indexOf('S') }
    val initPath = grid.adjacentTo(startPos).first()
    val path = grid.pipeLoopSequence(initPath).takeWhile { it.position != startPos }.toMutableList()

    val startPipe = grid.getStartPipeWithCorrectType(initPath, path.last())
    path += startPipe

    val pathPositions = path.map { p -> p.position }.toSet()

    val x = path.filter { it.char in ("|JL") }
        .groupBy { it.position.first }
        .flatMap { gr ->
            val ranges = gr.value
                .sortedBy { it.position.second }
                .chunked(2)
                .map { it.first().position.second .. it.last().position.second }
            ranges.flatMap { v -> v.map { gr.key to it } }.filter { it !in pathPositions }
        }.toSet()

    return x.size
}

data class Pipe(val char: Char, val position: Pair<Int, Int>, val fromDirection: Direction)

enum class Direction(val moveToward: (Pair<Int,Int>) -> Pair<Int, Int>) {
    NORTH({p -> p.first-1 to p.second}),
    EAST({p -> p.first to p.second+1}),
    SOUTH({p -> p.first+1 to p.second}),
    WEST({p -> p.first to p.second-1});

    fun getOpposite(): Direction =
        when(this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }

    companion object {
        val pipeMovementMap = PipeType.values().flatMap { t ->
            t.directions.map { d -> (t.char to d) to t.directions.first { it != d } }
        }.toMap()
    }
}

enum class PipeType(val char: Char, val directions: List<Direction>) {
    VERTICAL('|', listOf(Direction.NORTH, Direction.SOUTH)),
    HORIZONTAL('-', listOf(Direction.EAST, Direction.WEST)),
    SOUTHWEST('L', listOf(Direction.NORTH, Direction.EAST)),
    SOUTHEAST('J', listOf(Direction.NORTH, Direction.WEST)),
    NORTHEAST('7', listOf(Direction.SOUTH, Direction.WEST)),
    NORTHWEST('F', listOf(Direction.SOUTH, Direction.EAST)),
    START('S', Direction.values().toList())
}

fun List<List<Char>>.adjacentTo(point: Pair<Int, Int>): List<Pipe> =
    Direction.values().mapNotNull { dir ->
        dir.moveToward(point).takeIf { isValidPipe(it, dir) }
            ?.let { Pipe(this.get(it), it, dir.getOpposite()) }
    }

fun List<List<Char>>.get(point: Pair<Int, Int>): Char = this[point.first][point.second]

fun List<List<Char>>.isValidPipe(point: Pair<Int, Int>, dir: Direction): Boolean =
    getOrNull(point.first)?.getOrNull(point.second) != null &&
            PipeType.values().filter { it.directions.contains(dir.getOpposite()) }.map { it.char }.contains(get(point))

fun List<List<Char>>.pipeLoopSequence(start: Pipe) =
    generateSequence(start) { p -> this.getNextPipePosition(p) }

fun List<List<Char>>.getNextPipePosition(p: Pipe): Pipe {
    val dir = Direction.pipeMovementMap[p.char to p.fromDirection]?:error("dead end pipe ${p.char} ${p.fromDirection} | $p")
    val newPos = dir.moveToward(p.position)
    return Pipe(this.get(newPos), newPos, dir.getOpposite())
}

fun List<List<Char>>.getStartPipeWithCorrectType(pathStart: Pipe, pathEnd: Pipe): Pipe {
    var startPipe = this.getNextPipePosition(pathEnd)
    val startPipeType = PipeType.values().first {
        it.char != 'S' &&
                it.directions.contains(startPipe.fromDirection) &&
                it.directions.contains(pathStart.fromDirection.getOpposite())
    }
    return Pipe(startPipeType.char, startPipe.position, startPipe.fromDirection)
}

