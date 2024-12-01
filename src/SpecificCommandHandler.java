public interface SpecificCommandHandler {
    //This interface enforces that the Location, Item or Character subclass implements a SpecificCommandHandler() method, which can be used to allow location-/item-/character-specific funtionality.

    //the specific commandHandler() method handles commands which represent location-specific, character-specific or item-specific functionality (so not the general commands applicable to every location/character/item)
    //it takes the inputted command as an argument, and the Game instance.
    //The Game instance is passed because the specific command might have to interact with the game state (for example 'drink potion' should increase the strengthLevel of the player).
    //the method also return whether the commandHandler does indeed handle the command (boolean).
    public abstract boolean specificCommandHandler(String[] command, Game game);
}
