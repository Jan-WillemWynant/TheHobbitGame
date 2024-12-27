/**
 * The interface enforces that a Location, Item or Character subclass implements a SpecificCommandHandler() method,
 * which can be used to allow location-/item-/character-specific functionality.
 */

public interface SpecificCommandHandler {

    /**
     * The specificCommandHandler() method handles commands which represent location-specific, character-specific
     * or item-specific functionality (so not the general commands applicable to every location/character/item).
     * It takes the inputted command as an argument, and the Game instance which calls the specificCommandHandler.
     * The Game instance is passed because the specific command might have to interact with the game state
     * (for example 'drink potion' should increase the strengthLevel of the player).
     * It returns whether the specificCommandHandler() indeed handles the inputted command or not (true or false).
     *
     * @param command The inputted command
     * @param game The game instance which calls the specificCommandHandler() method
     * @return true if the specificCommandHandler() handles the inputted command, false otherwise
     */
    boolean specificCommandHandler(String[] command, Game game);
}
