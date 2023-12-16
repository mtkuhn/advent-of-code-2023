package mkuhn.aoc.util

enum class Direction(val moveFrom: (Pair<Int,Int>) -> Pair<Int, Int>) {
    NORTH({ p -> p.first - 1 to p.second }),
    EAST({ p -> p.first to p.second + 1 }),
    SOUTH({ p -> p.first + 1 to p.second }),
    WEST({ p -> p.first to p.second - 1 });

    fun getOpposite(): Direction =
        when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
}