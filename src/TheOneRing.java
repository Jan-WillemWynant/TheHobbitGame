import java.util.Scanner;

public class TheOneRing extends Item implements SpecificCommandHandler {

    private boolean ringOn;

    public TheOneRing() {

        super("The One Ring", true);
        this.ringOn = false;

    }

    //this enum will contain the specific commands this Item handles.
    private enum SpecificCommand {
        WEAR(new String[]{"put ring on", "put the ring on", "wear the ring", "wear ring"}),
        TAKE_OFF(new String[]{"take off the ring", "take off ring", "take the ring off", "remove the ring", "remove ring"}),
        PICK_UP(new String[]{"pick up ring", "pick up the ring", "take the ring", "take ring"});

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
        TheOneRing.SpecificCommand inputCommand = null;
        for (TheOneRing.SpecificCommand specificCommand : TheOneRing.SpecificCommand.values()) {
            if (specificCommand.commandInvoked(command)) {
                inputCommand = specificCommand;
                break;
            }
        }
        if (inputCommand == null) return false;

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

    private void wear(Game game) {
        if (ringOn) {
            System.out.println("You're already wearing the Ring.");
        } else if (this.inInventory) {
            game.setInvisibility(true);
            ringOn = true;
            System.out.println("You are wearing the Ring. Now, you are invisible. No one can see you.");
        } else {
            System.out.println("You must first pick up the ring before you can wear it.");
        }
    }

    private void takeOff(Game game) {
        if (ringOn) {
            game.setInvisibility(false);
            ringOn = false;
            System.out.println("You are not invisible anymore. Because you have taken off the Ring.");
        } else if (!inInventory) {
            System.out.println("You first need to pick up the ring before you can take it off.");
        } else {
            System.out.println("You first need to wear the ring before you can take it off.");
        }
    }

    private void pickUp(Game game) {

        System.out.println("The ring is the most valuable item. If you want to gain it, you should first solve 3 riddles correctly.");

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
        game.pickUp(this);

    }
}

