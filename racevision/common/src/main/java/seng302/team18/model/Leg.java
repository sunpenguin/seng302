package seng302.team18.model;


/**
 * Created by dhl25 on 18/03/17.
 */
public class Leg {
    private CompoundMark destination;
    private CompoundMark departure;
//    private double heading;
//    private double distance;
    private int legNumber;

    public Leg(CompoundMark departure, CompoundMark destination, int legNumber) {
        this.destination = destination;
        this.departure = departure;
//        this.heading = GPSCalculations.findAngle(departure.getMidCoordinate(), destination.getMidCoordinate());
        this.legNumber = legNumber; // TODO this is a kinda shitty way of doing it might change later
    }

    public CompoundMark getDestination() {
        return destination;
    }

    public CompoundMark getDeparture() {
        return departure;
    }

//    public double getHeading() {
//        return heading;
//    }

    public int getLegNumber() {
        return legNumber;
    }

//    public double distance() {
//
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leg leg = (Leg) o;

//        if (Double.compare(leg.heading, heading) != 0) return false;
        if (legNumber != leg.legNumber) return false;
        if (!destination.equals(leg.destination)) return false;
        return departure.equals(leg.departure);
    }

    @Override
    public int hashCode() {
        int result;
//        long temp;
        result = destination.hashCode();
        result = 31 * result + departure.hashCode();
//        temp = Double.doubleToLongBits(heading);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + legNumber;
        return result;
    }
}
