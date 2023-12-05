package mkuhn.aoc

import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput
import kotlin.test.assertEquals

internal class Day05Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day05_test")
        assertEquals(35, day5part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day05_test")
        assertEquals(46, day5part2(testInput))
    }
}