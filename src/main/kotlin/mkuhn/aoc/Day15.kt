package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day15")
    println(day15part1(input))
    println(day15part2(input))
}

fun day15part1(input: List<String>): Int = input.first().split(",").sumOf { it.holidayHASH() }

fun day15part2(input: List<String>): Int {
    val hashMap = mutableMapOf<Int, LensBoxes>()
    input.first().split(",").forEach { step ->
        if(step.endsWith("-")) hashMap.removeLens(step.substringBefore("-"))
        else if(step.contains("=")) hashMap.putLens(step.substringBefore("="), step.substringAfter("=").toInt())
        else error("bad operation: $step")
    }

    return hashMap.entries.flatMap { boxEntry ->
        boxEntry.value.mapIndexed { slot, lens ->
            (boxEntry.key+1) * (slot+1) * lens.second
        }
    }.sum()
}

typealias LensBoxes = MutableList<Pair<String,Int>>

fun MutableMap<Int, LensBoxes>.removeLens(label: String) = this[label.holidayHASH()]?.removeAll { it.first == label }

fun MutableMap<Int, LensBoxes>.putLens(label: String, value: Int) {
    val hash = label.holidayHASH()
    if(this[hash] == null) this[hash] = mutableListOf()
    val box = this[label.holidayHASH()]!!

    if(box.isEmpty()) this[label.holidayHASH()]
    val oldLens = box.firstOrNull { it.first == label }

    if(oldLens != null) box[box.indexOf(oldLens)] = (label to value)
    else box += (label to value)
}

fun String.holidayHASH() = fold(0) { acc, c -> ((acc+c.code)*17)%256 }