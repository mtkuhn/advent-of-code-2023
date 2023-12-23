package mkuhn.aoc

import mkuhn.aoc.util.leastCommonFactor
import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day20")
    println(day20part1(input))
    println(day20part2(input))
}

fun day20part1(input: List<String>): Long {
    val modules = input.map { it.nameMapping() }.toModuleMap()
    val pulseCounts = modules.press("broadcaster", 1000)
    return pulseCounts.first.toLong() * pulseCounts.second.toLong()
}

fun day20part2(input: List<String>): Long {
    val mapping = input.map { it.nameMapping() }
    val modules = mapping.toModuleMap()
    val sources = modules["gf"]!!.sources

    val cycles = sources.map { s ->
        val submap = mapping.toModuleMap().getSubMapForDestination(s)
        println(submap)
        submap.findCycle("broadcaster")
    }

    return leastCommonFactor(cycles.map { it.first.toLong() })
}

fun Map<String, Module>.press(startModule: String, count: Int): Pair<Int, Int> {
    var pulseCounts = 0 to 0

    repeat(count) {
        val press = this.press(startModule)
        pulseCounts = pulseCounts.first + press.first to pulseCounts.second + press.second
    }

    return pulseCounts.first to pulseCounts.second
}

fun Map<String, Module>.findCycle(startModule: String): Triple<Int, Int, Int> {
    val stateHistory = mutableListOf<String>()
    var pulseCounts = 0 to 0
    while(this.state() !in stateHistory) { //todo: and this module produces a low pulse
        stateHistory += this.state()
        val press = this.press(startModule)
        pulseCounts = pulseCounts.first+press.first to pulseCounts.second+press.second
    }

    val cycleLength = stateHistory.size-stateHistory.indexOf(this.state())

    return Triple(cycleLength, pulseCounts.first, pulseCounts.second)
}

fun Map<String, Module>.getSubMapForDestination(dest: String): Map<String, Module> {
    val modulesToInclude = mutableSetOf(this[dest])
    var lastSize = 0
    while(modulesToInclude.size != lastSize) {
        lastSize = modulesToInclude.size
        modulesToInclude += modulesToInclude.mapNotNull { m -> m?.sources?.map { this[it] } }.flatten().filter { it !in modulesToInclude }
    }
    return this.filter { e -> e.key in modulesToInclude.mapNotNull { it?.name } }
}


fun Map<String, Module>.state(): String =
    this.entries.sortedBy { it.key }.mapNotNull { it.value.serializedState() }.joinToString(";")

fun Map<String, Module>.press(startModule: String): Pair<Int, Int> {
    val queued = mutableListOf(Pulse(PulseType.LOW, startModule, "origin"))
    val handled = mutableListOf<Pulse>()

    while(queued.isNotEmpty()) {
        val pulse = queued.removeFirst()
        handled += pulse
        val newPulses = this[pulse.to]?.handlePulse(pulse)?:emptyList()
        queued += newPulses
    }

    return handled.count { it.type == PulseType.LOW } to handled.count { it.type == PulseType.HIGH }
}

fun String.nameMapping(): ModuleMapping {
    val before = substringBefore(" -> ")
    val type = before.first()
    val name = before.filter { it !in "&%" }
    val destinations = substringAfter(" -> ").split(", ")
    return ModuleMapping(name, type, destinations)
}

fun List<ModuleMapping>.toModuleMap(): Map<String, Module> =
    this.associate { m ->
        val sources = this.filter { m.name in it.destinations }.map { it.name }
        when(m.type) {
            '%' -> m.name to FlipFlopModule(m.name, sources, m.destinations)
            '&' -> m.name to ConjunctionModule(m.name, sources, m.destinations)
            else -> m.name to BroadcastModule(m.name, sources, m.destinations)
        }
    }

data class ModuleMapping(val name: String, val type: Char, val destinations: List<String>)
enum class PulseType { HIGH, LOW }
data class Pulse(val type: PulseType, val to: String, val from: String)

abstract class Module(open val name: String,
                      open val sources: List<String>,
                      open val destinations: List<String>) {
    abstract fun handlePulse(p: Pulse): List<Pulse>

    fun pulseToAll(pt: PulseType) = destinations.map { Pulse(pt, it, name) }
    open fun serializedState(): String? = null

}

data class FlipFlopModule(override val name: String,
                          override val sources: List<String>,
                          override val destinations: List<String>,
                          var on: Boolean = false): Module(name, sources, destinations) {
    override fun handlePulse(p: Pulse): List<Pulse> {
        return if (p.type == PulseType.HIGH) emptyList()
        else if(!on) {
            on = true
            pulseToAll(PulseType.HIGH)
        } else {
            on = false
            pulseToAll(PulseType.LOW)
        }
    }

    override fun serializedState(): String = on.toString()
}

data class ConjunctionModule(override val name: String,
                             override val sources: List<String>,
                             override val destinations: List<String>,
                             val lastPulses: MutableMap<String, PulseType> = sources.associateWith { PulseType.LOW }.toMutableMap()):
    Module(name, sources, destinations) {
    override fun handlePulse(p: Pulse): List<Pulse> {
        lastPulses[p.from] = p.type
        return if(sources.all { (lastPulses[it]!!) == PulseType.HIGH }) pulseToAll(PulseType.LOW)
        else pulseToAll(PulseType.HIGH)
    }

    override fun serializedState(): String = lastPulses.entries.sortedBy { it.key }.toString()
}

data class BroadcastModule(override val name: String,
                           override val sources: List<String>,
                           override val destinations: List<String>): Module(name, sources, destinations) {
    override fun handlePulse(p: Pulse): List<Pulse> = pulseToAll(p.type)
}