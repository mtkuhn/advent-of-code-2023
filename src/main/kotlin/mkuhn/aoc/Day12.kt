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
    val possibleArrangements = incompleteArrangements.map {
        println("$it")
        it.findAllPossibleArrangements().apply { this.forEach{ println("  $it") } }
    }

    return possibleArrangements.sumOf { it.size }
}

fun day12part2(input: List<String>): Int =
    2

data class ArrangementData(val known: String, val unknown: String, val lengths: List<Int>) {

    fun findAllPossibleArrangements(): List<ArrangementData> {
        return if(lengths.isEmpty()) return listOf(this)
        else { findArrangementsForLength(lengths.first()) }
    }

    private fun findArrangementsForLength(arrLen: Int): List<ArrangementData> {
        val possiblePositions = unknown.indices.windowed(arrLen, partialWindows = false)
            .filter { w -> w.all { unknown[it] != '.' } &&
                    w.isBoundedGroup(unknown) &&
                    !unknown.substring(0, w.first()).contains("#")
            }

        val correctedChars = possiblePositions.map { indices ->
            val charPos = indices.map { it to '#' }.toMutableList()
            if(indices.first() > 0) charPos.add(indices.first()-1 to '.')
            if(indices.last() < unknown.length-1) charPos.add(indices.last()+1 to '.')
            charPos.sortedBy { it.first }
        }

        return correctedChars.flatMap { p ->
            val newLengths = lengths.drop(1)
            var newUnknown = unknown.substring(p.last().first+1)
            var newKnown = known +
                    unknown.substring(0, p.first().first).replace('?', '.') +
                    p.map { it.second }.joinToString("")
            if(newLengths.isEmpty()) {
                newKnown += newUnknown.replace('?', '.')
                newUnknown = ""
            }
            ArrangementData(newKnown, newUnknown, newLengths).findAllPossibleArrangements()
        }
    }

    private fun List<Int>.isBoundedGroup(str: String) =
        (first() == 0 || str[first()-1] in "?.") &&
        (last() == str.length-1 || str[last()+1] in "?.")
}


