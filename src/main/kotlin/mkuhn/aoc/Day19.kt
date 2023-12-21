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

fun Map<String, List<Volume4D>>.combineToAcceptanceVolumes(): List<Volume4D> {
    var working = this["in"]!!.toMutableList()
    val accepted = mutableListOf<Volume4D>()
    while(working.isNotEmpty()) {
        accepted += working.filter { it.result == "A" }
        working.removeIf { it.result == "A" }
        working.removeIf { it.result == "R" }
        working = working.flatMap { v -> this[v.result]!!.mapNotNull { v2 -> v2.intersect(v) } }.toMutableList()
    }
    return accepted
}

val maxRange = 1.. 4000

fun Map<String, List<Rule>>.rulesToWorkflowVolumes(): Map<String, List<Volume4D>> =
    this.map { entry ->
        entry.key to entry.value.reversed()
            .fold(listOf(Volume4D(maxRange, maxRange, maxRange, maxRange, "unassigned"))) { acc, rule ->
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
        }?: Rule("*", maxRange, this)

fun intRangeFrom(comparator: String, right: Int) =
    when(comparator) {
        ">" -> (right+1 .. maxRange.last)
        "<" -> (maxRange.first until right)
        else -> error("Invalid comparison $comparator")
    }

data class Rule(val axis: String, val range: IntRange, val result: String)

data class Volume4D(val xRange: IntRange, val mRange: IntRange, val aRange: IntRange, val sRange: IntRange, val result: String) {

    fun volume(): Long =
        (xRange.size().toLong())*(mRange.size().toLong())*(aRange.size().toLong())*(sRange.size().toLong())

    fun contains(x: Int, m: Int, a: Int, s: Int) =
        xRange.contains(x) && mRange.contains(m) && aRange.contains(a) && sRange.contains(s)

    fun intersect(other: Volume4D): Volume4D? {
        val x = xRange.intersect(other.xRange)
        val m = mRange.intersect(other.mRange)
        val a = aRange.intersect(other.aRange)
        val s = sRange.intersect(other.sRange)
        return if(x != null && m != null && a != null && s != null) Volume4D(x, m, a, s, result)
        else null //todo: error("no intersection $this | $other")
    }

    fun splitOn(rule: Rule): List<Volume4D> =
        when (rule.axis) {
            "x" -> splitOnX(rule.range, rule.result)
            "m" -> splitOnM(rule.range, rule.result)
            "a" -> splitOnA(rule.range, rule.result)
            "s" -> splitOnS(rule.range, rule.result)
            else -> listOf(Volume4D(maxRange, maxRange, maxRange, maxRange, rule.result))
        }

    fun splitOnX(splitRange: IntRange, splitResult: String): List<Volume4D> =
        if(this.xRange.intersect(splitRange) == null) listOf(this)
        else (this.xRange rangeMinus splitRange).map { Volume4D(it, mRange, aRange, sRange, result) } +
                Volume4D(splitRange, mRange, aRange, sRange, splitResult)

    fun splitOnM(splitRange: IntRange, splitResult: String): List<Volume4D> =
        if(this.mRange.intersect(splitRange) == null) listOf(this)
        else (this.mRange rangeMinus splitRange).map { Volume4D(xRange, it, aRange, sRange, result) } +
                Volume4D(xRange, splitRange, aRange, sRange, splitResult)

    fun splitOnA(splitRange: IntRange, splitResult: String): List<Volume4D> =
        if(this.aRange.intersect(splitRange) == null) listOf(this)
        else (this.aRange rangeMinus splitRange).map { Volume4D(xRange, mRange, it, sRange, result) } +
                Volume4D(xRange, mRange, splitRange, sRange, splitResult)

    fun splitOnS(splitRange: IntRange, splitResult: String): List<Volume4D> =
        if(this.sRange.intersect(splitRange) == null) listOf(this)
        else (this.sRange rangeMinus splitRange).map { Volume4D(xRange, mRange, aRange, it, result) } +
                Volume4D(xRange, mRange, aRange, splitRange, splitResult)
}