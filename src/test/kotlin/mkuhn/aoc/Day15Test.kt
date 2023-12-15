package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day15Test {

    @Test
    fun testHash() {
        val hash = "HASH".holidayHASH()
        assertEquals(52, hash)
    }

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day15_test")
        assertEquals(1320, day15part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day15_test")
        assertEquals(145, day15part2(testInput))
    }
}