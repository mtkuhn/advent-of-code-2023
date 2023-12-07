package mkuhn.aoc

import mkuhn.aoc.util.readInput
import kotlin.math.pow

fun main() {
    val input = readInput("Day07")
    println(day07part1(input))
    println(day07part2(input))
}

fun day07part1(input: List<String>): Int {
    val hands = input.map { Hand(it.substringBefore(" ").toList(), it.substringAfter(" ").toInt()) }
    return hands.sortedBy { it.score() }.mapIndexed { i, h -> h.bid * (i+1) }.sum()
}

fun day07part2(input: List<String>): Int {
    val hands = input.map { Hand(it.substringBefore(" ").toList(), it.substringAfter(" ").toInt()) }
    return hands.sortedBy { it.scoreWithJokers() }.mapIndexed { i, h -> h.bid * (i+1) }.sum()
}

data class Hand(val cards: List<Char>, val bid: Int) {

    fun score(): Long {
        val groupedCards = cards.groupBy { it }
        val typeScore = when {
            groupedCards.size == 1 -> 6
            groupedCards.any { it.value.size == 4 } -> 5
            groupedCards.any { it.value.size == 3 } && groupedCards.any { it.value.size == 2 } -> 4
            groupedCards.any { it.value.size == 3 } -> 3
            groupedCards.count { it.value.size == 2 } == 2 -> 2
            groupedCards.any { it.value.size == 2 } -> 1
            else -> 0
        }

        val cardScore = cards.reversed().mapIndexed { i, c ->
            cardScore(c).toLong()*((100.0).pow(i).toLong())
        }.sum()

        return (typeScore*10000000000) + cardScore
    }

    fun scoreWithJokers(): Long {
        val numJokers = cards.count { it == 'J' }
        val groupedCards = cards.filter { it != 'J' }.groupBy { it }
        val typeScore = when {
            numJokers == 5 -> 6
            groupedCards.any { it.value.size+numJokers == 5 } -> 6
            groupedCards.any { it.value.size+numJokers == 4 } -> 5
            groupedCards.any { it.value.size == 3 } && groupedCards.any { it.value.size == 2 } -> 4
            groupedCards.count { it.value.size == 2 } == 2 && numJokers >= 1 -> 4
            groupedCards.any { it.value.size+numJokers == 3 } -> 3
            groupedCards.count { it.value.size == 2 } == 2 -> 2 //bad joker case, jumping to 3 of a kind is always better than 2 pair
            groupedCards.any { it.value.size+numJokers == 2 } -> 1
            else -> 0
        }

        val cardScore = cards.reversed().mapIndexed { i, c ->
            cardScoreWithJokers(c).toLong()*((100.0).pow(i).toLong())
        }.sum()

        return (typeScore*10000000000) + cardScore
    }

    fun cardScore(c: Char): Int = "23456789TJQKA".indexOf(c)

    fun cardScoreWithJokers(c: Char): Int = "J23456789TQKA".indexOf(c)
}