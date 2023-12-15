package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day15")
    println(day15part1(input))
    println(day15part2(input))
}

fun day15part1(input: List<String>): Int = input.first().split(",").sumOf { it.holidayHASH() }

fun day15part2(input: List<String>): Int {
    val lensBoxes = (0..255).associateWith { mutableListOf<Lens>() }.toMutableMap()
    input.first().split(",").forEach { step ->
        if(step.endsWith("-")) lensBoxes.removeLens(step.substringBefore("-"))
        else if(step.contains("=")) lensBoxes.putLens(step.substringBefore("="), step.substringAfter("=").toInt())
        else error("bad operation: $step")
    }

    return lensBoxes.entries.flatMap { boxEntry ->
        boxEntry.value.mapIndexed { slot, lens ->
            (boxEntry.key+1) * (slot+1) * lens.second
        }
    }.sum()
}

typealias Lens = Pair<String,Int>
typealias LensBoxes = MutableMap<Int, MutableList<Lens>>

fun LensBoxes.removeLens(label: String) = this[label.holidayHASH()]?.removeAll { it.first == label }

fun LensBoxes.putLens(label: String, value: Int) {
    val box = this[label.holidayHASH()]!!
    val oldLens = box.firstOrNull { it.first == label }

    if(oldLens != null) box[box.indexOf(oldLens)] = (label to value)
    else box += (label to value)
}

fun String.holidayHASH() = fold(0) { acc, c -> ((acc+c.code)*17)%256 }