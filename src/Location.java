/**
 * The Location class represents the blueprint for the locations in the game.
 *
 * A location has a name, a description and a description if the player tries to look towards the direction from afar.
 * Furthermore, it contains information on the locations it neighbors to.
 * It also contains a list of the items which are situated in the location, as well as a list of the characters in the location.
 * A location can have orcs or not, and a flag is kept which signals whether the player has already visited the location or not.
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {

    private String name; //the name of the location
    private String description; //the description of the location. The player is shown this description when he looks around while in the location.
    private String descriptionFromAfar; //the description from afar of the location. The player is shown this description if he looks towards the location from a neighboring location.
    private Map<Direction, Location> neighboringLocations; //a map variable which maps the neighboring locations to their respective directions.
    private Map<Direction, Boolean> canTravelTo; //a map variable which stores what directions the can travel to from the location. (For example: {"NORTH": true, "EAST":false etc.)
    private List<Item> itemsInLocation; //a list of the items which are situated in the location.
    private List<GameCharacter> charactersInLocation; //a list of the characters which are situated in the location.
    private boolean hasOrcs = false; //boolean denoting whether the location has orcs in it or not. Is by default false.
    private boolean alreadyVisited = false; //boolean which stores whether the location has already been visited. Used in functionality: print location name if player looks to this location from a neighboring location only if player has already visited the location before.
    private Map<Item, String> toBeRemovedIfItemPickedUp;   //Once an item is picked up, it isn't in itemsInLocation anymore. The description of the location should then also change.
    //This is done through a Map<Item, String> toBeRemovedIfItemPickedUp. This holds the string segment to be removed from the location description if an item is picked up.

    /**
     * Constructor method.
     *
     * @param name
     */
    public Location(String name) {
        this.name = name;
        neighboringLocations = new HashMap<Direction, Location>();
        canTravelTo = new HashMap<Direction, Boolean>();
        itemsInLocation = new ArrayList<Item>();
        charactersInLocation = new ArrayList<GameCharacter>();
        toBeRemovedIfItemPickedUp = new HashMap<Item, String>();
    }

    /**
     * Sets the description of the location to the passed argument.
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description from afar to the passed argument.
     *
     * @param descriptionFromAfar
     */
    public void setDescriptionFromAfar(String descriptionFromAfar) {
        this.descriptionFromAfar = descriptionFromAfar;
    }

    /**
     * It adds the neighboring location in a certain direction to the location.
     * It also sets the initial reachability to that location.
     *
     * @param direction The direction the neighboring location is situated in
     * @param location The neighboring location
     * @param reachable Whether the player can travel to this location or not
     */
    public void addNeighboringLocation(Direction direction, Location location, boolean reachable) {
        neighboringLocations.put(direction, location);
        canTravelTo.put(direction, reachable);
        if (location.getNeighboringLocation(direction.getOppositeDirection()) == null) {  //If the neighboring location which has just been added, doesn't have a neighboring location yet in the direction of this location, the method adds this location as a neighboring location.
            location.addNeighboringLocation(direction.getOppositeDirection(), this, reachable);  //This thus effectively creates a back-and-forth connection/travel path.
        }
    }

    /**
     * Method sets the reachability to a certain direction to a certain value (true or false).
     *
     * @param direction
     * @param reachable Whether the player can travel in this direction or not
     */
    public void setReachability(Direction direction, Boolean reachable){
        canTravelTo.put(direction, reachable);
    }

    /**
     * Method returns whether there are orcs in the location or not (true or false).
     *
     * @return Whether the location has or not
     */
    public boolean hasOrcs() {
        return hasOrcs;
    }

    /**
     * Method sets the flag denoting whether the location has orcs or not, to a certain value.
     *
     * @param hasOrcs Whether the location has orcs or not
     */
    public void setHasOrcs(boolean hasOrcs) {
        this.hasOrcs = hasOrcs;
    }

    /**
     * Methods adds a specified item to the location.
     *
     * @param item
     */
    public void addItem(Item item) {
        itemsInLocation.add(item);
    }

    /**
     * Method removes the specified item from the location.
     * It also adjusts the location description if necessary, to reflect that the item is no longer in the location.
     *
     * @param item
     */
    public void removeItem(Item item) {
        itemsInLocation.remove(item);
        if (toBeRemovedIfItemPickedUp.get(item) != null) {
            description = description.replace(toBeRemovedIfItemPickedUp.get(item), ""); //Besides removing the item from the Items List, the method should also adjust the description of the location so the item doesn't appear in the description anymore.
        }
    }

    /**
     * Method adds a specified character to the location.
     *
     * @param character
     */
    public void addCharacter(GameCharacter character) {
        charactersInLocation.add(character);
    }

    /**
     * Method removes a specified character from the location.
     *
     * @param character
     */
    public void removeCharacter(GameCharacter character){
        charactersInLocation.remove(character);
    }

    /**
     * Returns the name of the location.
     *
     * @return name of the location
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the location.
     *
     * @return description of the location
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the description of the location when looked at from a neighboring location.
     *
     * @return description of the location from afar
     */
    public String getDescriptionFromAfar() {
        return descriptionFromAfar;
    }

    /**
     * Returns whether the location has already been visited by the player or not (true or false).
     *
     * @return whether the location has already been visited by the player or not
     */
    public boolean getAlreadyVisited() {
        return alreadyVisited;
    }

    /**
     * Sets the already visited flag to the specified value (true or false).
     *
     * @param alreadyVisited Whether the location has already been visited or not
     */
    public void setAlreadyVisited(boolean alreadyVisited){
        this.alreadyVisited=alreadyVisited;
    }

    /**
     * Returns the list of items situated in the location.
     *
     * @return the list of items in the location
     */
    public List<Item> getItemsInLocation() {
        return itemsInLocation;
    }

    /**
     * Returns the list of characters situated in the location.
     *
     * @return the list of character in the location
     */
    public List<GameCharacter> getCharactersInLocation() {
        return charactersInLocation;
    }

    /**
     * Returns whether the player can travel in the specified direction or not (true or false).
     *
     * @param direction
     * @return whether the player can travel in the specified direction or not
     */
    public boolean canTravelTo(Direction direction) {
        return canTravelTo.get(direction);
    }

    /**
     * Returns the location this location neighbors to in a specified direction.
     *
     * @param direction
     * @return neighboring location
     */
    public Location getNeighboringLocation(Direction direction) {
        return neighboringLocations.get(direction);
    }

    /**
     * Method sets a string segment which is to be removed from the location description
     * if the specified item is picked up.
     *
     * @param item
     * @param removalString The string segment to be removed from the location description if item is picked up
     */
    public void setStringToBeRemovedIfItemPickedUp(Item item, String removalString) {
        toBeRemovedIfItemPickedUp.put(item, removalString);
    }

    /**
     * Method handles the arriving of the player in a certain location.
     * It shows the name of the location and the description of the location to the player.
     * It also sets the alreadyVisited flag to true.
     */
    public void arrive() {
        System.out.println(this.name + ":");
        System.out.println(this.description);
        alreadyVisited = true;

    }
}
