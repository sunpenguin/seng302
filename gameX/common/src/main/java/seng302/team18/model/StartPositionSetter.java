package seng302.team18.model;

import java.util.List;

/**
 * Defines an interface for objects the position a boat according to the course and number of participants
 */
public interface StartPositionSetter {


    void setBoatPositions(List<Boat> boats, Course course);


    /**
     * Calculates the starting position for the given boat
     *
     * @param boat     the boat to be placed
     * @param course   the course the boat is to be placed in
     * @param numBoats the total number of boats being placed
     * @return the boat's starting position
     */
    Coordinate getBoatPosition(Boat boat, Course course, int numBoats);
}
