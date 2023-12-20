package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day19Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day19_test")
        assertEquals(19114, day19part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day19_test")
        assertEquals(167409079868000, day19part2(testInput))
    }
}