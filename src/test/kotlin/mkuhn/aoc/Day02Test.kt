package mkuhn.aoc

import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput
import kotlin.test.assertEquals

internal class Day02Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day02_test")
        assertEquals(8, day2part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day02_test")
        assertEquals(2286, day2part2(testInput))
    }
}