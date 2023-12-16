package mkuhn.aoc

import mkuhn.aoc.util.Direction
import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day16")
    println(day16part1(input))
    println(day16part2(input))
}

fun day16part1(input: List<String>): Int {
    val tiles = input.map { it.toList() }
    val init = BeamNode(null, 0 to 0, Direction.EAST)
    return init.beam(tiles).map { it.point }.toSet().size
}

fun day16part2(input: List<String>): Int {
    val tiles = input.map { it.toList() }
    val beamSizes = tiles.edgeNodes().map { e ->  e.beam(tiles).map { it.point }.toSet().size }
    return beamSizes.max()
}

data class BeamNode(val preceding: BeamNode?, val point: Pair<Int, Int>, val incomingDirection: Direction) {
    fun next(tiles: List<List<Char>>): List<BeamNode> =
        tiles[point.first][point.second]
            .getNewDirections(incomingDirection)
            .map { dir -> BeamNode(this, dir.moveFrom(point), dir) }
            .filter { tiles.containsPoint(it.point) }
            .filter { !it.precedingBeamContains(it) }

    fun beam(tiles: List<List<Char>>): Set<BeamNode> {
        val beamSeq = generateSequence(listOf(this)) { branches -> branches.flatMap { bn -> bn.next(tiles) } }
        return beamSeq.takeWhile { it.isNotEmpty() }.flatten().toSet()
    }

    private fun matches(beamNode: BeamNode) = this.incomingDirection == beamNode.incomingDirection && this.point == beamNode.point

    private fun precedingBeamContains(beamNode: BeamNode): Boolean = preceding?.matches(beamNode)?:false || preceding?.precedingBeamContains(beamNode)?:false
}

fun List<List<Char>>.containsPoint(p: Pair<Int, Int>) = p.first >= 0 && p.second >=0 &&
        p.first < this.size && p.second < this.first().size

fun List<List<Char>>.edgeNodes(): List<BeamNode> =
    (this.first().indices).map { col -> BeamNode(null, this.indices.first to col, Direction.SOUTH) } +
            (this.first().indices).map { col -> BeamNode(null, this.indices.last to col, Direction.NORTH) } +
            (this.indices).map { row -> BeamNode(null, row to this.first().indices.first, Direction.EAST) }  +
            (this.indices).map { row -> BeamNode(null, row to this.first().indices.last, Direction.WEST) }

fun Char.getNewDirections(enteringDirection: Direction): List<Direction> =
    when(enteringDirection to this) {
        Direction.NORTH to '-' -> listOf(Direction.EAST, Direction.WEST)
        Direction.NORTH to '\\' -> listOf(Direction.WEST)
        Direction.NORTH to '/' -> listOf(Direction.EAST)
        Direction.SOUTH to '-' -> listOf(Direction.EAST, Direction.WEST)
        Direction.SOUTH to '\\' -> listOf(Direction.EAST)
        Direction.SOUTH to '/' -> listOf(Direction.WEST)
        Direction.EAST to '|' -> listOf(Direction.NORTH, Direction.SOUTH)
        Direction.EAST to '\\' -> listOf(Direction.SOUTH)
        Direction.EAST to '/' -> listOf(Direction.NORTH)
        Direction.WEST to '|' -> listOf(Direction.NORTH, Direction.SOUTH)
        Direction.WEST to '\\' -> listOf(Direction.NORTH)
        Direction.WEST to '/' -> listOf(Direction.SOUTH)
        else -> listOf(enteringDirection)
    }