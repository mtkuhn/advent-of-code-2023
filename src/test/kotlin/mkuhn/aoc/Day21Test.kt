package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day21Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day21_test")
        assertEquals(16, reachablePlots(testInput, 6))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day21_test")
        assertEquals(0, day21part2(testInput))
    }
}