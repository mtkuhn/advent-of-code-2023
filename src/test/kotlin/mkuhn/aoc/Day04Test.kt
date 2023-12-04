package mkuhn.aoc

import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput
import kotlin.test.assertEquals

internal class Day04Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day04_test")
        assertEquals(13, day4part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day04_test")
        assertEquals(30, day4part2(testInput))
    }
}