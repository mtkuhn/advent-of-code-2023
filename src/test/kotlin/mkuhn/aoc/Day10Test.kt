package mkuhn.aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day10Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day10_test")
        assertEquals(4, day10part1(testInput))
    }
    @Test
    fun testPart1b() {
        val testInput = readTestInput("Day10_test2")
        assertEquals(8, day10part1(testInput))
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day10_test3")
        assertEquals(4, day10part2(testInput))
    }

    @Test
    fun testPart2b() {
        val testInput = readTestInput("Day10_test4")
        assertEquals(4, day10part2(testInput))
    }

    @Test
    fun testPart2c() {
        val testInput = readTestInput("Day10_test5")
        assertEquals(8, day10part2(testInput))
    }
}