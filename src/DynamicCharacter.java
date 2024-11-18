public abstract class DynamicCharacter {
    //the specific character commandHandler handles commands which represent character-specific functionality (so not the general commands applicable to every character)
    //the method also return whether the commandHandler does indeed handle the command (boolean).
    public abstract boolean specificCharacterCommandHandler();
}
