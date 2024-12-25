/*
 * Special location which will allow the functionality of 'climb tree'.
 * Class still needs to be implemented.
 */

public class Mirkwood extends Location implements SpecificCommandHandler {
    private boolean upTree;

    public Mirkwood(String name) {
        super(name);
        upTree = false;
    }

    //This enum will contain the specific commands this location handles.
    private enum SpecificCommand {
        CLIMB_TREE(new String[]{"climb tree", "climb the tree", "go up the tree", "go up tree", "go the tree up", "go tree up", "rise the tree", "rise tree"}),
        TRAVEL(new String[]{"travel"}),
        LOOK(new String[]{"look"});
        private String[] possibleDenominations;

        SpecificCommand(String[] possibleDenominations) {
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
        SpecificCommand inputCommand = null;
        for (SpecificCommand specificCommand : SpecificCommand.values()) {
            if (specificCommand.commandInvoked(command)) {
                inputCommand = specificCommand;
                break;
            }
        }
        if (inputCommand == null) return false;

        switch (inputCommand) {
            case CLIMB_TREE:
                climbTree();
                break;

            case TRAVEL:
                if (upTree) {  //if the player is in the Mirkwood, the general travel commands in Game cannot be applied. So if the player is in the Mirkwood, the travel command is not handled in Enedwaith.
                    return false;
                } else {
                    travel();
                    return true;
                }
            case LOOK:
                if (upTree) { //if the player is in the Mirkwood, the general look commands in Game cannot be applied. So if the player is in the Mirkwood, the look command is not handled in Enedwaith.
                    return false;
                } else {
                    look();
                    return true;
                }
        }
        return true;
    }

    private void climbTree(){
        if (upTree) {
            System.out.println("You are already on top of the tree.");
        }else{
            upTree = true;
            System.out.println("Now, you are on top of the tree and regain your sense of direction.");
        }
    }

    private void travel(){
        if(!upTree){
            System.out.println("You are already in the Mirkwood, a shadowy, dark and oppressive forest. Your sense of direction does not work here. You first need to be in an elevation!");
        }else if (upTree){
            System.out.println("You are already on top of the tree and regain your sense of direction.");
            upTree = false;
        }
    }

    private void look(){
        if(!upTree){
            System.out.println("You are already in the Mirkwood, a shadowy, dark and oppressive forest. Your sense of direction does not work here. You first need to be in an elevation!");
        }else if (upTree){
            System.out.println("You are already on top of the tree and regain your sense of direction.");
        }

    }
}
