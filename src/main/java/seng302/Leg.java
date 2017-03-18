package seng302;

/**
 * Created by dhl25 on 18/03/17.
 */
public class Leg {
    private CompoundMark destination;
    private CompoundMark departure;
    private double heading;
    private double distance;

    public Leg(CompoundMark departure, CompoundMark destination) {
        this.destination = destination;
        this.departure = departure;
        this.heading = GPSCalculations.findAngle(departure.getMidCoordinate(), destination.getMidCoordinate());
        this.distance = GPSCalculations.GPSDistance(departure.getMidCoordinate(), destination.getMidCoordinate());
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

    public double getDistance() {
        return distance;
    }
}
