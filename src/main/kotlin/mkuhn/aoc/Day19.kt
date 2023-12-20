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

fun Map<String, List<Volume4D>>.rulesToWorkflowVolumes(): Map<String, List<Volume4D>> =
    this.map { entry ->
        entry.key to entry.value.reversed()
            .fold(emptyList<Volume4D>()) { acc, rule ->
                if(acc.isEmpty()) listOf(rule) else acc.flatMap { it.splitOn(rule) }
            }
    }.toMap()

fun String.toRuleListMap(): Pair<String, List<Volume4D>> {
    val name = substringBefore("{")
    val rules = substringAfter("{")
        .dropLast(1)
        .split(",")
        .map { it.toRule() }
    return name to rules
}

fun String.toRule(): Volume4D =
    "(.)([><])(\\d+):(.*)".toRegex().matchEntire(this)?.destructured
        ?.let { (ratingType, comp, value, res) ->
            Volume4D(
                if(ratingType == "x") intRangeFrom(comp, value.toInt()) else null,
                if(ratingType == "m") intRangeFrom(comp, value.toInt()) else null,
                if(ratingType == "a") intRangeFrom(comp, value.toInt()) else null,
                if(ratingType == "s") intRangeFrom(comp, value.toInt()) else null,
                res
            )
        }?: Volume4D(maxRange, maxRange, maxRange, maxRange, this)

fun intRangeFrom(comparator: String, right: Int) =
    when(comparator) {
        ">" -> (right+1 .. maxRange.last)
        "<" -> (maxRange.first until right)
        else -> error("Invalid comparison $comparator")
    }

data class Volume4D(val xRange: IntRange?, val mRange: IntRange?, val aRange: IntRange?, val sRange: IntRange?, val result: String) {

    fun volume(): Long =
        if (xRange == null || mRange == null || aRange == null || sRange == null) 0
        else (xRange.size().toLong())*(mRange.size().toLong())*(aRange.size().toLong())*(sRange.size().toLong())

    fun contains(x: Int, m: Int, a: Int, s: Int) =
        xRange?.contains(x)?:false && mRange?.contains(m)?:false && aRange?.contains(a)?:false && sRange?.contains(s)?:false

    fun intersect(other: Volume4D): Volume4D? {
        val x = xRange?.intersect(other.xRange)
        val m = mRange?.intersect(other.mRange)
        val a = aRange?.intersect(other.aRange)
        val s = sRange?.intersect(other.sRange)
        return if(x != null && m != null && a != null && s != null) Volume4D(x, m, a, s, result)
        else null
    }

    //todo: not ready for splitting on multiple planes at a time
    fun splitOn(rule: Volume4D): List<Volume4D> =
        splitOnX(rule.xRange, rule.result)
            .flatMap { it.splitOnM(rule.mRange, rule.result) }
            .flatMap { it.splitOnA(rule.aRange, rule.result) }
            .flatMap { it.splitOnS(rule.sRange, rule.result) }

    fun splitOnX(splitRange: IntRange?, splitResult: String): List<Volume4D> =
        safeMinusRange(this.xRange, splitRange).map { Volume4D(it, mRange, aRange, sRange, result) }
            .let { if(splitRange != null) it + Volume4D(splitRange, mRange, aRange, sRange, splitResult) else it }

    fun splitOnM(splitRange: IntRange?, splitResult: String): List<Volume4D> =
        safeMinusRange(this.mRange, splitRange).map { Volume4D(xRange, it, aRange, sRange, result) }
            .let { if(splitRange != null) it + Volume4D(xRange, splitRange, aRange, sRange, splitResult) else it }

    fun splitOnA(splitRange: IntRange?, splitResult: String): List<Volume4D> =
        safeMinusRange(this.aRange, splitRange).map { Volume4D(xRange, mRange, it, sRange, result) }
            .let { if(splitRange != null) it + Volume4D(xRange, mRange, splitRange, sRange, splitResult) else it }

    fun splitOnS(splitRange: IntRange?, splitResult: String): List<Volume4D> =
        safeMinusRange(this.sRange, splitRange).map { Volume4D(xRange, mRange, aRange, it, result) }
            .let { if(splitRange != null) it + Volume4D(xRange, mRange, aRange, splitRange, splitResult) else it }

    private fun safeMinusRange(thisRange: IntRange?, thatRange: IntRange?) =
        if(thisRange != null && thatRange != null) (thisRange rangeMinus thatRange)
        else listOf(thisRange)
}