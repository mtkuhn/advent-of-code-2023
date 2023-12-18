package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day16")
    println(day16part1(input))
    println(day16part2(input))
}

fun day16part1(input: List<String>): Int {
    val tiles = input.map { it.toList() }
    val init = BeamNode(null, 0 to 0, BeamDirection.EAST)
    return init.beam(tiles).map { it.point }.toSet().size
}

fun day16part2(input: List<String>): Int {
    val tiles = input.map { it.toList() }
    val beamSizes = tiles.edgeNodes().map { e ->  e.beam(tiles).map { it.point }.toSet().size }
    return beamSizes.max()
}

data class BeamNode(val preceding: BeamNode?, val point: Pair<Int, Int>, val incomingDirection: BeamDirection) {
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

enum class BeamDirection(val moveFrom: (Pair<Int,Int>) -> Pair<Int, Int>) {
    NORTH({ p -> p.first - 1 to p.second }),
    EAST({ p -> p.first to p.second + 1 }),
    SOUTH({ p -> p.first + 1 to p.second }),
    WEST({ p -> p.first to p.second - 1 });

    fun getOpposite(): BeamDirection =
        when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
}

fun List<List<Char>>.containsPoint(p: Pair<Int, Int>) = p.first >= 0 && p.second >=0 &&
        p.first < this.size && p.second < this.first().size

fun List<List<Char>>.edgeNodes(): List<BeamNode> =
    (this.first().indices).map { col -> BeamNode(null, this.indices.first to col, BeamDirection.SOUTH) } +
            (this.first().indices).map { col -> BeamNode(null, this.indices.last to col, BeamDirection.NORTH) } +
            (this.indices).map { row -> BeamNode(null, row to this.first().indices.first, BeamDirection.EAST) }  +
            (this.indices).map { row -> BeamNode(null, row to this.first().indices.last, BeamDirection.WEST) }

fun Char.getNewDirections(enteringDirection: BeamDirection): List<BeamDirection> =
    when(enteringDirection to this) {
        BeamDirection.NORTH to '-' -> listOf(BeamDirection.EAST, BeamDirection.WEST)
        BeamDirection.NORTH to '\\' -> listOf(BeamDirection.WEST)
        BeamDirection.NORTH to '/' -> listOf(BeamDirection.EAST)
        BeamDirection.SOUTH to '-' -> listOf(BeamDirection.EAST, BeamDirection.WEST)
        BeamDirection.SOUTH to '\\' -> listOf(BeamDirection.EAST)
        BeamDirection.SOUTH to '/' -> listOf(BeamDirection.WEST)
        BeamDirection.EAST to '|' -> listOf(BeamDirection.NORTH, BeamDirection.SOUTH)
        BeamDirection.EAST to '\\' -> listOf(BeamDirection.SOUTH)
        BeamDirection.EAST to '/' -> listOf(BeamDirection.NORTH)
        BeamDirection.WEST to '|' -> listOf(BeamDirection.NORTH, BeamDirection.SOUTH)
        BeamDirection.WEST to '\\' -> listOf(BeamDirection.NORTH)
        BeamDirection.WEST to '/' -> listOf(BeamDirection.SOUTH)
        else -> listOf(enteringDirection)
    }