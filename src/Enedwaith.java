import java.util.ArrayList;
import java.util.Arrays;

public class Enedwaith extends Location implements SpecificCommandHandler {

    private boolean inCave;
    private boolean swordHasBeenPickedUp;

    public Enedwaith(String name) {
        super(name);
        inCave = false;
        swordHasBeenPickedUp = false;
    }

    //This enum will contain the specific commands this location handles.
    private enum SpecificCommand2 {
        GO_INTO(new String[]{"go into the cave", "go into cave", "enter the cave", "enter cave"}),
        GO_OUT(new String[]{"go out the cave", "go out cave", "leave the cave", "leave cave"}),
        TRAVEL(new String[]{"travel"}),
        LOOK(new String[]{"look"});

        private String[] possibleDenominations;

        SpecificCommand2(String[] possibleDenominations) {
            this.possibleDenominations = possibleDenominations;
        }

        /*
         * This method checks if the command is invoked by the passed String[] command, which is the command inputted by the player in a String[] format.
         * The method returns true if the command is invoked by the player.
         */
        public boolean commandInvoked(String[] command) {
            for (String denomination : possibleDenominations) {//go through the possible denominations which could invoke this specificCommand
                boolean commandInvoked = true;
                String[] denominationStringAsStringArray = denomination.split("\\s+");
                if (denominationStringAsStringArray.length > command.length)
                    continue; //If the number of words in the possible in the possible denomination is higher tan the number of words in the inputted line, they definitely don't correspond
                for (int i = 0; i < denominationStringAsStringArray.length; i++) {//check if all words in the inputted line correspond to the possible denomination
                    if (!denominationStringAsStringArray[i].equals(command[i])) {//if one of the words in the inputted line does not match the possible denomination
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
        SpecificCommand2 inputCommand = null;
        for (SpecificCommand2 specificCommand : SpecificCommand2.values()) {
            if (specificCommand.commandInvoked(command)) {
                inputCommand = specificCommand;
                break;
            }
        }
        if (inputCommand == null) return false;

        switch (inputCommand) {//switch statement is a bit overkill, since item only implements one command, but could be useful if there was a need to add other commands to the item later.
            case GO_INTO:
                goInto();
                break;
            case GO_OUT:
                goOut();
                break;
            case TRAVEL:
                if (!inCave) {
                    return false;
                } else {
                    travel();
                    return true;
                }
            case LOOK:
                if (!inCave) {
                    return false;
                } else {
                    look();
                    return true;
                }
        }
        return true;
    }

    private void goInto() {
        if (inCave) {
            System.out.println("You are already inside the cave.");
        } else {
            inCave = true;
            Item elvishSword = new Item("Elvish Sword", true);
            elvishSword.setDescription("The sword is small and light, with a blade that glimmers faintly, even in the dimmest light. Its edges are razor-sharp, and elegant Elvish runes are etched along the blade. \nThe sword feels strong and alive, as if it carries the wisdom and craftsmanship of an ancient age.");
            elvishSword.setPossibleDenominations(new String[]{"sword", "blade", "the sword", "the blade"});
            this.addItem(elvishSword);
            System.out.println("Now, you are inside the cave.");
        }
    }

    private void goOut() {
        if (!inCave) {
            System.out.println("You have already left the cave.");
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
                swordHasBeenPickedUp = true;
            } else if (removedSword != null) {
                removeItem(removedSword);
            }
            System.out.println("You have left the cave now.");

        }
    }

    public void travel() {
        if (inCave) {
            System.out.println("You are in the cave. First exit the cave before you can travel.");
        }

    }

    public void look() {

        for (Item item : this.getItemsInLocation()) {
            if (item.getName().equals("Elvish Sword")) {
                System.out.println("The cave feels empty and still, its cool, damp air carrying the faint echoes of your movements. The cave feels empty and still, its cool, damp air carrying the faint echoes of your movements. The faint glimmer that once caught your attention is now gone, leaving only bare stone.");
                return;
            }
        }
        System.out.println("Inside the dimly lit cave, the air feels cool and slightly damp. The faint smell of earth and stone surrounds you. As your eyes adjust to the shadows, you notice something gleaming faintly near the back of the cave.");

    }
}