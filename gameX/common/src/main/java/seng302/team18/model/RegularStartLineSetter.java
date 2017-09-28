package seng302.team18.model;

import seng302.team18.util.GPSCalculator;

import java.util.List;

/**
 * Move to next file
 */
public class RegularStartLineSetter implements StartPositionSetter {

    private double distance;
    private GPSCalculator calculator = new GPSCalculator();


    public RegularStartLineSetter(double distance) {
        this.distance = distance;
    }


    @Override
    public void setBoatPositions(List<Boat> boats, Course course) {
        int counter = 0;
        for (Boat boat : boats) {
            Coordinate position = getBoatPosition(boat, course, counter);
            boat.setCoordinate(position);
            counter++;
        }
    }


    @Override
    public Coordinate getBoatPosition(Boat boat, Course course, int counter) {
        Coordinate position = new Coordinate(5.2735, -4.4253);
        switch (boat.getId()) {
            case 121:
                position = new Coordinate(5.2735, -4.4253);
                break;
            case 122:
                position = new Coordinate(5.2737, -4.4255);
                break;
            case 123:
                position = new Coordinate(5.2739, -4.4257);
                break;
            case 124:
                position = new Coordinate(5.2733, -4.4255);
                break;
            case 125:
                position = new Coordinate(5.2735, -4.4257);
                break;
            case 126:
                position = new Coordinate(5.2737, -4.4259);
                break;
        }
        return position;
    }


    @Override
    public double getBoatHeading(Coordinate boatCoord, Course course) {
        return calculator.getBearing(boatCoord, course.getMarkRounding(0).getCoordinate());
    }
}

