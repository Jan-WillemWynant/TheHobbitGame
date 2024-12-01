public enum Direction {
    NORTH("north"), EAST("east"), SOUTH("south"), WEST("west");
    private String directionName;

    Direction(String name) {
        this.directionName = name;
    }

    /*
     *Returns the name of the direction.
     */
    public String getDirectionName() {
        return this.directionName;
    }

    /*
     *Returns the opposite direction (so if called on NORTH, returns SOUTH).
     */
    public Direction getOppositeDirection() {
        if (this == NORTH) return SOUTH;
        if (this == EAST) return WEST;
        if (this == SOUTH) return NORTH;
        if (this == WEST) return EAST;
        return null;
    }

}