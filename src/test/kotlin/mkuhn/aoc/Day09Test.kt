package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day09Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day09_test")
        assertEquals(114, day09part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day09_test")
        assertEquals(2, day09part2(testInput))
    }
}