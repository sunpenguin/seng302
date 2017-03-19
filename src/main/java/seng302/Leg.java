package seng302;

/**
 * Created by dhl25 on 18/03/17.
 */
public class Leg {
    private CompoundMark destination;
    private CompoundMark departure;
    private double heading;
    private double distance;
    private int legNumber;

    public Leg(CompoundMark departure, CompoundMark destination, int legNumber) {
        this.destination = destination;
        this.departure = departure;
        this.heading = GPSCalculations.findAngle(departure.getMidCoordinate(), destination.getMidCoordinate());
        this.legNumber = legNumber;

        XYPair departureXY = GPSCalculations.GPSxy(departure.getMidCoordinate());
        XYPair destinationXY = GPSCalculations.GPSxy(destination.getMidCoordinate());

        double diffXSquared = (departureXY.getX() - destinationXY.getX()) * (departureXY.getX() - destinationXY.getX());
        double diffYSquared = (departureXY.getY() - destinationXY.getY()) * (departureXY.getY() - destinationXY.getY());
        this.distance = Math.sqrt(diffXSquared + diffYSquared);

//        this.distance = GPSCalculations.GPSDistance(departure.getMidCoordinate(), destination.getMidCoordinate());
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

    public int getLegNumber() {
        return legNumber;
    }
}
