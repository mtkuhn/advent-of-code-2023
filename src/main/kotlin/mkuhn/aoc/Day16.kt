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
    val beam = initFromExclusive(BeamNode(0 to -1, listOf(Direction.EAST)), tiles)
    return beam.map { it.fromPoint }.toSet().size
}

fun day16part2(input: List<String>): Int {
    val tiles = input.map { it.toList() }
    val beams = tiles.outsideEdgeNodes().map { initNode ->
        initFromExclusive(initNode, tiles).map { it.fromPoint }.toSet().size }
    return beams.max()
}

data class BeamNode(val fromPoint : Pair<Int, Int>, val directions: List<Direction>) {
    fun next(tiles: List<List<Char>>): List<BeamNode> =
        directions.map { dir -> dir.moveFrom(fromPoint) to dir }
            .filter { tiles.containsPoint(it.first) }
            .map { pd -> BeamNode(pd.first, tiles[pd.first.first][pd.first.second].getNewDirections(pd.second)) }
}

fun initFromExclusive(initNode: BeamNode, tiles: List<List<Char>>): Set<BeamNode> {
    val nodes = mutableSetOf(initNode)
    while(true) {
        val expanded = nodes.flatMap { it.next(tiles) }
        val netNew = expanded.minus(nodes)
        if(netNew.isEmpty()) break
        nodes += netNew
    }
    return nodes.minus(initNode)
}

fun List<List<Char>>.containsPoint(p: Pair<Int, Int>) = p.first >= 0 && p.second >=0 &&
        p.first < this.size && p.second < this.first().size

fun List<List<Char>>.outsideEdgeNodes(): List<BeamNode> =
    (this.first().indices).map { col -> BeamNode(this.indices.first-1 to col, listOf(Direction.SOUTH)) } +
            (this.first().indices).map { col -> BeamNode(this.indices.last+1 to col, listOf(Direction.NORTH)) } +
            (this.indices).map { row -> BeamNode(row to this.first().indices.first-1, listOf(Direction.EAST)) }  +
            (this.indices).map { row -> BeamNode(row to this.first().indices.last+1, listOf(Direction.WEST)) }

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