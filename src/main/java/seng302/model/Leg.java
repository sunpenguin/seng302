package seng302.model;

import seng302.raceutil.GPSCalculations;

/**
 * A class which denotes which two marks a boat is currently travelling between using a destination mark, a departure
 * mark, a heading and the number of the leg in the race
 */
public class Leg {
    private CompoundMark destination;
    private CompoundMark departure;
    private double heading;
    private int legNumber;

    /**
     * A constructor for the Leg
     * @param departure The CompoundMark at the beginning of the leg
     * @param destination The CompoundMark at the end of the leg
     * @param legNumber The number of the leg in the race
     */
    public Leg(CompoundMark departure, CompoundMark destination, int legNumber) {
        this.destination = destination;
        this.departure = departure;
        this.heading = GPSCalculations.findAngle(departure.getMidCoordinate(), destination.getMidCoordinate());
        this.legNumber = legNumber; // TODO this is a kinda shitty way of doing it might change later
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leg leg = (Leg) o;

        if (Double.compare(leg.heading, heading) != 0) return false;
        if (legNumber != leg.legNumber) return false;
        if (!destination.equals(leg.destination)) return false;
        return departure.equals(leg.departure);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = destination.hashCode();
        result = 31 * result + departure.hashCode();
        temp = Double.doubleToLongBits(heading);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + legNumber;
        return result;
    }
}
