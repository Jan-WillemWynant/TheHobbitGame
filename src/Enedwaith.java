/*
 * Enedwaith is a special location which handles commands specific to the location Enedwaith.
 * There is a cave in Enedwaith which the player can enter and exit.
 * Once in the cave, the player can see and pick up the Elvish Sword (the Elvish Sword which turns blue when approaching orcs).
 */

public class Enedwaith extends Location implements SpecificCommandHandler {

    private boolean inCave; //boolean which stores whether the player is currently in the cave or not.
    private boolean swordHasBeenPickedUp; //boolean which stores whether the player has already picked up the sword in a previous visit to the cave.

    /**
     * Constructor method.
     *
     * @param name
     */
    public Enedwaith(String name) {
        super(name);
        inCave = false;
        swordHasBeenPickedUp = false;
    }

    /**
     * The specificCommand enum holds the specific commands the Enedwaith location handles.
     * A command can have a number of ways the player can invoke them, which are stored in the string array possibleDenominations.
     * The SpecificCommand enum provides the functionality of checking an inputted string against the possible denominations of the command.
     */
    private enum SpecificCommand {
        GO_INTO(new String[]{"go into the cave", "go into cave", "enter the cave", "enter cave","go in cave","go in the cave"}),
        GO_OUT(new String[]{"go out the cave", "go out cave", "go out of cave", "go out of the cave", "leave the cave", "leave cave","exit cave", "exit the cave"}),
        TRAVEL(new String[]{"travel"}), //When the player is in the cave, the general travel command isn't applicable.
        LOOK(new String[]{"look"}); //When the player is in the cave, the general look command isn't applicable.

        private String[] possibleDenominations;

        /**
         * Constructor method.
         *
         * @param possibleDenominations
         */
        SpecificCommand(String[] possibleDenominations) {
            this.possibleDenominations = possibleDenominations;
        }

        /**
         * This method checks if the command is invoked by the passed String[] command, which is the command inputted by the player in a String[] format.
         * The method returns true if the command is invoked by the player.
         *
         * @param command The command inputted by the player
         * @return true if the command is invoked by the input, false otherwise
         */
        public boolean commandInvoked(String[] command) {
            for (String denomination : possibleDenominations) {//go through the possible denominations which could invoke this specificCommand
                boolean commandInvoked = true;
                String[] denominationStringAsStringArray = denomination.split("\\s+");
                if (denominationStringAsStringArray.length > command.length)
                    continue; //If the number of words in the possible in the possible denomination is higher tan the number of words in the inputted line, they definitely don't correspond
                for (int i = 0; i < denominationStringAsStringArray.length; i++) { //check if all words in the inputted line correspond to the possible denomination
                    if (!denominationStringAsStringArray[i].equals(command[i])) { //if one of the words in the inputted line does not match the possible denomination, the denomination and the input don't match.
                        commandInvoked = false;
                    }
                }
                if (commandInvoked) return true;
            }
            return false;
        }
    }

    /**
     * The specificCommandHandler method receives the inputted command as an argument, and checks if this class handles the inputted command.
     * If the class handles the command, it executes the right methods in the class to reflect what the player wants to do and returns true.
     * If this involves changes to the game state, the passed Game instance is used to reflect this.
     *
     * @param command The inputted command
     * @param game The game instance which calls the specificCommandHandler() method
     * @return true if the method handled the command, false otherwise
     */
    @Override
    public boolean specificCommandHandler(String[] command, Game game) {

        //The method first goes through the specific commands it handles (which are contained in the specificCommand enum), and checks the inputted command
        //against the denominations of the specific commands.
        SpecificCommand inputCommand = null;
        for (SpecificCommand specificCommand : SpecificCommand.values()) {
            if (specificCommand.commandInvoked(command)) { //If one of the specific commands is invoked by the inputted command, we set the inputCommand variable to this specific command.
                inputCommand = specificCommand;
                break;
            }
        }
        if (inputCommand == null) return false; //If none of the specific commands are invoked, we return false since the class does not handle the inputted command.

        switch (inputCommand) {
            case GO_INTO:
                goInto();
                break;
            case GO_OUT:
                goOut();
                break;
            case TRAVEL:
                if (!inCave) {  //if the player is not in the cave, the general travel commands in Game should be applied. So if the player is not in the cave, the travel command is not handled in Enedwaith.
                    return false;
                } else {
                    travel();
                    return true;
                }
            case LOOK:
                if (!inCave) { //if the player is not in the cave, the general look commands in Game should be applied. So if the player is not in the cave, the look command is not handled in Enedwaith.
                    return false;
                } else {
                    look();
                    return true;
                }
        }
        return true;
    }

    /**
     * Method handles the entering into the cave.
     * Once the player enters the cave, he should be able to see the Elvish Sword.
     * So, when the player enters the cave, the sword is added to the Enedwaith location.
     */
    private void goInto() {
        if (inCave) {
            System.out.println("You are already inside the cave.");
        } else {
            inCave = true;
            if (!swordHasBeenPickedUp) { //If the player hasn't picked up the sword in a previous visit to the cave, the Elvish Sword is added to the Enedwaith location when the player enters the cave.
                Item elvishSword = new Item("Elvish Sword", true);
                elvishSword.setDescription("The sword is small and light, with a blade that glimmers faintly, even in the dimmest light. Its edges are razor-sharp, and elegant Elvish runes are etched along the blade. \nThe sword feels strong and alive, as if it carries the wisdom and craftsmanship of an ancient age.");
                elvishSword.setPossibleDenominations(new String[]{"sword", "blade", "the sword", "the blade"});
                this.addItem(elvishSword);
                System.out.println("You are inside the cave now.");
            }
        }
    }

    /**
     * Method handles the going out of the cave.
     * If at the moment the player leaves the cave, the sword is not in the cave anymore, the sword has been picked up in the meantime.
     * If the sword is still in the cave, it has to be removed from the Enedwaith location at the point the player leaves the cave.
     */
    private void goOut() {
        if (!inCave) {
            System.out.println("You are already out of the cave.");
        } else {
            inCave = false;
            Item removedSword = null;
            for (Item item : this.getItemsInLocation()) {
                if (item.getName().equals("Elvish Sword")) {
                    removedSword = item;
                    break;
                }
            }
            if (removedSword == null) {
                swordHasBeenPickedUp = true; //if at the point the player leaves the location, the sword is no longer there, it means he has picked it up in the meantime.
            } else if (removedSword != null) {  //if the sword is still there when the player leaves the cave, we have to remove it from the Enedwaith location.
                removeItem(removedSword);
            }
            System.out.println("You have left the cave now.");

        }
    }

    /**
     * Executes the travel command when the player is in the cave.
     */
    public void travel() {
        if (inCave) {
            System.out.println("You are in the cave. First exit the cave before you can travel.");
        }
    }

    /**
     * Executes the look command when the player is in the cave.
     */
    public void look() {
        //the look method gives the description of the cave to the player (note that when the player types 'look north', 'look east', he also receives this description. He has to leave the cave to get the descriptions of the locations north/east... of Enedwaith . The description of the cave is different dependent on whether the sword is still there or not.
        for (Item item : this.getItemsInLocation()) {  //so first loop through itemsInLocation and check if the Elvish Sword is still there.
            if (item.getName().equals("Elvish Sword")) {
                System.out.println("Inside the dimly lit cave, the air feels cool and slightly damp. The faint smell of earth and stone surrounds you. As your eyes adjust to the shadows, you notice something gleaming faintly near the back of the cave.\nIt's a sword which reflects the little light in the cave.");
                return;
            }
        }
        System.out.println("The cave feels empty and still, its cool, damp air carrying the faint echoes of your movements. The faint glimmer that once caught your attention is now gone, leaving only bare stone.");

    }
}