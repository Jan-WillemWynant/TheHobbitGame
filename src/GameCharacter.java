public class GameCharacter {
    private String name;
    private String description;
    private String dialogue;
    private boolean alreadyTalkedTo=false; //boolean which stores if player has already talked to character. (Possible functionality: different dialogue if player has already talked to the character. For example add "Hey Bilbo again. You wish to hear my advice again...")
    private boolean canTalk;

    public GameCharacter(String name, boolean canTalk){
        this.name=name;
        this.canTalk=canTalk;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public void setDialogue(String dialogue){
        this.dialogue=dialogue;
    }

    public void setAlreadyTalkedTo(boolean alreadyTalkedTo){
        this.alreadyTalkedTo=alreadyTalkedTo;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getDialogue(){
        return dialogue;
    }

    public boolean canTalk(){
        return canTalk;
    }

}
