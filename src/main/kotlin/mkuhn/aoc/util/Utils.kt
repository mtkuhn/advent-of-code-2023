package mkuhn.aoc.util

import java.io.File

fun readInput(name: String) = File("src/main/resources/", "$name.txt")
    .readLines()

fun readTestInput(name: String) = File("src/test/resources/", "$name.txt")
    .readLines()

fun String.splitToPair(separator: Char) = this.substringBefore(separator) to this.substringAfter(separator)

fun <T> List<T>.splitList(matcher: (T) -> Boolean): List<List<T>> =
    this.fold(mutableListOf(mutableListOf<T>())) { acc, a ->
        if(matcher(a)) acc += mutableListOf<T>()
        else acc.last() += a
        acc
    }

fun <T> List<T>.splitList(separator: T): List<List<T>> = this.splitList { it == separator }

fun <T> Collection<Collection<T>>.intersectAll(): Set<T> =
    this.fold(this.first().toSet()) { acc, e -> acc intersect e.toSet() }

fun <T> List<List<T>>.transpose(filterCondition: (T) -> Boolean = { true }): MutableList<MutableList<T>> =
    mutableListOf<MutableList<T>>().apply {
        repeat(this@transpose.first().size) { this += mutableListOf<T>() }
        this@transpose.forEach { r ->
            r.forEachIndexed { i, c ->
                if(filterCondition(c)) { this[i] += c }
            }
        }
    }

inline fun <T> T.feedbackCycle(count: Int, func: (T) -> T): T {
    var iter = this
    repeat(count) { iter = func(iter) }
    return iter
}

inline fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (!predicate(item))
            break
    }
    return list
}

inline fun <T> Sequence<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (!predicate(item))
            break
    }
    return list
}

fun Int.progressBetween(i: Int) = IntProgression.fromClosedRange(this, i, if(this > i) -1 else 1)

fun Long.progressBetween(i: Long) = LongProgression.fromClosedRange(this, i, if(this > i) -1 else 1)

//stolen shamelessly from Todd
infix fun IntRange.fullyOverlaps(other: IntRange): Boolean =
    first <= other.first && last >= other.last

infix fun IntRange.overlaps(other: IntRange): Boolean =
    first <= other.last && other.first <= last

infix fun LongRange.fullyOverlaps(other: LongRange): Boolean =
    first <= other.first && last >= other.last

infix fun LongRange.overlaps(other: LongRange): Boolean =
    first <= other.last && other.first <= last

fun IntRange.coerceWithin(bounds: IntRange): IntRange =
    (start).coerceAtLeast(bounds.first) .. (endInclusive).coerceAtMost(bounds.last)

fun LongRange.coerceWithin(bounds: LongRange): LongRange =
    (start).coerceAtLeast(bounds.first) .. (endInclusive).coerceAtMost(bounds.last)

infix fun LongRange.minus(substract: LongRange): List<LongRange> {
    return if(substract fullyOverlaps this) emptyList()
    else if(this intersect substract == null) listOf(this)
    else {
        val differences = mutableListOf<LongRange>()
        if(this.first < substract.first) differences.add(this.first until substract.first)
        if(this.last > substract.last) differences.add((substract.last+1) .. this.last)
        differences
    }
}

infix fun LongRange.intersect(other: LongRange): LongRange? =
    if (this fullyOverlaps other) other
    else if (other fullyOverlaps this) this
    else if (this overlaps other) this.first.coerceAtLeast(other.first) .. this.last.coerceAtMost(other.last)
    else null

infix fun IntRange.rangeMinus(subtract: IntRange): List<IntRange> {
    return if(subtract fullyOverlaps this) emptyList()
    else if(this intersect subtract == null) listOf(this)
    else {
        val differences = mutableListOf<IntRange>()
        if(this.first < subtract.first) differences.add(this.first until subtract.first)
        if(this.last > subtract.last) differences.add((subtract.last+1) .. this.last)
        differences
    }
}

infix fun IntRange.intersect(other: IntRange?): IntRange? =
    if (other == null) null
    else if (this fullyOverlaps other) other
    else if (other fullyOverlaps this) this
    else if (this overlaps other) this.first.coerceAtLeast(other.first) .. this.last.coerceAtMost(other.last)
    else null

fun IntRange.size() = this.last - this.first + 1

fun greatestCommonFactor(a: Long, b: Long): Long = if (b == 0L) a else greatestCommonFactor(b, a % b)
fun leastCommonFactor(a: Long, b: Long): Long = (a*b) / greatestCommonFactor(a, b)
fun leastCommonFactor(a: List<Long>): Long = a.reduce { acc, f -> leastCommonFactor(acc, f) }
fun <T> Sequence<T>.repeatForever() = generateSequence(this) { it }.flatten() //Thank you, stack overflow

