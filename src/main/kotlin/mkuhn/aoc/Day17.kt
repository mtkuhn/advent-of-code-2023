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
    val heatMap = Grid(input.map { l -> l.map { it.digitToInt() } })
    val startPoint = Point(0, 0)
    val goal = Point(heatMap.xBounds().last, heatMap.yBounds().last)
    val startPath = HeatMapPath(startPoint, emptyList(), 0, 10000)
    val estimateMap = heatMap.allPoints().associateWith {
        estimateMinimumRemainingHeatLoss(it, heatMap) + it.manhattanDistance(goal)
    }
    println("estimate done: $estimateMap")

    val paths = PriorityQueue<HeatMapPath> { a, b -> a.score - b.score }
    paths.add(startPath)

    val best = paths.findBestPath(heatMap, estimateMap, goal)

    return best.heatLoss
}

fun day17part2(input: List<String>): Int =
    2

fun estimateMinimumRemainingHeatLoss(point: Point, heatMap: Grid<Int>): Int {
    //val xSum = heatMap.allEast(point).sumOf { p -> (heatMap.allSouth(p)+p).minOfOrNull { heatMap.valueAt(it) }?:0 }
    //val ySum = heatMap.allSouth(point).sumOf { p -> (heatMap.allEast(p)+p).minOfOrNull { heatMap.valueAt(it) }?:0 }

    val xSum = (point.x+1 .. heatMap.xBounds().last).sumOf { x -> heatMap.allPoints().filter { it.x == x }.minOfOrNull { heatMap.valueAt(it) }?:0 }
    val ySum = (point.y+1 .. heatMap.yBounds().last).sumOf { y -> heatMap.allPoints().filter { it.y == y }.minOfOrNull { heatMap.valueAt(it) }?:0 }

    return xSum + ySum
}

data class HeatMapPath(val point: Point, val directions: List<Direction>, val heatLoss: Int, val score: Int) {
    companion object {
        fun score(heatLoss: Int, point: Point, estimateMap: Map<Point, Int>): Int {
            return heatLoss + (estimateMap[point]?:0)
        }
    }

    override fun toString() = "[path loss=$heatLoss, s=$score, p=$point, d=${directions.lastOrNull()}]"
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

fun PriorityQueue<HeatMapPath>.findBestPath(heatMap: Grid<Int>, estimateMap: Map<Point, Int>, goal: Point): HeatMapPath {
    var i = 0 //todo
    while(i < 1000000) {
        i++
        val best = this.poll()
        if(i%10000 == 0) println("best: $best | totalPaths: ${this.size}")
        val new = best.getPossibleMoves(heatMap, estimateMap, goal)
        val finished = new.filter { it.point == goal }.minByOrNull { it.heatLoss }

        if(finished != null) return finished
        else { new.forEach { this.offer(it) } }
    }
    error("too many iterations")
}

fun HeatMapPath.getPossibleMoves(heatMap: Grid<Int>, estimateMap: Map<Point, Int>, goal: Point): List<HeatMapPath> {
    val validDirections = Direction.values()
        .filter { d -> d != directions.lastOrNull()?.getOpposite() }
        .filter { d -> directions.size < 3 || !(directions.takeLast(3).all { it == d }) }

    val validPoints = validDirections
        .map { d -> d to d.moveFrom(point) }
        .filter { heatMap.isInBounds(it.second) }

    return validPoints.map { step ->
        HeatMapPath(step.second,
            directions.plus(step.first).takeLast(3),
            heatLoss + heatMap.valueAt(step.second),
            HeatMapPath.score(heatLoss + heatMap.valueAt(step.second), step.second, estimateMap))
    }
}