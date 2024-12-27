/**
 * The Item class represents the blueprint for the items in the game.
 *
 * An item has a name, and an array of possible denominations.
 * These possible denominations represent the ways in which the player can refer to the object
 * when trying to examine or pick it up.
 * An item also has a description, a flag variable which stores whether the item is currently
 * in inventory, and a flag variable denoting whether the player can pick the item up or not.
 *
 */

public class Item {
    private String name; //the name of the item
    private String[] possibleDenominations; //String array which contains the possible denominations with which the player can refer to the item. For example to pick up the Elvis Sword the player would probably type either 'pick up sword' or 'pick up blade'. So we can account both for the denominations 'sword' and 'blade'.
    private String description; //the description of the item. Is shown when the player examines the item.
    protected boolean inInventory = false; //boolean storing whether the item is currently in inventory or not.
    private boolean canPickUp; //boolean storing whether the item can be picked up by the player or not.

    /**
     * Constructor method.
     *
     * @param name Name of the item
     * @param canPickUp Whether the player can pick the item up or not
     */
    public Item(String name, boolean canPickUp) {
        this.name = name;
        this.canPickUp = canPickUp;
    }

    /**
     * Sets the string array of possible denominations for the item.
     *
     * @param possibleDenominations The ways in which the player can refer to the item.
     */
    public void setPossibleDenominations(String[] possibleDenominations) {
        this.possibleDenominations = possibleDenominations;
    }

    /**
     * Sets the description of the item to the specified string.
     *
     * @param description Description of the item
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the in inventory flag to the specified value (true or false).
     * For example, to set in inventory to true when the player picks up the item.
     *
     * @param inInventory Whether the item is in the inventory of the player or not
     */
    public void setInInventory(boolean inInventory) {
        this.inInventory = inInventory;
    }

    /**
     * Returns the name of the item.
     *
     * @return name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Takes an inputted string (as an array of Strings) and checks it against the possible denominations for this item.
     * Returns true if the inputted string refers to the item.
     *
     * @param input The inputted string
     * @return True if the inputted string refers to the item, false otherwise.
     */
    public boolean refersToItem(String[] input) {
        //We first check the String[] input against the name of the item.
        boolean matchWithName = true;
        String[] nameAsStringArray = name.toLowerCase().split("\\s+"); //First convert the name variable to lowercase, and split it into a string array with whitespace as the delimiter.
        if (nameAsStringArray.length > input.length) //If the number of words in the name of the item is higher than the number of words in the input, the two will never match.
            matchWithName = false;
        else {
            for (int i = 0; i < nameAsStringArray.length; i++) {  //Check if all words in the inputted line correspond to the name.
                if (!nameAsStringArray[i].equals(input[i])) { //If one of the words in the inputted line does not match the name, matchWithName is set to false.
                    matchWithName = false;
                    break;
                }
            }
        }
        if (matchWithName) return true; //If the inputted string corresponds with the name of the item, the player has successfully referred to the item.

        //Now we check the String[] input against the other possible denominations.
        if (possibleDenominations == null) return false; //First check if there are other possible denominations
        for (String denomination : possibleDenominations) {  //Go through the possible denominations which could refer to this item.
            boolean itemReferred = true;
            String[] denominationAsStringArray = denomination.toLowerCase().split("\\s+"); //Convert the possible denomination to lower case, and split it into a string array with whitespace as the delimiter.
            if (denominationAsStringArray.length > input.length)
                continue; //If the number of words in the possible denomination is higher than the number of words in the input, the two will never match.
            for (int i = 0; i < denominationAsStringArray.length; i++) {  //Check if all words in the inputted line correspond to the possible denomination
                if (!denominationAsStringArray[i].equals(input[i])) { //If one of the words in the inputted line does not match the possible denomination, itemReferred is set to false.
                    itemReferred = false;
                    break;
                }
            }
            if (itemReferred) return true;
        }

        //If we get to this line, none of the possible denominations matched with the inputted string, so we return false.
        return false;
    }

    /**
     * Returns the description of the item.
     * Player is shown this description when he examines the item.
     *
     * @return the description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the player can pick up the item or not.
     *
     * @return true if the player can pick up the item, false otherwise.
     */
    public boolean canPickUp() {
        return canPickUp;
    }

    @Override
    public String toString() {
        //The toString() method merely returns the name of the item (no description).
        //Is used for example when the player is shown what items he has in inventory.
        return name;
    }

}
