public class Mirkwood extends Location implements SpecificCommandHandler {

    public Mirkwood(String name){
        super(name);
    }

    @Override
    public boolean specificCommandHandler(String[] command, Game game) {
        return false;
    }
}
