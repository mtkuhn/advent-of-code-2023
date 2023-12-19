package mkuhn.aoc

import mkuhn.aoc.util.readInput
import mkuhn.aoc.util.splitList
import mkuhn.aoc.util.takeWhileInclusive

fun main() {
    val input = readInput("Day19")
    println(day19part1(input))
    println(day19part2(input))
}

fun day19part1(input: List<String>): Int {
    val split = input.splitList("")
    val workflows = split.first().associate { it.toWorkflow() }
    val parts = split.last().map { line ->
        line.drop(1).dropLast(1)
            .split(",")
            .associate { it.substringBefore("=") to it.substringAfter("=").toInt() }
    }

    return parts.filter { workflows.evaluate(it) == "A" }
        .sumOf { it.values.sum() }
}

fun day19part2(input: List<String>): Int =
    2

typealias Part = Map<String, Int>
typealias Workflow = List<Rule>

data class Rule(
    val ratingType: String,
    val validator: (Int, Int) -> Boolean,
    val comparableValue: Int,
    val result: String) {

    fun evaluate(part: Part): String? =
        if(validator(part[ratingType]?:error("missing rating type $ratingType"), comparableValue)) result
        else null

}

fun String.toWorkflow(): Pair<String, List<Rule>> {
    val name = substringBefore("{")
    val rules = substringAfter("{")
        .dropLast(1)
        .split(",")
        .map { it.toRule() }
    return name to rules
}

fun String.toRule(): Rule =
    "(.)([><])(\\d+):(.*)".toRegex().matchEntire(this)?.destructured
        ?.let { (ratingType, comp, value, res) -> Rule(ratingType, comp.toValidator(), value.toInt(), res) }
        ?:Rule("x", {_: Int, _: Int -> true}, 0, this)

fun String.toValidator() =
    when(this) {
        ">" -> { a: Int, b: Int -> a > b }
        "<" -> { a: Int, b: Int -> a < b }
        else -> error("unknown comparison $this")
    }

fun Workflow.evaluate(part: Part): String = this.firstNotNullOf { it.evaluate(part) }

fun Map<String, List<Rule>>.evaluate(part: Part): String =
    generateSequence("in") { this[it]?.evaluate(part) }.takeWhileInclusive { it != "R" && it != "A" }.last()