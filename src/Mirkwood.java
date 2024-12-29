/**
 * Mirkwood is a special location which implements the functionalities of climbing up a tree and climbing down.
 * Each time the player initially arrives in Mirkwood, he is completely disoriented by the thick forest.
 * Without his sense of direction, he can not travel north/east etc. and because of the thick forest he cannot look beyond the forest either.
 * In order to regain his sense of direction, he has to first climb the tree. After regaining his sense of direction, he can travel again.
 */

public class Mirkwood extends Location implements SpecificCommandHandler {
    private boolean upTree; //boolean storing whether the player is currently up the tree or not
    private boolean senseOfDirection; //boolean storing whether the player currently has a sense of direction (i.e. can travel out of the forest).

    /**
     * Constructor method.
     *
     * @param name
     */
    public Mirkwood(String name) {
        super(name);
        upTree = false;
        senseOfDirection = false;
    }

    /**
     * The arrive() method for Mirkwood is slightly different from the general arrive() method for locations,
     * since we always initially want to reset the senseOfDirection variable to false when the player arrives in Mirkwood.
     */
    @Override
    public void arrive(){
        super.arrive(); //Do the things related to arriving for every location.
        senseOfDirection=false; //Also reset the sense of direction variable to false.
    }

    /**
     * The specificCommand enum holds the specific commands the Mirkwood location handles.
     * A command can have a number of ways the player can invoke them, which are stored in the string array possibleDenominations.
     * The SpecificCommand enum provides the functionality of checking an inputted string against the possible denominations of the command.
     */
    private enum SpecificCommand {
        CLIMB_UP(new String[]{"climb up", "climb up tree", "climb tree", "climb a tree", "climb the tree", "go up the tree", "go up tree", "go tree up"}),
        CLIMB_DOWN(new String[]{"climb down", "go down","climb down tree", "climb down the tree", "climb out of tree", "climb out of the tree", "go down the tree"}),
        TRAVEL(new String[]{"travel"}), //player can only travel is he has regained his sense of direction (and has climbed down the tree again).
        LOOK(new String[]{"look"}); //look functionality depends on whether the player is up the tree or down on the ground.

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
            for (String denomination : possibleDenominations) { //Go through the possible denominations which could invoke this specificCommand
                boolean commandInvoked = true;
                String[] denominationStringAsStringArray = denomination.split("\\s+");
                if (denominationStringAsStringArray.length > command.length)
                    continue; //If the number of words in the possible in the possible denomination is higher tan the number of words in the inputted line, they definitely don't correspond
                for (int i = 0; i < denominationStringAsStringArray.length; i++) {//check if all words in the inputted line correspond to the possible denomination
                    if (!denominationStringAsStringArray[i].equals(command[i])) {//if one of the words in the inputted line does not match the possible denomination, the denomination and the input don't match
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

        switch (inputCommand) {
            case CLIMB_UP:
                climbTree();
                break;

            case CLIMB_DOWN:
                climbDown();
                break;

            case TRAVEL:
                if (senseOfDirection && !upTree) { //If the player has regained his sense of direction and is not up in the tree anymore, the general travel commands are applicable.
                    return false; //Thus, in this case the travel command should be handled by the general commands handled in the Game class, thus we return false.
                } else {
                    travel();
                    return true;
                }
            case LOOK:
                /*
                 * In the case of the LOOK command, it depends whether it should be handled by the Mirkwood class or the general commands in Game:
                 * If the command is to look around (so simply 'look'):
                 * -> general command is fine if player is on the ground and hasn't climbed the tree previously. Otherwise, handle in Mirkwood.
                 * If the command is to look to a certain direction (so for example: 'look north'):
                 * -> general command should be applied in Game class if the player is up in the tree.
                 * -> In other case: forest is too thick to look through, so command should be handled in Mirkwood class.
                 */

                if (command.length <= 1) { //This is the case where the player inputted just 'look'.
                    if(!upTree && !senseOfDirection){
                        return false; //If the player is not up the tree and hasn't regained his sense of direction, the description already in Mirkwood should be shown. Thus, we return false so the general look command in Game is executed.
                    } else {
                        look();
                        return true;
                    }

                } else { //this is the case where the player inputted 'look' plus something else (if the player used the command correctly, 'look' + a direction).

                    if (upTree){
                        return false; //If the player wants to look in a direction and is up the tree, the general looking to a direction is applicable and thus, we return false.
                    }
                    else{
                        //If the player is not up in the tree, he cannot look to directions beyond the forest. Thus, look command is handled in Mirkwood then.
                        //First we check if the player correctly used the look command (i.e. typed in 'look' + a valid direction name).
                        for (Direction direction : Direction.values()) { //Go through the directions and check which one the player wants to look in.
                            if ((command[1].equals(direction.getDirectionName()))) { //Checks if direction player wants to look in is 'north','east','south' or 'west'.
                                lookInDirection();
                                return true;
                            }
                        }
                        //If we get to this line, player has typed in 'look' plus something else than a direction. So we point out how to use the look command with a message.
                        System.out.println("You must type 'look' plus a direction ('north','east','south' or 'west') to look in a certain direction.");
                        return true;
                    }
                }

        }
        return true;
    }

    /**
     * Method handles the climbing up the tree.
     * It updates the upTree and senseOfDirection booleans and displays information to the player.
     */
    private void climbTree(){
        if (upTree) {
            System.out.println("You are already up in the tree.");
        }
        else{
            upTree = true;
            senseOfDirection=true;
            System.out.println("You are on top of the tree now. As you emerge above the dense forest, you regain your sense of direction and are finally able to look at what lies beyond the forest.");
        }
    }

    /**
     * Method handles the climbing down the tree.
     * It updates the upTree boolean and displays information to the player.
     */
    private void climbDown(){
        if(!upTree){
            System.out.println("You are already down on the ground.");
        }
        else{
            upTree=false;
            System.out.println("You are back on the ground now. \nWith the sense of direction you have regained from climbing the tree, you can continue your journey now.");
        }
    }

    /**
     * Method handles the travel command inputted by the player when the player either doesn't have a sense of direction or is still up in the tree.
     * It displays this information to the player.
     */
    private void travel(){
        if(!senseOfDirection && !upTree){ //this is the case where the player hasn't climbed the tree yet.
            System.out.println("You are completely disoriented by the thick forest. You have no idea what direction is north/east etc. \nYou first need to find a way to regain your sense of direction before you can continue travelling.");
        }else if (upTree){ //this is the case where the player still is up in the tree.
            System.out.println("You are still on top of the tree. You must first climb down to be able to continue your travels.");
        }
    }

    /**
     * Method handles the looking around functionality when the player either is up in the tree or is on the ground but has already regained his sense of direction (slightly different description than the usual one).
     * It displays this information to the player.
     */
    private void look(){
        if(upTree){
            System.out.println("You have emerged from the dense canopy. Sunlight warms your face. \nYou overlook the forest as the fresh air fills you with a renewed sense of hope and direction.");
        }else if (!upTree && senseOfDirection){ //the case where the player is on the ground, but has regained his sense of direction already. Looking around description differs slightly from original one.
            System.out.println("A shadowy, oppressive forest where ancient trees rise high, their gnarled branches forming a thick canopy that blocks out the sunlight. \nLuckily, you have regained a sense of direction by previously climbing a tree.");
        }

    }

    /**
     * Method handles the looking into a direction when the player is on the ground.
     * In this case, the player can not look beyond the forest and this information is displayed to him.
     */
    private void lookInDirection(){
        System.out.println("The forest is too dense and too thick to look through.");
    }

}
