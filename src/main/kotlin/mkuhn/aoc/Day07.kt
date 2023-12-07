package mkuhn.aoc

import mkuhn.aoc.util.readInput
import kotlin.math.pow

fun main() {
    val input = readInput("Day07")
    println(day07part1(input)) //246272322 too low, 246806198 too high
    println(day07part2(input))
}

fun day07part1(input: List<String>): Int {
    val hands = input.map { Hand(it.substringBefore(" ").toList(), it.substringAfter(" ").toInt()) }
    return hands.sortedBy { it.score() }.mapIndexed { i, h -> h.bid * (i+1) }.sum()
}

fun day07part2(input: List<String>): Int {
    return 0
}

data class Hand(val cards: List<Char>, val bid: Int) {

    fun score(): Long {
        val groupedCards = cards.groupBy { it }
        val typeScore = when {
            groupedCards.size == 1 -> 20
            groupedCards.any { it.value.size == 4 } -> 19
            groupedCards.any { it.value.size == 3 }
                    && groupedCards.any { it.value.size == 2 } -> 18
            groupedCards.any { it.value.size == 3 } -> 17
            groupedCards.count { it.value.size == 2 } == 2 -> 16
            groupedCards.any { it.value.size == 2 } -> 15
            else -> 0 //cards.maxOf { cardScore(it) }
        }

        val cardScore = cards.reversed().mapIndexed { i, c ->
            cardScore(c).toLong()*((100.0).pow(i).toLong())
        }.sum()

        return (typeScore*10000000000) + cardScore
    }

    fun cardScore(c: Char): Int =
        when {
            c.isDigit() -> c.digitToInt()
            c == 'T' -> 10
            c == 'J' -> 11
            c == 'Q' -> 12
            c == 'K' -> 13
            c == 'A' -> 14
            else -> error("bad input")
        }
}