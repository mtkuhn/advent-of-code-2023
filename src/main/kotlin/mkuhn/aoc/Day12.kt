package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day12")
    println(day12part1(input))
    println(day12part2(input))
}

fun day12part1(input: List<String>): Int {
    val inputArrangements = input.map { line ->
        ArrangementData(line.substringBefore(" "), 0,
            line.substringAfter(" ").split(",").map { it.toInt() })
    }
    val possibleArrangements = inputArrangements.mapIndexed { i, a ->
        val arrangementCache: MutableMap<Pair<Int, Int>, List<ArrangementData>> = mutableMapOf()
        a.findAllPossibleArrangements(arrangementCache)
    }
    return possibleArrangements.sumOf { it.size }
}

fun day12part2(input: List<String>): Int {
    val inputArrangements = input.map { line ->
        ArrangementData(line.substringBefore(" "), 0,
            line.substringAfter(" ").split(",").map { it.toInt() }).unfold()
    }
    val possibleArrangements = inputArrangements.mapIndexed { i, a ->
        val arrangementCache: MutableMap<Pair<Int, Int>, List<ArrangementData>> = mutableMapOf()
        a.findAllPossibleArrangements(arrangementCache).apply { println("$i | ${this.size}") }
    }
    return possibleArrangements.sumOf { it.size }
}

data class ArrangementData(val fullString: String, val startPos: Int, val lengths: List<Int>) {

    fun findAllPossibleArrangements(cache: MutableMap<Pair<Int, Int>, List<ArrangementData>>): List<ArrangementData> {
        val str = fullString.substring(startPos)
        return if(lengths.isEmpty()) listOf(this)
        else {
            if(cache.containsKey(str.length to this.lengths.first())) {
                //println("cache hit")
                cache[str.length to this.lengths.first()]!!
            } else {
                val result = str.findArrangementsForGroupOfLength(lengths.first(), cache)
                cache[str.length to this.lengths.first()] = result
                result
            }
        }
    }


    private fun String.findArrangementsForGroupOfLength(arrLen: Int,
                                                 cache: MutableMap<Pair<Int, Int>, List<ArrangementData>>): List<ArrangementData> {

        val possiblePositions = this.indices.windowed(arrLen, partialWindows = false)
            .filter { w ->
                    w.canBeBounded(this) &&
                    w.all { this[it] != '.' } &&
                    !this.substring(0, w.first()).contains("#") && //we can't have extra groups before this
                    (lengths.size > 1 || !this.substring(w.last()+1).contains("#")) && //no trailing groups on last match
                    //the below for optimization, not correctness
                    ((this.substring(w.last()).length) > (lengths.drop(1).sum())+lengths.size-1) //there's enough space for the remaining groups
            }

        return possiblePositions.flatMap { p ->
            var offset = p.last()+2
            ArrangementData(fullString,
                (startPos+offset).coerceAtMost(fullString.length-1),
                lengths.drop(1)).findAllPossibleArrangements(cache)
        }

    }

    private fun List<Int>.canBeBounded(str: String) =
        (first() == 0 || str[first()-1] in "?.") &&
        (last() == str.length-1 || str[last()+1] in "?.")

    fun unfold(): ArrangementData = ArrangementData(
        (1..5).joinToString("?") { fullString },
        0,
        (1..5).flatMap { lengths })
}


