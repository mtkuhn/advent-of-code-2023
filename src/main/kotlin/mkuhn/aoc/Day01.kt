package mkuhn.aoc

import mkuhn.aoc.util.DigitString
import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day01")
    println(day1part1(input))
    println(day1part2(input))
}

fun day1part1(input: List<String>): Int =
    input.sumOf { line ->
        (line.takeFirstDigit(true)*10) + line.takeLastDigit(false)
    }

fun day1part2(input: List<String>): Int =
    input.sumOf { line ->
        (line.takeFirstDigit(true)*10) + line.takeLastDigit(true)
    }

fun String.takeFirstDigit(includeStringLiterals: Boolean = true): Int {
    (1 .. this.length).forEach { n ->
        if(this[n-1].isDigit()) {
            return this[n-1].digitToInt()
        }
        else if(includeStringLiterals) {
            val i = this.take(n).getIntForContainedLiteralOrNull()
            if(i != null) return i
        }
    }
    error("No digit found")
}

fun String.takeLastDigit(includeStringLiterals: Boolean = true): Int {
    (1 .. this.length).forEach { n ->
        if(this[this.length-n].isDigit()) {
            return this[this.length-n].digitToInt()
        }
        else if(includeStringLiterals) {
            val i = this.takeLast(n).getIntForContainedLiteralOrNull()
            if(i != null) return i
        }
    }
    error("No digit found")
}

fun String.getIntForContainedLiteralOrNull(): Int? =
    DigitString.values().firstOrNull { this.uppercase().contains(it.name) }?.intValue