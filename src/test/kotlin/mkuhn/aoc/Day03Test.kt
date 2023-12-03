package mkuhn.aoc

import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput
import kotlin.test.assertEquals

internal class Day03Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day03_test")
        assertEquals(4361, day3part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day03_test")
        assertEquals(2286, day3part2(testInput))
    }
}