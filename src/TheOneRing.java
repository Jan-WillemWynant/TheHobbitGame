public class TheOneRing extends Item implements SpecificCommandHandler {

    public TheOneRing() {
        super("The One Ring", true);
    }

    @Override
    public boolean specificCommandHandler(String[] command, Game game) {
        //need to implement this
        return false;
    }

}

