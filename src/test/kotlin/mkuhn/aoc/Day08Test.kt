package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day08Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day08_test")
        assertEquals(2, day08part1(testInput))
    }

    @Test
    fun testPart1b() {
        val testInput = readTestInput("Day08_test2")
        assertEquals(6, day08part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day08_test3")
        assertEquals(0, day08part2(testInput))
    }
}