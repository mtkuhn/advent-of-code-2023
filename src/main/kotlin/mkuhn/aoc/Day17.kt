package mkuhn.aoc

import mkuhn.aoc.util.Grid
import mkuhn.aoc.util.Point
import mkuhn.aoc.util.readInput
import java.util.*

fun main() {
    val input = readInput("Day17")
    println(day17part1(input))
    println(day17part2(input))
}

fun day17part1(input: List<String>): Int {
    val startTime = System.currentTimeMillis()
    val grid = Grid(input.map { l -> l.map { it.digitToInt() } })
    val condition = { edge: Edge, dir: Direction ->
        edge.direction != dir || edge.directionCount <= 2
    }
    val shortPath = grid.findShortestPathTo(condition)

    println("Time: ${System.currentTimeMillis()-startTime}")
    return shortPath.heatLoss
}

fun day17part2(input: List<String>): Int {
    val startTime = System.currentTimeMillis()
    val grid = Grid(input.map { l -> l.map { it.digitToInt() } })
    val condition = { edge: Edge, dir: Direction ->
        if(edge.direction != dir && edge.directionCount < 4) false
        else if(edge.direction == dir && edge.directionCount >= 10) false
        else true
    }
    val shortPath = grid.findShortestPathTo(condition)

    println("Time: ${System.currentTimeMillis()-startTime}")
    return shortPath.heatLoss
}

data class Edge(val point: Point, val direction: Direction, val directionCount: Int)

data class Path(val edge: Edge, val heatLoss: Int) {
    fun getPossibleEdges(heatMap: Grid<Int>, crucibleCondition: (Edge, Direction) -> Boolean): List<Edge> =
        Direction.values()
            .filter { d -> d != edge.direction.getOpposite() }
            .filter { d -> crucibleCondition(edge, d) }
            .map { d -> d to d.moveFrom(edge.point) }
            .filter { heatMap.isInBounds(it.second) }
            .map { step -> Edge(step.second, step.first, if(edge.direction == step.first) edge.directionCount+1 else 1) }
}

fun Grid<Int>.findShortestPathTo(crucibleCondition: (Edge, Direction) -> Boolean): Path {
    val startPoint = Point(0, 0)
    val goal = Point(xBounds().last, yBounds().last)
    val startEdge = Edge(startPoint, Direction.NORTH, 0)
    val seen = mutableSetOf<Edge>()
    val pathQueue = PriorityQueue<Path>() { a, b -> a.heatLoss - b.heatLoss }

    seen += startEdge
    pathQueue += Path(startEdge, 0)

    while(pathQueue.isNotEmpty()) {
        val curr = pathQueue.poll()

        if(curr.edge.point == goal) return curr

        val nextEdges = curr.getPossibleEdges(this, crucibleCondition).filter { it !in seen }
        val nextPaths = nextEdges.map { Path(it, curr.heatLoss + valueAt(it.point)) }

        seen += nextEdges
        pathQueue += nextPaths
        pathQueue -= curr
    }

    error("no path found")
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