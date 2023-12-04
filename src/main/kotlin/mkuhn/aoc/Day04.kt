package mkuhn.aoc

import mkuhn.aoc.util.coerceWithin
import mkuhn.aoc.util.readInput
import kotlin.math.pow

fun main() {
    val input = readInput("Day04")
    println(day4part1(input))
    println(day4part2(input))
}

fun day4part1(input: List<String>): Int =
    input.map { line -> ScoredCard.fromString(line) }
        .sumOf { it.score }

fun day4part2(input: List<String>): Int {
    val cards = input.map { line -> ScoredCard.fromString(line) }
    val cardIdxToCopyCount = List(cards.size) { i -> i to 1 }.toMap().toMutableMap()
    cards.forEachIndexed { cardIdx, card ->
        if(card.matchingNumberCount > 0) {
            (cardIdx+1 .. cardIdx+card.matchingNumberCount).coerceWithin(cards.indices)
                .forEach { copyIdx ->
                    cardIdxToCopyCount[copyIdx] = cardIdxToCopyCount[copyIdx]!!.plus(cardIdxToCopyCount[cardIdx]!!)
                }
        }
    }
    return cardIdxToCopyCount.values.sum()
}

data class ScoredCard(val matchingNumberCount: Int, val score: Int) {
    companion object {
        fun fromString(line: String): ScoredCard {
            val winningNumbers = line.substringAfter(": ")
                .substringBefore(" | ")
                .split(" ")
                .filter { it.trim().isNotEmpty() }
                .map { it.toInt() }
                .toSet()
            val ourNumbers = line.substringAfter(" | ")
                .split(" ")
                .filter { it.trim().isNotEmpty() }
                .map { it.toInt() }
                .toSet()
            val matchingNumberCount = winningNumbers.intersect(ourNumbers).count()
            val score = when(matchingNumberCount) {
                0 -> 0
                else -> (2.0).pow(matchingNumberCount - 1.0).toInt()
            }
            return ScoredCard(matchingNumberCount, score)
        }
    }
}



