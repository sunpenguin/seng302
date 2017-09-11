package seng302.team18.model;

import java.util.List;

/**
 * Created by dhl25 on 11/09/17.
 */
public interface StartPositionSetter {

    void setBoatPositions(List<Boat> boats, Course course);


    Coordinate getBoatPosition(Boat boats, Course course, int numBoats);
}
