package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day07Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day07_test")
        assertEquals(6440, day07part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day07_test")
        assertEquals(5905, day07part2(testInput))
    }
}