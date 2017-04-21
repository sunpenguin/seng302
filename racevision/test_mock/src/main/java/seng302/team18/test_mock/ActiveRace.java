package seng302.team18.test_mock;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculations;

import java.util.List;

/**
 * Subclass of race that can simulate a race.
 */
public class ActiveRace extends Race {
    GPSCalculations gpsCalculations;

    /**
     * Race class constructor.
     *
     * @param startingList Arraylist holding all entered boats
     * @param course       Course object
     */
    public ActiveRace(List<Boat> startingList, Course course) {
        super(startingList, course);
        gpsCalculations = new GPSCalculations(course);
    }


    /**
     * Updates the position and heading of every boat in the race.
     *
     * @param time
     */
    public void updateBoats(double time) { // time in seconds
        for (Boat boat : startingList) {
            if (!finishedList.contains(boat)) {
                updateBoat(boat, time);
            }
        }
    }


    /**
     * Updates a boats position then heading.
     *
     * @param boat
     * @param time
     */
    private void updateBoat(Boat boat, double time) {
        updatePosition(boat, time);
        updateHeading(boat);
    }


    /**
     * Changes the boats heading so that if it has reached its destination
     * it heads in the direction of its next destination. Otherwise set the heading
     * to be in the direction of its current destination.
     *
     * @param boat to be updated
     */
    private void updateHeading(Boat boat) {
        // if boat gets within range of its next destination changes its destination and heading
        if ((Math.abs(boat.getDestination().getLongitude() - boat.getCoordinate().getLongitude()) < 0.0001)
                && (Math.abs(boat.getDestination().getLatitude() - boat.getCoordinate().getLatitude()) < 0.0001)) {
            Leg nextLeg = course.getNextLeg(boat.getLeg()); // find next leg
            // if current leg is the last leg boat is now finished
            if (nextLeg.equals(boat.getLeg())) {
                finishedList.add(boat);
                boat.setSpeed(0d);
                return;
            }
            if (boat.getLeg().getDestination().getMarks().size() == CompoundMark.GATE_SIZE &&  // if the destination is a gate
                    !boat.getDestination().equals(nextLeg.getDeparture().getMarks().get(0).getCoordinate())) { // and it hasn't gone around the gate
                boat.setDestination(nextLeg.getDeparture().getMarks().get(0).getCoordinate()); // move around the gate
            } else { // the destination was a mark or is already gone around gate so move onto the next leg
                setNextLeg(boat, nextLeg);
            }
        }
        boat.setHeading(gpsCalculations.retrieveHeading(boat.getCoordinate(), boat.getDestination()));
    }


    /**
     * Sets the next Leg of the boat, updates the mark to show the boat has passed it,
     * and sets the destination to the next marks coordinates.
     *
     * @param boat
     * @param nextLeg
     */
    private void setNextLeg(Boat boat, Leg nextLeg) {
        CompoundMark passedMark = boat.getLeg().getDestination();
        passedMark.addPassed(boat);
        boat.setPlace(passedMark.getPassed().indexOf(boat) + 1);
        boat.setDestination(nextLeg.getDestination().getMidCoordinate());
        boat.setLeg(nextLeg);
        //startingList.set(startingList.indexOf(boat), boat); // forces list to notify the tableview
    }


    /**
     * Updates the boats coordinates to move closer to the boats destination.
     * Amount moved is proportional to the time passed
     *
     * @param boat to be moved
     * @param time that has passed
     */
    private void updatePosition(Boat boat, double time) {
//        final double KMPH_TO_MPS = 1000.0 / 3600.0;
//        double speed = boat.getSpeed() * KMPH_TO_MPS;
//        double distanceTravelled = speed * time
//                / (duration / (course.getCourseDistance() / (startingList.get(0).getSpeed() * KMPH_TO_MPS))); // meters
//        boat.setCoordinate( // set next position based on current coordinate, distance travelled, and heading.
//                gpsCalculations.coordinateToCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
    }


    public boolean isFinished() {
        return startingList.size() == finishedList.size();
    }
}
