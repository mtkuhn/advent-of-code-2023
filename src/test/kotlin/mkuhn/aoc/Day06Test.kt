package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day06Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day06_test")
        assertEquals(288, day6part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day06_test")
        assertEquals(71503, day6part2(testInput))
    }
}