package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day17Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day17_test")
        assertEquals(102, day17part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day17_test")
        assertEquals(94, day17part2(testInput))
    }
}