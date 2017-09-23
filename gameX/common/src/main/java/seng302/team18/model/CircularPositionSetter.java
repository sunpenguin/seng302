package seng302.team18.model;

import seng302.team18.util.GPSCalculator;

import java.util.List;

/**
 * Start position setter that places boats in a circle around the centre of the course
 */
public class CircularPositionSetter implements StartPositionSetter {

    private double distance;
    private GPSCalculator calculator = new GPSCalculator();


    public CircularPositionSetter(double distance) {
        this.distance = distance;
    }


    @Override
    public void setBoatPositions(List<Boat> boats, Course course) {
        for (Boat boat : boats) {
            Coordinate position = getBoatPosition(boat, course, boats.size());
            boat.setCoordinate(position);
        }
    }


    @Override
    public Coordinate getBoatPosition(Boat boat, Course course, int numBoats) {
        Coordinate center = course.getCenter();
        return calculator.toCoordinate(center, ((360 / numBoats * boat.getId() + 90) % 360), distance);
    }


    @Override
    public double getBoatHeading(Coordinate boatCoord, Course course) {
        return calculator.getBearing(boatCoord, course.getCenter());
    }
}
