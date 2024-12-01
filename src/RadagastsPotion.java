import java.util.Arrays;

public class RadagastsPotion extends Item implements SpecificCommandHandler {
    //Radagast's potion will allow the player to increase his strength.
    //The class will implement one item-specific functionality: 'drink potion'.
    private boolean empty; //will need this boolean, because once the player has drunk it, shouldn't be able to drink it again.
    private int potency; //the amount by which drinking the potion, increases strength of the player.

    public RadagastsPotion() {
        super("Radagast's potion", true);
        empty = false;
        potency = 10;
    }

    //this enum will contain the specific commands this Item handles.
    private enum SpecificCommand {
        DRINK(new String[]{"drink potion", "drink the potion", "drink bottle", "drink the bottle", "drink radagast's potion"});

        private String[] possibleDenominations;

        SpecificCommand(String[] possibleDenominations) {
            this.possibleDenominations = possibleDenominations;
        }

        /*
         * This method checks if the command is invoked by the passed String[] command, which is the command inputted by the player in a String[] format.
         * The method returns true if the command is invoked by the player.
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

    @Override
    public boolean specificCommandHandler(String[] command, Game game) {
        SpecificCommand inputCommand = null;
        for (SpecificCommand specificCommand : SpecificCommand.values()) {
            if (specificCommand.commandInvoked(command)) {
                inputCommand = specificCommand;
                break;
            }
        }

        if (inputCommand == null) return false;

        switch (inputCommand) { //switch statement is a bit overkill, since item only implements one command, but could be useful if there was a need to add other commands to the item later.
            case DRINK:
                drink(game);
        }
        return true;
    }

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
