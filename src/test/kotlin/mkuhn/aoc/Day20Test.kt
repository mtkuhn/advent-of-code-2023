package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day20Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day20_test")
        assertEquals(0, day20part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day20_test")
        assertEquals(0, day20part2(testInput))
    }
}