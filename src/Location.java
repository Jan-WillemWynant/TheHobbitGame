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
    private boolean hasOrcs=false;
    private boolean alreadyVisited=false; //boolean which stores whether the location has already been visited. Used in functionality: print location name if player looks to this direction only if player has already visited the location before (Possible other functionality: print full description of location only when location visited for the first time).
    //Once an item is picked up, it isn't in itemsInLocation anymore.
    //Description of location should also change. This is done through a Map<Item, String> toBeRemovedIfItemPickedUp. Which holds the string segment to be removed from the location description if an item is picked up.
    private Map<Item, String> toBeRemovedIfItemPickedUp;

    public Location(String name){
        this.name=name;
        neighboringLocations=new HashMap<Direction, Location>();
        canTravelTo=new HashMap<Direction, Boolean>();
        itemsInLocation= new ArrayList<Item>();
        charactersInLocation= new ArrayList<GameCharacter>();
        toBeRemovedIfItemPickedUp=new HashMap<Item,String>();
    }

    public void setDescription(String description){
        this.description=description;
    }

    public void setDescriptionFromAfar(String descriptionFromAfar){
        this.descriptionFromAfar=descriptionFromAfar;
    }

    public void addNeighboringLocation(Direction direction, Location location, boolean reachable){
        neighboringLocations.put(direction, location);
        canTravelTo.put(direction, reachable);
        if (location.getNeighboringLocation(direction.getOppositeDirection())==null){  //if the neighboring location which has just been added, doesn't have a neighboring location yet in the direction of this location, add this location as neighboring location.
            location.addNeighboringLocation(direction.getOppositeDirection(), this, reachable);  //this thus effectively creates a back-and-forth connection/travel path.
        }
    }

    //public void setReachability(Direction direction, Boolean reachable){
    //    canTravelTo.put(direction, reachable);
    //}

    public boolean hasOrcs(){
        return hasOrcs;
    }

    public void setHasOrcs(boolean hasOrcs){
        this.hasOrcs=hasOrcs;
    }

    public void addItem(Item item){
        itemsInLocation.add(item);
    }

    public void removeItem(Item item){
        itemsInLocation.remove(item);
        if (toBeRemovedIfItemPickedUp.get(item)!=null){
            description=description.replace(toBeRemovedIfItemPickedUp.get(item),""); //besides removing the item from the Items List, should also adjust the description of the location so the item doesn't appear in the description anymore.
        }
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

    public boolean getAlreadyVisited(){
        return alreadyVisited;
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

    public void setStringToBeRemovedIfItemPickedUp(Item item, String removalString){
        toBeRemovedIfItemPickedUp.put(item,removalString);
    }

    public void arrive(){
        System.out.println(this.name+ ":");
        System.out.println(this.description);
        alreadyVisited=true;

    }
}
