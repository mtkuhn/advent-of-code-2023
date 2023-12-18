package mkuhn.aoc

import mkuhn.aoc.util.*

fun main() {
    val input = readInput("Day17")
    println(day17part1(input))
    println(day17part2(input))
}

fun day17part1(input: List<String>): Int =
    traverseGraphWithCondition(input) {node: CrucibleNode, dir: Direction ->
        node.direction != dir || node.directionCount <= 2
    }

fun day17part2(input: List<String>): Int =
    traverseGraphWithCondition(input) { node: CrucibleNode, dir: Direction ->
        if(node.directionCount == 0 || node.direction == null) true
        else if(node.direction != dir && node.directionCount < 4) false
        else if(node.direction == dir && node.directionCount >= 10) false
        else true
    }

fun traverseGraphWithCondition(input: List<String>, neighborCondition: (CrucibleNode, Direction) -> Boolean): Int {
    val grid = Grid(input.map { l -> l.map { it.digitToInt() } })
    val graph = CrucibleGraph(grid, neighborCondition)
    val originPoint = Point(0, 0)
    val destinationPoint = Point(grid.xBounds().last, grid.yBounds().last)
    val origin = CrucibleNode(originPoint, null, 0, grid.valueAt(originPoint))
    val shortPath = graph.findShortestPath(origin) { node: CrucibleNode -> node.point == destinationPoint }
    return shortPath.cost
}

data class CrucibleNode(val point: Point, val direction: Direction?, val directionCount: Int, override val cost: Int): GraphNode(cost)

class CrucibleGraph(private val grid: Grid<Int>, private val neighborCondition: (CrucibleNode, Direction) -> Boolean): Graph<CrucibleNode>() {
    override fun getNeighbors(node: CrucibleNode): List<CrucibleNode> =
        Direction.values()
            .filter { d -> d != node.direction?.getOpposite() }
            .filter { d -> neighborCondition(node, d) }
            .map { d -> d to d.moveFrom(node.point) }
            .filter { grid.isInBounds(it.second) }
            .map { step -> CrucibleNode(step.second, step.first, if(node.direction == step.first) node.directionCount+1 else 1, grid.valueAt(step.second)) }
}

enum class Direction(val moveFrom: (Point) -> Point) {
    NORTH({ p -> Point(p.x, p.y-1) }),
    EAST({ p -> Point(p.x+1, p.y) }),
    SOUTH({ p -> Point(p.x, p.y+1) }),
    WEST({ p -> Point(p.x-1, p.y) });

    fun getOpposite(): Direction =
        when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
}