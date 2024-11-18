public class Item {
    private String name;
    private String description;
    private boolean inInventory=false;
    private boolean canPickUp;

    public Item(String name, boolean canPickUp){
        this.name=name;
        this.canPickUp=canPickUp;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public void setInInventory(boolean inInventory){
        this.inInventory=inInventory;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public boolean canPickUp(){
        return canPickUp;
    }

}
