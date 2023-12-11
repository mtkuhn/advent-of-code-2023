package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day11Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day11_test")
        assertEquals(374, day11part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day11_test")
        assertEquals(8410, day11part2test(testInput))
    }
}