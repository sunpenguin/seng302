package seng302.team18.model;

/**
 * A class that represents a mark on the race course
 */
public class Mark extends AbstractBoat implements GeographicLocation {


    public Mark(int id, String name, String shortName) {
        super(id, name, shortName);
    }

    public Mark(int id, Coordinate coordinate) {
        super();
        setId(id);
        setCoordinate(coordinate);
        setWeight(Double.MAX_VALUE);
        setLength(18); // just a random number lol
    }


    @Override
    public BoatType getType() {
        return BoatType.MARK;
    }
}

