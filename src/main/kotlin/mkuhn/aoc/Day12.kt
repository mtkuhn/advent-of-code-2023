package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day12")
    println(day12part1(input))
    println(day12part2(input))
}

fun day12part1(input: List<String>): Int {
    val incompleteArrangements = input.map { line ->
        ArrangementData("", line.substringBefore(" "),
            line.substringAfter(" ").split(",").map { it.toInt() })
    }
    val possibleArrangements = incompleteArrangements.flatMap { it.findAllPossibleArrangements() }
    println(possibleArrangements.size)
    println(possibleArrangements.toSet().size)
    println(possibleArrangements.map { it.known }.toSet().size)
    possibleArrangements.groupBy { it }.forEach { println(it.value) }

    return incompleteArrangements.sumOf { it.findAllPossibleArrangements().size }
}

fun day12part2(input: List<String>): Int =
    2

data class ArrangementData(val known: String, val unknown: String, val lengths: List<Int>) {

    fun findAllPossibleArrangements(): Set<ArrangementData> {
        return if(lengths.isEmpty()) return setOf(this)
        else { findArrangementsForLength(lengths.first()) }
    }

    private fun findArrangementsForLength(arrLen: Int): Set<ArrangementData> {
        val possiblePositions = unknown.indices.windowed(arrLen, partialWindows = false)
            .filter { w ->
                w.all { unknown[it] != '.' } && w.isBoundedGroup(unknown)
            }
        return possiblePositions.flatMap { p ->
            val newLengths = lengths.drop(1)
            var newUnknown = unknown.substring((p.last()+2).coerceAtMost(unknown.length))
            var newKnown = known + unknown.substring(0, p.first()).replace('?', '.')+
                    "#".repeat(arrLen) +
                    if(newUnknown.isNotEmpty()) "." else ""
            if(newLengths.isEmpty()) {
                newKnown += newUnknown.replace('?', '.')
                newUnknown = ""
            }
            if((newKnown + newUnknown).length != (known + unknown).length) error("size changed! $known$unknown -> $newKnown$newUnknown | $this")
            ArrangementData(newKnown, newUnknown, newLengths).findAllPossibleArrangements()
        }.toSet()
    }

    private fun List<Int>.isBoundedGroup(str: String) =
        (first() == 0 || str[first()-1] in "?.") &&
        (last() == str.length-1 || str[last()+1] in "?.")
}


