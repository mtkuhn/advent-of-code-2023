package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day12")
    println(day12part1(input))
    println(day12part2(input))
}

fun day12part1(input: List<String>): Int {
    val inputArrangements = input.map { line ->
        ArrangementData(line.substringBefore(" "),
            line.substringAfter(" ").split(",").map { it.toInt() })
    }
    val possibleArrangements = inputArrangements.map { a -> a.findAllPossibleArrangements() }
    return possibleArrangements.sumOf { it.size }
}

fun day12part2(input: List<String>): Int {
    val inputArrangements = input.map { line ->
        ArrangementData(line.substringBefore(" "),
            line.substringAfter(" ").split(",").map { it.toInt() }).unfold()
    }
    val possibleArrangements = inputArrangements.mapIndexed { i, a -> a.findAllPossibleArrangements().apply { println("$i | ${this.size}") } }

    return possibleArrangements.sumOf { it.size }
}

data class ArrangementData(val unknown: String, val lengths: List<Int>) {

    fun findAllPossibleArrangements(): List<ArrangementData> {
        return if(lengths.isEmpty()) return listOf(this)
        else { findArrangementsForGroupOfLength(lengths.first()) }
    }

    private fun findArrangementsForGroupOfLength(arrLen: Int): List<ArrangementData> {
        val possiblePositions = unknown.indices.windowed(arrLen, partialWindows = false)
            .filter { w ->
                    w.canBeBounded(unknown) &&
                    w.all { unknown[it] != '.' } &&
                    !unknown.substring(0, w.first()).contains("#") && //we can't have extra groups before this
                    (lengths.size > 1 || !unknown.substring(w.last()+1).contains("#")) && //no trailing groups on last match
                    //the below for optimization, not correctness
                    ((unknown.substring(w.last()).length) > (lengths.drop(1).sum())+lengths.size-1) //there's enough space for the remaining groups
            }

        return possiblePositions.flatMap { p ->
            var newUnknown = unknown.substring(p.last()+1)
            if(p.last() < unknown.length-1) newUnknown = newUnknown.drop(1)
            ArrangementData(newUnknown, lengths.drop(1)).findAllPossibleArrangements()
        }

    }

    private fun List<Int>.canBeBounded(str: String) =
        (first() == 0 || str[first()-1] in "?.") &&
        (last() == str.length-1 || str[last()+1] in "?.")

    fun unfold(): ArrangementData = ArrangementData(
        (1..5).joinToString("?") { unknown },
        (1..5).flatMap { lengths })
}


