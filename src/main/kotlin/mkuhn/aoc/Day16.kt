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
    val beam = beamFrom(BeamNode(0 to 0, listOf(Direction.EAST)), tiles)
    return beam.map { it.fromPoint }.toSet().size
}

fun day16part2(input: List<String>): Int {
    val tiles = input.map { it.toList() }
    val beams = tiles.edgeNodes().map { initNode ->
        beamFrom(initNode, tiles).map { it.fromPoint }.toSet().size }
    return beams.max()
}

data class BeamNode(val fromPoint : Pair<Int, Int>, val directions: List<Direction>) {
    fun next(tiles: List<List<Char>>): List<BeamNode> =
        directions.map { dir -> dir.moveFrom(fromPoint) to dir }
            .filter { tiles.containsPoint(it.first) }
            .map { pd -> BeamNode(pd.first, tiles[pd.first.first][pd.first.second].getNewDirections(pd.second)) }
}

fun beamFrom(initNode: BeamNode, tiles: List<List<Char>>): Set<BeamNode> {
    val nodes = mutableSetOf(initNode)
    while(true) {
        val expanded = nodes.flatMap { it.next(tiles) }
        val netNew = expanded.minus(nodes)
        if(netNew.isEmpty()) break
        nodes += netNew
    }
    return nodes
}

fun List<List<Char>>.containsPoint(p: Pair<Int, Int>) = p.first >= 0 && p.second >=0 &&
        p.first < this.size && p.second < this.first().size

fun List<List<Char>>.edgeNodes(): List<BeamNode> =
    (this.first().indices).map { col -> BeamNode(this.indices.first to col, listOf(Direction.SOUTH)) } +
            (this.first().indices).map { col -> BeamNode(this.indices.last to col, listOf(Direction.NORTH)) } +
            (this.indices).map { row -> BeamNode(row to this.first().indices.first, listOf(Direction.EAST)) }  +
            (this.indices).map { row -> BeamNode(row to this.first().indices.last, listOf(Direction.WEST)) }

fun Char.getNewDirections(enteringDirection: Direction): List<Direction> =
    if(enteringDirection == Direction.NORTH && this == '-') listOf(Direction.EAST, Direction.WEST)
    else if(enteringDirection == Direction.NORTH && this == '\\') listOf(Direction.WEST)
    else if(enteringDirection == Direction.NORTH && this == '/') listOf(Direction.EAST)
    else if(enteringDirection == Direction.SOUTH && this == '-') listOf(Direction.EAST, Direction.WEST)
    else if(enteringDirection == Direction.SOUTH && this == '\\') listOf(Direction.EAST)
    else if(enteringDirection == Direction.SOUTH && this == '/') listOf(Direction.WEST)
    else if(enteringDirection == Direction.EAST && this == '|') listOf(Direction.NORTH, Direction.SOUTH)
    else if(enteringDirection == Direction.EAST && this == '\\') listOf(Direction.SOUTH)
    else if(enteringDirection == Direction.EAST && this == '/') listOf(Direction.NORTH)
    else if(enteringDirection == Direction.WEST && this == '|') listOf(Direction.NORTH, Direction.SOUTH)
    else if(enteringDirection == Direction.WEST && this == '\\') listOf(Direction.NORTH)
    else if(enteringDirection == Direction.WEST && this == '/') listOf(Direction.SOUTH)
    else listOf(enteringDirection)