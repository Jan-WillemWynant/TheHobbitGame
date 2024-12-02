/*
 * Special location which will allow the functionality of 'climb tree'.
 * Class still needs to be implemented.
 */

public class Mirkwood extends Location implements SpecificCommandHandler {

    public Mirkwood(String name) {
        super(name);
    }

    @Override
    public boolean specificCommandHandler(String[] command, Game game) {
        return false;
    }
}
