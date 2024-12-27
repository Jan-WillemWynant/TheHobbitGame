/**
 * The Direction enum represents the directions the player can travel/look in.
 *
 * The enum provides the functionality of retrieving the opposite direction and of
 * retrieving the name of the direction as a String.
 */

public enum Direction {
    NORTH("north"), EAST("east"), SOUTH("south"), WEST("west");
    private String directionName; //the name of the direction as a string (e.g. "north").

    /**
     * Constructor method.
     *
     * @param name The name of the direction
     */
    Direction(String name) {
        this.directionName = name;
    }

    /**
     * Method returns the name of the direction.
     *
     * @return the name of the direction
     */
    public String getDirectionName() {
        return this.directionName;
    }

    /**
     * Method returns the opposite direction of the direction the method is called on.
     * For example, method returns SOUTH if it is called on NORTH.
     *
     * @return opposite direction of this direction
     */
    public Direction getOppositeDirection() {
        if (this == NORTH) return SOUTH;
        if (this == EAST) return WEST;
        if (this == SOUTH) return NORTH;
        if (this == WEST) return EAST;
        return null;
    }

}