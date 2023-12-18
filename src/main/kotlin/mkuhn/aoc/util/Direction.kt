package mkuhn.aoc.util

enum class Direction(val moveFrom: (Point) -> Point) {
    NORTH({ p -> Point(p.x, p.y-1) }),
    EAST({ p -> Point(p.x+1, p.y) }),
    SOUTH({ p -> Point(p.x, p.y+1) }),
    WEST({ p -> Point(p.x-1, p.y) });

    fun getOpposite(): Direction =
        when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
}