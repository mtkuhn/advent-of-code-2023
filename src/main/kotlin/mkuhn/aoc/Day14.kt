package mkuhn.aoc

import mkuhn.aoc.util.*

fun main() {
    val input = readInput("Day14")
    println(day14part1(input))
    println(day14part2(input))
}

fun day14part1(input: List<String>): Int =
    input.map { it.toList() }.rollNorth().getLoad()

fun day14part2(input: List<String>): Int {
    val initDish = input.map { it.toList() }
    val dishList = mutableMapOf(initDish to 0)

    val maxCycles = 1000000000
    var currCycles = 0
    var dish = initDish
    while(currCycles < maxCycles) {
        dish = dish.cycle()
        if(dishList.contains(dish)) {
            val loopLength = currCycles - dishList[dish]!!
            val loopsInRemaining = (maxCycles-currCycles)%loopLength
            currCycles = maxCycles - loopsInRemaining
        } else {
            dishList[dish] = currCycles
        }
        currCycles++
    }

    return dish.getLoad()
}

typealias Dish = List<List<Char>>

fun Dish.cycle() = rollNorth().rollWest().rollSouth().rollEast()

fun Dish.rollNorth() =
    this.first().indices.map { x ->
        getCol(x).splitList('#').flatMap { it.sortedDescending().plus('#') }.dropLast(1)
    }.transpose()

fun Dish.rollWest() =
    this.first().indices.map { x ->
        get(x).splitList('#')
            .flatMap { it.sortedDescending().plus('#') }
            .dropLast(1)
    }

fun Dish.rollSouth() =
    this.first().indices.map { x ->
        getCol(x).reversed()
            .splitList('#')
            .flatMap { it.sortedDescending().plus('#') }
            .dropLast(1)
            .reversed()
    }.transpose()

fun Dish.rollEast() =
    this.first().indices.map { x ->
        get(x).reversed()
            .splitList('#')
            .flatMap { it.sortedDescending().plus('#') }
            .dropLast(1)
            .reversed()
    }

fun Dish.getCol(c: Int) = this.map { it[c] }

fun Dish.getLoad() = this.reversed().mapIndexed { idx, c -> (idx+1) * c.count { it == 'O' } }.sum()