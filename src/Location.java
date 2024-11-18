import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {

    private String name;
    private String description;
    private String descriptionFromAfar;
    private Map<Direction, Location> neighboringLocations;
    private Map<Direction, Boolean> canTravelTo;
    private List<Item> itemsInLocation;
    private List<GameCharacter> charactersInLocation;
    private boolean alreadyVisited=false; //boolean which stores whether the location has already been visited. (Possible functionality: print full description of location only when location visited for the first time)
    //Thought I just had. Once an item is picked up, it isn't in itemsInLocation anymore.
    //description of location should also change. Could do this through a Map<Item, String> toBeRemovedIfItemPickedUp. Which holds the string segment to be removed from the location description if an item is picked up.

    public Location(String name){
        this.name=name;
        neighboringLocations=new HashMap<Direction, Location>();
        canTravelTo=new HashMap<Direction, Boolean>();
        itemsInLocation= new ArrayList<Item>();
        charactersInLocation= new ArrayList<GameCharacter>();
    }

    public void setDescription(String description){
        this.description=description;
    }

    public void setDescriptionFromAfar(String descriptionFromAfar){
        this.descriptionFromAfar=descriptionFromAfar;
    }

    public void addNeighboringLocation(Direction direction, Location location){
        neighboringLocations.put(direction, location);
    }

    public void setReachability(Direction direction, Boolean reachable){
        canTravelTo.put(direction, reachable);
    }

    public void addItem(Item item){
        itemsInLocation.add(item);
    }

    public void removeItem(Item item){
        itemsInLocation.remove(item);
    }

    public void addCharacter(GameCharacter character){
        charactersInLocation.add(character);
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getDescriptionFromAfar(){
        return  descriptionFromAfar;
    }

    public List<Item> getItemsInLocation(){
        return itemsInLocation;
    }

    public List<GameCharacter> getCharactersInLocation() {
        return charactersInLocation;
    }

    public boolean canTravelTo(Direction direction){
        return canTravelTo.get(direction);
    }

    public Location getNeighboringLocation(Direction direction){
        return neighboringLocations.get(direction);
    }

    public void arrive(){
        System.out.println(this.name+ ": ");
        System.out.println(this.description);
        alreadyVisited=true;
    }
}
