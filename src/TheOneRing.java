import java.util.Scanner;

public class TheOneRing extends Item implements SpecificCommandHandler {

    private boolean ringOn;
//    private boolean ringHasBeenPickedUp;

    public TheOneRing() {

        super("The One Ring", true);
        this.ringOn = false;
//        ringHasBeenPickedUp = false;
    }

    //this enum will contain the specific commands this Item handles.
    private enum SpecificCommand {
        WEAR(new String[]{"put ring on", "put the ring on", "wear the ring", "wear ring"}),
        TAKE_OFF(new String[]{"take off the ring","take off ring", "take the ring off", "remove the ring", "remove ring"}),
//        TRAVEL(new String[]{"travel"}),
//        LOOK(new String[]{"look"}),
        PICK_UP(new String[]{"pick up ring","pick up the ring", "take the ring", "take ring"});
//        TALK_TO(new String[]{"talk to", "speak with"});

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
//            case TRAVEL:
//                travel();
//                break;
//            case LOOK:
//                look();
//                break;
            case PICK_UP:
                if (inInventory){
                    return false; //if the ring is already picked up, no need to handle it in TheOneRing. The general pickUp method can just handle the command.
                }
                pickUp(game);
                break;
//            case TALK_TO:
//                talkToCharacter();
//                break;
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
        }
        else if(!inInventory){
            System.out.println("You first need to find the ring before you can take it off.");
        }
        else{
            System.out.println("You first need to wear the ring before you can take it off.");
        }
    }

//    private void travel() {
//        if (visibility) {
//            System.out.println("You are already invisible. First, take the ring off to be able travel to your next destination.");
//        }
//    }

//    public void look() {
//        if (!visibility) {
//            System.out.println("The vast, shadowed halls of Moria stretch endlessly before you, their grandeur both awe-inspiring and unsettling. Massive stone pillars rise like ancient trees, supporting a ceiling lost in darkness above. The air is heavy and cool, carrying the faint echoes of long-forgotten footsteps. As you walk through these halls, at the base of one of these pillars, you see a small golden ring, which seems to shimmer despite the lack of light source, as if it came straight out of the fire it was forged in. The only rays of light in this place seem to come through the gate on the east.");
//        } else if (visibility) {
//            System.out.println("As you slip the Ring onto your finger, the world blurs into shadows, and an eerie silence consumes all sound. A chill washes over you, and you feel invisible yet exposed, as if unseen eyes are watching your every move. The Ring’s power pulses through you—seductive, yet heavy with an ominous weight.");
//        }
//    }

//    private void talkToCharacter() {
//        if (ringOn) {
//            System.out.println("You are already invisible. First, take the ring off to be able talk to other characters.");
//        }
//    }

    private void pickUp(Game game) {

        System.out.println("The ring is the most valuable item. If you want to gain it, you should first solve 3 riddles correctly. \n First riddle: What has roots as nobody sees, \n Is taller than trees, \n Up, up it goes, \n And yet it never grows?");

        Scanner input = new Scanner(System.in);
        String[] correctAnswer = {"a mountain", "wind", "darkness"};
        String[] userAnswers = new String[3];

        while (true) {
            String answer = input.nextLine().toLowerCase();
            if (answer.equals(correctAnswer[0])) { //if player replies a mountain, the first riddle has been solved (i.e. continue with the outer while loop).
                userAnswers[0] = answer;
                break;
            } else {
                System.out.println("Your answer is not correct. Please try again.");
            }
        }

        System.out.println("Second riddle: Voiceless it cries, \n Wingless it flutters, \n Toothless it bites, \n Mouthless it mutters.");
        while (true) {
            String answer = input.nextLine().toLowerCase();
            if (answer.equals(correctAnswer[1])) { //if player replies wind, the second riddle has been solved (i.e. continue with the outer while loop).
                userAnswers[1] = answer;
                break;
            } else {
                System.out.println("Your answer is not correct. Please try again.");
            }
        }

        System.out.println("Third riddle: It cannot be seen, cannot be felt, \n Cannot be heard, cannot be smelt. \n It lies behind stars and under hills, \n And empty holes it fills. \n It comes first and follows after, \n Ends life, kills laughter.");
        while (true) {
            String answer = input.nextLine().toLowerCase();
            if (answer.equals(correctAnswer[2])) { //if player replies darkness, the third riddle has been solved (i.e. continue with the outer while loop).
                userAnswers[2] = answer;
                break;
            }
            else {
                System.out.println("Your answer is not correct. Please try again.");
            }
        }
        System.out.println("You have solved all riddles! The ring is now yours.");
//                ringHasBeenPickedUp = true;
        game.pickUp(this);

    }
}

