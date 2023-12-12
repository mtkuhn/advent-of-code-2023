package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day12")
    println(day12part1(input))
    println(day12part2(input))
}

fun day12part1(input: List<String>): Long {
    val inputArrangements = input.map { line ->
        ArrangementData(line.substringBefore(" "),
            line.substringAfter(" ").split(",").map { it.toInt() })
    }
    val possibleArrangements = inputArrangements.mapIndexed { i, a ->
        a.findAllPossibleArrangements(ArrangementEntry(0, 0))
    }

    return possibleArrangements.sumOf { it }
}

fun day12part2(input: List<String>): Long {
    val inputArrangements = input.map { line ->
        ArrangementData(line.substringBefore(" "),
            line.substringAfter(" ").split(",").map { it.toInt() }).unfold()
    }
    val possibleArrangements = inputArrangements.mapIndexed { i, a ->
        a.findAllPossibleArrangements(ArrangementEntry(0, 0)).apply { println("$i | ${this}") }
    }
    return possibleArrangements.sumOf { it }
}

data class ArrangementEntry(val strPos: Int, val lenPos: Int)

data class ArrangementData(val springs: String, val lengths: List<Int>) {

    private val cache: MutableMap<ArrangementEntry, Long> = mutableMapOf()

    fun findAllPossibleArrangements(entry: ArrangementEntry): Long {
        return if(entry.lenPos >= lengths.size) 1
        else if(entry.strPos >= springs.length) 0
        else {
            if(cache.containsKey(entry)) {
                //println("cache hit")
                cache[entry]!!
            } else {
                val result = entry.findArrangements()
                cache[entry] = result
                result
            }
        }
    }


    private fun ArrangementEntry.findArrangements(): Long {
        val stringToEval = springs.substring(strPos)
        val possiblePositions = stringToEval.indices.windowed(lengths[lenPos], partialWindows = false)
            .filter { w ->
                    w.canBeBounded(stringToEval) &&
                    w.all { stringToEval[it] != '.' } &&
                    !stringToEval.substring(0, w.first()).contains("#") && //we can't have extra groups before this
                    !(lenPos == lengths.size-1 && stringToEval.substring(w.last()+1).contains("#")) //no trailing groups on last match
            }

        return possiblePositions.sumOf { p ->
            findAllPossibleArrangements(ArrangementEntry((strPos+p.last()+2), lenPos+1))
        }
    }

    private fun List<Int>.canBeBounded(str: String) =
        (first() == 0 || str[first()-1] in "?.") &&
        (last() == str.length-1 || str[last()+1] in "?.")

    fun unfold(): ArrangementData = ArrangementData(
        (1..5).joinToString("?") { springs },
        (1..5).flatMap { lengths }
    )
}


