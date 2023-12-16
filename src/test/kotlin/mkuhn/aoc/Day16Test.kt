package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day16Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day16_test")
        assertEquals(46, day16part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day16_test")
        assertEquals(51, day16part2(testInput))
    }
}