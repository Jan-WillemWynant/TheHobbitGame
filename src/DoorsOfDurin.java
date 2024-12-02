/*
 * The Doors of Durin are a special location. Initially, the doors are closed, which means the player can't travel to the mines of Moria to the east.
 * The player has to talk to Gandalf to open the doors.
 * Gandalf will only open the doors only if Bilbo has already picked up the Elvish Sword. Otherwise, he will urge him to go back west and first find the sword.
 * The dialogue Gandalf says if the player doesn't have the sword yet, is in the GameCharacter Gandalf.
 * Once Bilbo talks to Gandalf and already has the sword, Gandalf opens the doors and leaves.
 * So the description and descriptionFromAfar should naturally also be different after the doors have been opened.
 */

import java.util.Scanner;

public class DoorsOfDurin extends Location implements SpecificCommandHandler {

    private boolean doorsOpened; //this boolean will store whether the doors of Durin have already been opened or not.

    public DoorsOfDurin(String name) {
        super(name);
        doorsOpened=false; //the doors are initially closed.
    }

    //this enum will contain the specific commands this Item handles.
    private enum SpecificCommand {
        LOOK_EAST(new String[]{"look east"}), //the door is to the east. If the doors are not opened, DoorsOfDurin should handle this command.
        TALK_TO_GANDALF(new String[]{"talk to gandalf", "speak with gandalf"});

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

        switch (inputCommand) {
            case LOOK_EAST:
                if(doorsOpened){ //once the doors are opened, the general functionality of looking east is okay. So the command 'look east' should then just be handled in the Game class by the general looking methods.
                    return false;
                }
                else{
                    lookEast();
                    return true;
                }

            case TALK_TO_GANDALF:
                //if the player wants to talk to Gandalf, it depends. Once the doors have been opened, Gandalf will no longer be there, so DoorsOfDurin should not handle the 'talk to gandalf' command.
                //if the doors are not yet opened, we check if the player has already picked up the elvish sword.
                //if he hasn't picked up the sword yet, the dialogue in the Gandalf GameCharacter object is okay, so the general method talkTo(Character) in Game is okay -> in this case DoorsOfDurin does not handle the 'talk to gandalf' command.
                //if the player has already picked up the elvish sword, DoorsOfDurin handles the talking to gandalf, opens the door and Gandalf leaves.

                if (doorsOpened) return false;

                for (Item item : game.getItemsInInventory()) {
                    if (item.getName().equals("Elvish Sword")) {
                        talkToGandalf();
                        return true;
                    }
                }
                //if we get to this line, the player hasn't picked up the elvish sword yet, so the standard dialogue of Gandalf is fine.
                //So the general talkTo(Character) method in Game should handle this.
                return false;


        }
        return true;
    }

    public void lookEast(){
        System.out.println("To the east, the Doors of Durin stand between you and the Mines of Moria. Etched into the smooth rock are faint, intricate designs that shimmer in the right light, forming a hidden archway. \nSilent and imposing, the doors seem to wait, unmoving, for the right words to grant passage.");
    }

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
        //originally in the location Moria, the location to the west is just dark halls.
        //Now that the doors are opened, this should be overwritten and Moria should have DoorsOfDurin to the west.
        moria.addNeighboringLocation(Direction.WEST,this,true);
        //The description of Moria should also change to reflect that the gate to the west has opened.
        String currentDescription=moria.getDescription();
        moria.setDescription(currentDescription.replace("The only rays of light in this place seem to come through the gate on the east.","The only rays of light in this place seem to come through the gates on the east and west."));

        //Now we still have to delete Gandalf from the CharactersInLocation of DoorsOfDurin
        GameCharacter gandalf=null;
        for(GameCharacter character:this.getCharactersInLocation()){
            if(character.getName().equals("Gandalf")){
                gandalf=character;
            }
        }
        this.removeCharacter(gandalf);

        //Now we only still have to change the description of the location DoorsOfDurin.
        this.setDescription("The Misty Mountains rise up around you large and impressive. The massive Doors of Durin stand opened, as if to welcome you into the Mines of Moria.");
        this.setDescriptionFromAfar("At the base of the towering Misty Mountains, you notice the Doors of Durin standing wide open.");

    }


}
