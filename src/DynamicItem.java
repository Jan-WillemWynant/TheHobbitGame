public abstract class DynamicItem {
    //the specific item commandHandler handles commands which represent item-specific functionality (so not the general commands applicable to every item)
    //the method also return whether the commandHandler does indeed handle the command (boolean).
    public abstract boolean specificItemCommandHandler();
}
