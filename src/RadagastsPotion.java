/**
 * Radagast's potion is a special item which implements one location-specific functionality: 'drink potion'
 * Drinking the potion increases the player's strength.
 */

public class RadagastsPotion extends Item implements SpecificCommandHandler {
    private boolean empty; //this boolean is needed, because once the player has drunk it, shouldn't be able to drink it again.
    private int potency; //the amount by which drinking the potion, increases strength of the player.

    public RadagastsPotion() {
        super("Radagast's potion", true);
        empty = false;
        potency = 10;
    }

    /**
     * The specificCommand enum holds the specific commands the Radagast's potion item handles.
     * A command can have a number of ways the player can invoke them, which are stored in the string array possibleDenominations.
     * The SpecificCommand enum provides the functionality of checking an inputted string against the possible denominations of the command.
     */
    private enum SpecificCommand {
        DRINK(new String[]{"drink potion", "drink the potion", "drink bottle", "drink the bottle", "drink radagast's potion"});

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
            for (String denomination : possibleDenominations) {  //go through the possible denominations which could invoke this specificCommand
                boolean commandInvoked = true;
                String[] denominationAsStringArray = denomination.split("\\s+");
                if (denominationAsStringArray.length > command.length)
                    continue; //if the number of words in the possible denomination is higher than the number of words in the inputted line, they definitely don't correspond.
                for (int i = 0; i < denominationAsStringArray.length; i++) {  //check if all words in the inputted line correspond to the possible denomination
                    if (!denominationAsStringArray[i].equals(command[i])) { //if one of the words in the inputted line does not match the possible denomination
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
            if (specificCommand.commandInvoked(command)) {
                inputCommand = specificCommand;
                break;
            }
        }

        if (inputCommand == null) return false; //If none of the specific commands are invoked, we return false since the class does not handle the inputted command.

        switch (inputCommand) { //switch statement is a bit overkill, since item only implements one command, but could be useful if there was a need to add other commands to the item later.
            case DRINK:
                drink(game);
                break;
        }
        return true;
    }

    /**
     * Method executes the drink potion command.
     *
     * @param game
     */
    private void drink(Game game) {
        if (!this.inInventory) {
            System.out.println("You should first pick up the bottle before being able to drink it.");
        } else if (empty) {
            System.out.println("The bottle is empty.");
        } else {
            game.increaseStrength(potency);
            empty = true;
            System.out.println("You immediately feel a little stronger.");
        }
    }
}
