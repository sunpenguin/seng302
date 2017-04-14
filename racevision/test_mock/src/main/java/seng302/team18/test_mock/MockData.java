package seng302.team18.test_mock;

import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;

import java.util.List;

/**
 * Class to hold the mock data
 */
public class MockData {

    protected Coordinate centreCoordinate;
    protected List<Coordinate> boundaries;
    protected double raceID;
    protected String raceType;
    protected String raceStartTime;

    MockData() {
    }

    @Override
    public String toString() {
        String info = new String("Centre: " + centreCoordinate.toString() +
                "\nBoundaries: " + boundaries.toString() +
                "\nraceID: " + raceID +
                "\nRaceType: " + raceType +
                "\nRace Start Time: " + raceStartTime);
        return info;
    }
}
