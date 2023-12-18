package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day18Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day18_test")
        assertEquals(62, day18part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day18_test")
        assertEquals(0, day18part2(testInput))
    }
}