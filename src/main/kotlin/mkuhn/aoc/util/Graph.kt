package mkuhn.aoc.util

import java.util.*

abstract class GraphNode(open val cost: Int)
class GraphPath<T:GraphNode>(val edge: T, val cost: Int)

abstract class Graph<T:GraphNode> {

    abstract fun getNeighbors(node: T): List<T>

    fun findShortestPath(origin: T, goalCondition: (T) -> Boolean): GraphPath<T> {
        val seen = mutableSetOf<T>()
        val pathQueue = PriorityQueue<GraphPath<T>> { a, b -> a.cost - b.cost } //todo: add estimate function for A*

        seen += origin
        pathQueue += GraphPath(origin, 0)

        while(pathQueue.isNotEmpty()) {
            val curr = pathQueue.poll()
            if(goalCondition(curr.edge)) return curr
            val nextEdges = getNeighbors(curr.edge).filter { it !in seen }
            val nextPaths = nextEdges.map { GraphPath(it, curr.cost + it.cost) }

            seen += nextEdges
            pathQueue += nextPaths
            pathQueue -= curr
        }

        error("no path found")
    }
}