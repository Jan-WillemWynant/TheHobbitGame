public enum Direction {
    NORTH("north"),EAST("east"),SOUTH("south"),WEST("west");
    private String directionName;
    Direction(String name){
        this.directionName=name;
    }

    public String getDirectionName(){
        return this.directionName;
    }
}
