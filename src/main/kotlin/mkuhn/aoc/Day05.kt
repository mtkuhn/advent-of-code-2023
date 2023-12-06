package mkuhn.aoc

import mkuhn.aoc.util.*
import kotlin.math.pow

fun main() {
    val input = readInput("Day05")
    println(day5part1(input))
    println(day5part2(input))
}

fun day5part1(input: List<String>): Long {
    val seeds = input.first().substringAfter(": ").split(" ").map { it.toLong() }
    val maps = input.splitList("").drop(1).map { RangedMap.fromLines(it) }
    val seedLocations = seeds.map { seed -> maps.superMap(seed) }
    return seedLocations.min()
}

fun day5part2(input: List<String>): Long {
    val seedRanges = input.first().substringAfter(": ").split(" ")
        .map { it.toLong() }
        .chunked(2).map { it[0] until it[0]+it[1] }
    val maps = input.splitList("").drop(1).map { RangedMap.fromLines(it) }

    val seedLocationRanges = seedRanges.flatMap { seedRange -> maps.superMap(seedRange) }
    return seedLocationRanges.minOfOrNull { it.first }?:-1
}

fun List<RangedMap>.superMap(key: Long): Long = this.fold(key) { acc, m -> m.get(acc)  }

fun List<RangedMap>.superMap(key: LongRange): List<LongRange> = this.fold(listOf(key)) { acc, m -> acc.flatMap { m.get(it) }.sortedBy { it.first }  }

data class RangedMap(val sourceType: String, val destinationType: String, val rangedMapEntries: List<RangedMapEntry>) {

    fun get(key: Long): Long = rangedMapEntries.firstOrNull { key in it.sourceRange }?.get(key)?:key

    fun get(key: LongRange): List<LongRange> {
        val mappableAndMappedRanges = rangedMapEntries.mapNotNull { mapEntry ->
            (key intersect mapEntry.sourceRange)?.let { it to mapEntry.get(it.first) ..mapEntry.get(it.last) }
        }
        val unmappedKeyRanges = mappableAndMappedRanges.map { it.first }.fold(listOf(key)) { acc, r -> acc.flatMap { it minus r } }
        return unmappedKeyRanges + (mappableAndMappedRanges.map { it.second })
    }

    companion object {
        fun fromLines(lines: List<String>): RangedMap {
            val types = lines.first().split("-", " ")
            return RangedMap(types[0], types[2], lines.drop(1).map { RangedMapEntry.fromString(it) })
        }
    }
}

data class RangedMapEntry(val sourceRange: LongRange, val destinationRange: LongRange) {

    fun get(key: Long): Long = destinationRange.first + (key-sourceRange.first)

    companion object {
        fun fromString(input: String): RangedMapEntry =
            input.split(" ").map { it.toLong() }.let {
                RangedMapEntry(it[1] until it[1]+it[2], it[0] until it[0]+it[2])
            }
    }
}




