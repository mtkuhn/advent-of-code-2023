package mkuhn.aoc

import mkuhn.aoc.util.readInput

fun main() {
    val input = readInput("Day20")
    println(day20part1(input))
    println(day20part2(input))
}

fun day20part1(input: List<String>): Int {
    val modules = input.map { it.nameMapping() }.toModuleMap()
    val pulseCounts = modules.press(1000)

    return pulseCounts.first * pulseCounts.second
}

fun day20part2(input: List<String>): Int =
    2

fun Map<String, Module>.press(count: Int): Pair<Int, Int> {
    val stateHistory = mutableListOf<String>()
    var pulseCounts = 0 to 0
    println(this.state())
    while(this.state() !in stateHistory) {
        stateHistory += this.state()
        val press = this.press()
        println(this.state())
        pulseCounts = pulseCounts.first+press.first to pulseCounts.second+press.second
    }

    val pressesInCycle = stateHistory.size-stateHistory.indexOf(this.state())
    val cyclesLeft = count/pressesInCycle

    println("$pressesInCycle presses, $cyclesLeft cycles left. $pulseCounts | ${this.state()}")

    return pulseCounts.first*cyclesLeft to pulseCounts.second*cyclesLeft
}

fun Map<String, Module>.state(): String =
    this.entries.sortedBy { it.key }.mapNotNull { it.value.serializedState() }.joinToString(";")

fun Map<String, Module>.press(): Pair<Int, Int> {
    val queued = mutableListOf(Pulse(PulseType.LOW, "broadcaster", "button"))
    val handled = mutableListOf<Pulse>()

    while(queued.isNotEmpty()) {
        val pulse = queued.removeFirst()
        handled += pulse
        val newPulses = this[pulse.to]?.handlePulse(pulse)?:emptyList()
        queued += newPulses
    }

    //handled.forEach { println(it) }
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