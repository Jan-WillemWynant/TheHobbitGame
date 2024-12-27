/**
 * TheOneRing is a special item which implements the functionalities of wearing the ring, taking it off
 * and because it is the most valuable item, picking up the ring is also subject to a greater challenge than
 * picking up items usually is.
 * While the player wears the ring, he is invisible. (which doesn't protect him from orcs, but does help him get past the dragon Smaug)
 */

import java.util.Scanner;

public class TheOneRing extends Item implements SpecificCommandHandler {

    private boolean ringOn; //boolean denoting whether the player currently wears the ring or not.

    /**
     * Constructor method.
     */
    public TheOneRing() {

        super("The One Ring", true);
        this.ringOn = false; //ring is initially off.

    }

    /**
     * The specificCommand enum holds the specific commands TheOneRing item handles.
     * A command can have a number of ways the player can invoke them, which are stored in the string array possibleDenominations.
     * The SpecificCommand enum provides the functionality of checking an inputted string against the possible denominations of the command.
     */
    private enum SpecificCommand {
        WEAR(new String[]{"put ring on", "put the ring on", "wear the ring", "wear ring"}),
        TAKE_OFF(new String[]{"take off the ring", "take off ring", "take the ring off", "remove the ring", "remove ring"}),
        PICK_UP(new String[]{"pick up ring", "pick up the ring", "take the ring", "take ring"});

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
                    if (!denominationAsStringArray[i].equals(command[i])) { //if one of the words in the inputted line does not match the possible denomination, the denomination and input don't match.
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
        TheOneRing.SpecificCommand inputCommand = null;
        for (TheOneRing.SpecificCommand specificCommand : TheOneRing.SpecificCommand.values()) {
            if (specificCommand.commandInvoked(command)) {
                inputCommand = specificCommand;
                break;
            }
        }
        if (inputCommand == null) return false; //If none of the specific commands are invoked, we return false since the class does not handle the inputted command.

        switch (inputCommand) {
            case WEAR:
                wear(game);
                break;
            case TAKE_OFF:
                takeOff(game);
                break;
            case PICK_UP:
                if (inInventory) {
                    return false; //if the ring is already picked up, no need to handle it in TheOneRing. The general pickUp method can just handle the command.
                }
                pickUp(game);
                break;
        }
        return true;
    }

    /**
     * Method handles the wearing of the ring.
     * Once the player wears the ring, he is set to be invisible.
     *
     * @param game The game instance
     */
    private void wear(Game game) {
        if (ringOn) {
            System.out.println("You're already wearing the Ring.");
        } else if (this.inInventory) {
            game.setInvisibility(true);
            ringOn = true;
            System.out.println("You are wearing the Ring now. You are invisible now. No one can see you.");
        } else {
            System.out.println("You must first pick up the ring before you can wear it.");
        }
    }

    /**
     * Method handles the taking off of the ring.
     * Once the player wears the ring, his invisibility is once again set to false.
     *
     * @param game The game instance
     */
    private void takeOff(Game game) {
        if (ringOn) {
            game.setInvisibility(false);
            ringOn = false;
            System.out.println("You have taken off the Ring. You are not invisible anymore.");
        } else if (!inInventory) {
            System.out.println("You first need to pick up the ring before you can take it off.");
        } else {
            System.out.println("You first need to wear the ring before you can take it off.");
        }
    }

    /**
     * Method handles the picking up of the ring.
     * Since the ring is the most precious item, the player first has to solve a challenge before he can pick up the ring.
     *
     * @param game The game instance
     */
    private void pickUp(Game game) {

        System.out.println("The ring is the most valuable item. If you want to gain it, you first have to solve 3 riddles correctly.");

        String[] riddles = new String[]{" First riddle: What has roots as nobody sees, \n Is taller than trees, \n Up, up it goes, \n And yet it never grows?", "Second riddle: Voiceless it cries, \n Wingless it flutters, \n Toothless it bites, \n Mouthless it mutters", "Third riddle: It cannot be seen, cannot be felt, \n Cannot be heard, cannot be smelt. \n It lies behind stars and under hills, \n And empty holes it fills. \n It comes first and follows after, \n Ends life, kills laughter."};

        Scanner input = new Scanner(System.in);
        String[][] correctAnswers = new String[][]{{"a mountain", "mountain", "mountains", "the mountain"}, {"wind", "the wind", "a wind"}, {"darkness", "the darkness", "the dark", "dark"}};

        for (int i = 0; i < riddles.length; i++) {
            System.out.println(riddles[i]);
            while (true) {
                String answer = input.nextLine().trim().toLowerCase();
                boolean correct = false;
                for (String correctAnswer : correctAnswers[i]) {
                    if (answer.equals(correctAnswer)) { //check if the inputted answer corresponds to one of the correct answers.
                        correct = true;
                        break;
                    }
                }
                if (correct) {
                    break;
                } else {
                    System.out.println("Your answer is not correct. Please try again.");
                }
            }
        }

        System.out.println("You have solved all riddles! The ring is now yours.");
        game.pickUp(this); //Since, besides the riddles, the picking up of the ring happens exactly the same as picking up other items, we can now just call the general functionality of picking up an item with the ring as the argument.

    }
}

