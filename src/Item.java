public class Item {
    private String name;
    private String[] possibleDenominations; //this String array contains the possible denominations with which the player could try to refer to the item. For example to pick up the Elvis Sword player would probably type either 'pick up sword' or 'pick up blade'
    private String description;
    protected boolean inInventory=false;
    private boolean canPickUp;

    public Item(String name, boolean canPickUp){
        this.name=name;
        this.canPickUp=canPickUp;
    }

    public void setPossibleDenominations(String[] possibleDenominations){
        this.possibleDenominations=possibleDenominations;
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

    /*
     * Takes an inputted string (as an array of Strings) and checks it against the possible denominations for this item.
     * Returns true if the inputted string refers to the item.
     */
    public boolean refersToItem(String[] input){
        //We first check the String[] input against the name of the item.
        boolean matchWithName=true;
        String[] nameAsStringArray = name.toLowerCase().split("\\s+");
        if (nameAsStringArray.length > input.length) //if the number of words in the name of the item is higher than the number of words in the input, will never match.
            matchWithName=false;
        else {
            for (int i = 0; i < nameAsStringArray.length; i++) {  //check if all words in the inputted line correspond to the name.
                if (!nameAsStringArray[i].equals(input[i])) { //if one of the words in the inputted line does not match the name.
                    matchWithName = false;
                    break;
                }
            }
        }
        if (matchWithName) return true;

        //Now we check the String[] input against the other possible denominations.
        if (possibleDenominations==null) return false; //first check if there are other possible denominations
        for (String denomination: possibleDenominations) {  //go through the possible denominations which could refer to this item/
            boolean itemReferred = true;
            String[] denominationAsStringArray = denomination.toLowerCase().split("\\s+");
            if (denominationAsStringArray.length > input.length)
                continue; //if the number of words in the possible denomination is higher than the number of words in the input.
            for (int i = 0; i < denominationAsStringArray.length; i++) {  //check if all words in the inputted line correspond to the possible denomination
                if (!denominationAsStringArray[i].equals(input[i])) { //if one of the words in the inputted line does not match the possible denomination
                    itemReferred = false;
                    break;
                }
            }
            if (itemReferred) return true;
        }

        return false;
    }

    public String getDescription(){
        return description;
    }

    public boolean canPickUp(){
        return canPickUp;
    }

    @Override
    public String toString() {
        return name; //the toString() method merely returns the name (no description).
    }
}
