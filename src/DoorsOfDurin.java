/**
 * The Doors of Durin are a special location. Initially, the doors are closed,
 * which means the player can't travel to the mines of Moria to the east.
 * The player has to talk to Gandalf to open the doors.
 *
 * Gandalf will only open the doors if Bilbo has already picked up the Elvish Sword.
 * Otherwise, he will urge him to go back west and first find the sword.
 * The dialogue Gandalf says if the player doesn't have the sword yet, is in the GameCharacter Gandalf.
 *
 * Once Bilbo talks to Gandalf and already has the sword, Gandalf opens the doors and leaves.
 * So the description and descriptionFromAfar should naturally also be different after the doors have been opened.
 *
 * To enable these location-specific functionalities, the class implements SpecificCommandHandler.
 */

public class DoorsOfDurin extends Location implements SpecificCommandHandler {

    private boolean doorsOpened; //boolean which stores whether the doors of Durin have already been opened or not.

    /**
     * Constructor method.
     *
     * @param name
     */
    public DoorsOfDurin(String name) {
        super(name);
        doorsOpened=false; //the doors are initially closed.
    }

    /**
     * The specificCommand enum holds the specific commands the DoorsOfDurin location handles.
     * A command can have a number of ways the player can invoke them, which are stored in the string array possibleDenominations.
     * The SpecificCommand enum provides the functionality of checking an inputted string against the possible denominations of the command.
     */
    private enum SpecificCommand {
        LOOK_EAST(new String[]{"look east"}), //the door is to the east. If the doors are not opened, DoorsOfDurin should handle this command.
        TALK_TO_GANDALF(new String[]{"talk to gandalf", "speak with gandalf"});

        private String[] possibleDenominations; //array of strings contains the possible ways a player can invoke a certain command.

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
            for (String denomination : possibleDenominations) {  //Go through the possible denominations which could invoke this specificCommand
                boolean commandInvoked = true;
                String[] denominationAsStringArray = denomination.split("\\s+"); //Split the denomination to a string array, with whitespace as the delimiter
                if (denominationAsStringArray.length > command.length)
                    continue; //If the number of words in the possible denomination is higher than the number of words in the inputted line, they definitely don't correspond.
                for (int i = 0; i < denominationAsStringArray.length; i++) {  //Check if all words in the inputted line correspond to the possible denomination
                    if (!denominationAsStringArray[i].equals(command[i])) { //Cf one of the words in the inputted line does not match the possible denomination, the input doesn't match with the denomination.
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
            case LOOK_EAST:
                if(doorsOpened){ //Once the doors are opened, the general functionality of looking east is okay. So the command 'look east' should then just be handled in the Game class by the general looking methods.
                    return false;
                }
                else{
                    lookEast();
                    return true;
                }

            case TALK_TO_GANDALF:
                /*
                 * If the player wants to talk to Gandalf, it depends. Once the doors have been opened, Gandalf will no longer be there, so DoorsOfDurin should not handle the 'talk to gandalf' command.
                 * If the doors are not yet opened, we check if the player has already picked up the Elvish Sword.
                 * If he hasn't picked up the sword yet, the dialogue in the Gandalf GameCharacter object is okay, so the general method talkTo(Character) in Game is okay
                 * -> in this case DoorsOfDurin does not handle the 'talk to gandalf' command.
                 * if the player has already picked up the Elvish Sword, DoorsOfDurin handles the talking to gandalf, opens the door and Gandalf leaves.
                 */

                if (doorsOpened) return false;

                for (Item item : game.getItemsInInventory()) { //Go through the items currently in inventory, and check if the Elvish Sword is in there.
                    if (item.getName().equals("Elvish Sword")) {
                        talkToGandalf(); //If the player has the Elvish Sword already, we call the custom/specific talkToGandalf() method.
                        return true;
                    }
                }
                //If we get to this line, the player hasn't picked up the elvish sword yet, so the standard dialogue of Gandalf is fine.
                //So the general talkTo(Character) method in Game should handle this.
                return false;


        }
        return true;
    }

    /**
     * The method handles looking to the east from DoorsOfDurin when the doors are not yet opened.
     */
    public void lookEast(){
        System.out.println("To the east, the Doors of Durin stand between you and the Mines of Moria. Etched into the smooth rock are faint, intricate designs that shimmer in the right light, forming a hidden archway. \nSilent and imposing, the doors seem to wait, unmoving, for the right words to grant passage.");
    }

    /**
     * The method handles talking to the Gandalf when the doors are not yet opened
     * and the player has already picked up the Elvish Sword.
     * Besides printing Gandalf's updated dialogue, it opens the doors
     * by establishing the connection to the east to Moria.
     */
    public void talkToGandalf(){
        System.out.println("Hello Bilbo, I see you have already picked up a sword of Elvish make. Such a sword will glow blue as you approach orcs. Use it wisely to avoid encounters with these vicious creatures. \nYour road is as follows. Travel through the Mines of Moria to the woods of Lorien. From there on, you must find a way to get across Anduin River to Mirkwood.\nFrom there, you will find your way to Erebor.");
        System.out.println("The dragon Smaug has already awakened in Erebor though, my friend. You must get past him unseen. For that, you must pick up this valuable ring in Moria. \nWearing it grants you invisibility. It does however not prevent orcs from smelling you, bear that in mind.");
        System.out.println();
        System.out.println("Gandalf is thinking.");
        System.out.println();
        System.out.println("Now, all that is left for me, is to open this damn door. The door says 'speak friend, and enter'. Now, how do I open this damn door, my dear friend. \nThat is it! Speak friend and enter. Mellon, the Elvish word for friend. \nGood luck on your journey, Mellon.");
        System.out.println();
        System.out.println("The massive door opens and Gandalf vanishes into thin air.");

        doorsOpened=true;
        this.setReachability(Direction.EAST,true); //Now that the doors are opened, there should be a connection to the location to the east, Moria.
        Location moria=this.getNeighboringLocation(Direction.EAST);
        //Originally in the location Moria, the location to the west is just dark halls.
        //Now that the doors are opened, this should be overwritten and Moria should have DoorsOfDurin to the west.
        moria.addNeighboringLocation(Direction.WEST,this,true);
        //The description of Moria should also change to reflect that the gate to the west has opened.
        String currentDescription=moria.getDescription();
        moria.setDescription(currentDescription.replace("The only rays of light in this place seem to come through the gate on the east.","The only rays of light in this place seem to come through the gates on the east and west."));

        //Now we still have to delete Gandalf from the CharactersInLocation of DoorsOfDurin
        GameCharacter gandalf=null;
        for(GameCharacter character:this.getCharactersInLocation()){ //Go through the characters in DoorsOfDurin and check which one is Gandalf. (a bit overkill since Gandalf is currently the only character in DoorsOfDurin, but would be necessary if there were also other characters)
            if(character.getName().equals("Gandalf")){
                gandalf=character;
            }
        }
        this.removeCharacter(gandalf);

        //Now we only still have to change the description of the location DoorsOfDurin to reflect that the doors are opened and Gandalf has left.
        this.setDescription("The Misty Mountains rise up around you large and impressive. The massive Doors of Durin stand opened, as if to welcome you into the Mines of Moria.");
        this.setDescriptionFromAfar("At the base of the towering Misty Mountains, you notice the Doors of Durin standing wide open.");

    }


}
