package seng302;

/**
 * Created by dhl25 on 18/03/17.
 */
public class Leg {
    private CompoundMark destination;
    private CompoundMark departure;
    private double heading;
    private int legNumber;

    public Leg(CompoundMark departure, CompoundMark destination, int legNumber) {
        this.destination = destination;
        this.departure = departure;
        this.heading = GPSCalculations.findAngle(departure.getMidCoordinate(), destination.getMidCoordinate());
        this.legNumber = legNumber;
    }

    public CompoundMark getDestination() {
        return destination;
    }

    public CompoundMark getDeparture() {
        return departure;
    }

    public double getHeading() {
        return heading;
    }

    public int getLegNumber() {
        return legNumber;
    }
}
