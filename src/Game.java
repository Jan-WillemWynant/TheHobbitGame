import java.util.List;

public class Game {
    private Location currentLocation;
    private List<Item> itemsInInventory;
    private int daysLeft;
    private int strengthLevel;

    public static void main(String[] args){
        //
    }

    public void generatePlayingField(){
        //
    }

    public void commandHandler(){
        //
    }

    public void travel(Direction direction){
        if(currentLocation.canTravelTo(direction)){
            currentLocation=currentLocation.getNeighboringLocation(direction);
            currentLocation.arrive();
            daysLeft--; //assuming every step north, east etc. takes 1 day
        }
        else{
            System.out.println("You can not travel in this direction.\nType 'help directions' to find out what directions you can travel to from your current location.");
        }
    }

    public void look(Direction direction){
        System.out.println("To the " + direction.getDirectionName()+ " you see:");
        System.out.println(currentLocation.getNeighboringLocation(direction).getDescriptionFromAfar());
    }

    public void look(){
        //method which allows the player to 'look around'. So just give the description of the location again.
        System.out.println(currentLocation.getDescription());
    }

    public void examine(Item item){
        System.out.println(item.getDescription());
    }

    public void pickUp(Item item){
        if (item.canPickUp()){
            itemsInInventory.add(item);
            System.out.println("You have picked up the " + item.getName());
            item.setInInventory(true);
            currentLocation.removeItem(item); //the item is picked up so it should not be available anymore in the location anymore.
        }
        else{
            System.out.println("You can not pick up this item.");
        }
    }

    public void talkToCharacter(GameCharacter character){
        if (character.canTalk()){
            System.out.println(character.getDialogue());
        }
        else{
            System.out.println("This character can't talk.");
        }
    }

}
