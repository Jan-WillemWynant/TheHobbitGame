public abstract class DynamicLocation {
    //the specific location commandHandler handles commands which represent location-specific functionality (so not the general commands applicable to every location)
    //the method also return whether the commandHandler does indeed handle the command (boolean).
    public abstract boolean specificLocationCommandHandler();
}

