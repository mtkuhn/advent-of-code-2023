package mkuhn.aoc

import org.junit.jupiter.api.Test
import mkuhn.aoc.util.readTestInput

internal class Day01Test {

    @Test
    fun testPart1() {
        val testInput = readTestInput("Day01_test")
        check(day1part1(testInput) == 142)
    }

    @Test
    fun testPart2() {
        val testInput = readTestInput("Day01_test2")
        check(day1part2(testInput) == 281)
    }
}