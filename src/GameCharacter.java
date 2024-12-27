/**
 * The GameCharacter class represents the blueprint for characters in the game.
 *
 * A character has a name and a dialogue.
 * A flag which denotes whether the character can talk or not is also maintained.
 * The class interacts with the Game class (where the gameplay happens) through getters and setters.
 *
 */

public class GameCharacter {
    private String name; //the name of the character
    private String dialogue; //the dialogue which is shown to the player if the player talks to this character
    private boolean canTalk; //boolean denoting whether the character can talk or not

    /**
     * Constructor method
     *
     * @param name Name of the character
     * @param canTalk Whether the character can talk or not
     */
    public GameCharacter(String name, boolean canTalk) {
        this.name = name;
        this.canTalk = canTalk;
    }

    /**
     * Sets the dialogue of the character to the specified string.
     *
     * @param dialogue The dialogue text to be displayed when the character speaks
     */
    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    /**
     * Returns the name of the character.
     *
     * @return name of the character
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the dialogue of the character.
     *
     * @return
     */
    public String getDialogue() {
        return dialogue;
    }

    /**
     * Returns whether the character can talk or not (true or false).
     *
     * @return whether the character can talk or not
     */
    public boolean canTalk() {
        return canTalk;
    }

}
