package mkuhn.aoc

import mkuhn.aoc.util.*

fun main() {
    val input = readInput("Day19")
    println(day19part1(input))
    println(day19part2(input))
}

fun day19part1(input: List<String>): Int {
    val split = input.splitList("")
    val acceptanceVolumes = split.first()
        .associate { it.toRuleListMap() }
        .rulesToWorkflowVolumes()
        .combineToAcceptanceVolumes()

    val parts = split.last().map { line ->
        line.drop(1).dropLast(1)
            .split(",")
            .associate { it.substringBefore("=") to it.substringAfter("=").toInt() }
    }

    return parts.filter { p -> acceptanceVolumes.any { it.contains(p["x"]!!, p["m"]!!, p["a"]!!, p["s"]!!) } }
        .sumOf { it.values.sum() }
}

fun day19part2(input: List<String>): Long {
    val split = input.splitList("")
    val acceptanceVolumes = split.first()
        .associate { it.toRuleListMap() }
        .rulesToWorkflowVolumes()
        .apply { this.values.forEach { if(it.sumOf { a -> a.volume() } != 256000000000000) error("bad volume") } }
        .combineToAcceptanceVolumes()

    return acceptanceVolumes.sumOf { it.volume() }
}

fun Map<String, List<HyperVolume>>.combineToAcceptanceVolumes(): List<HyperVolume> {
    var working = this["in"]!!.toMutableList()
    val accepted = mutableListOf<HyperVolume>()
    while(working.isNotEmpty()) {
        accepted += working.filter { it.result == "A" }
        working.removeIf { it.result == "A" }
        working.removeIf { it.result == "R" }
        working = working.flatMap { v -> this[v.result]!!.mapNotNull { v2 -> v2.intersect(v) } }.toMutableList()
    }
    return accepted
}

fun Map<String, List<Rule>>.rulesToWorkflowVolumes(): Map<String, List<HyperVolume>> =
    this.map { entry ->
        val default = entry.value.last()
        entry.key to entry.value.reversed().drop(1)
            .fold(listOf(HyperVolume.getMaxRangeVolume(default.result))) { acc, rule ->
                acc.flatMap { it.splitOn(rule) }
            }
    }.toMap()

fun String.toRuleListMap(): Pair<String, List<Rule>> {
    val name = substringBefore("{")
    val rules = substringAfter("{")
        .dropLast(1)
        .split(",")
        .map { it.toRule() }
    return name to rules
}

fun String.toRule(): Rule =
    "(.)([><])(\\d+):(.*)".toRegex().matchEntire(this)?.destructured
        ?.let { (ratingType, comp, value, res) ->
            Rule(ratingType, intRangeFrom(comp, value.toInt()), res)
        }?: Rule("*", HyperVolume.maxRange, this)

fun intRangeFrom(comparator: String, right: Int) =
    when(comparator) {
        ">" -> (right+1 .. HyperVolume.maxRange.last)
        "<" -> (HyperVolume.maxRange.first until right)
        else -> error("Invalid comparison $comparator")
    }

data class Rule(val axis: String, val range: IntRange, val result: String)

data class HyperVolume(val axisRanges: Map<String, IntRange>, val result: String) {

    fun volume(): Long = axisRanges.values.map { it.size().toLong() }.reduce { acc, i -> acc*i }

    fun contains(x: Int, m: Int, a: Int, s: Int) =
        axisRanges["x"]!!.contains(x) && axisRanges["m"]!!.contains(m) && axisRanges["a"]!!.contains(a) && axisRanges["s"]!!.contains(s)

    fun intersect(other: HyperVolume): HyperVolume? {
        val newAxis = axisRanges.keys.map { axis -> axis to axisRanges[axis]?.intersect(other.axisRanges[axis]) }
        return if(newAxis.any { it.second == null }) null
        else HyperVolume(newAxis.associate { it.first to it.second!! }, result)
    }

    fun splitOn(rule: Rule): List<HyperVolume> = splitOnAxis(rule.axis, rule.range, rule.result)

    private fun splitOnAxis(axis: String, splitRange: IntRange, splitResult: String): List<HyperVolume> =
        if(axisRanges[axis]?.intersect(splitRange) == null) listOf(this)
        else (axisRanges[axis]!! rangeMinus splitRange).map { copyWithReplacedAxis(axis, it, result) } +
                copyWithReplacedAxis(axis, splitRange, splitResult)

    private fun copyWithReplacedAxis(axisKey: String, axisRange: IntRange, newResult: String) =
        HyperVolume(this.axisRanges.toMutableMap().apply { this[axisKey] = axisRange }, newResult)

    companion object {
        val maxRange = 1.. 4000
        fun getMaxRangeVolume(result: String) = HyperVolume(listOf("x", "m", "a", "s").associateWith { maxRange }, result)
    }

}