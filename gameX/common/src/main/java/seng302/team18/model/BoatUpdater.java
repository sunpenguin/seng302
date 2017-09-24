package seng302.team18.model;

import seng302.team18.message.PowerType;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.SpeedConverter;

/**
 * Class to update boat PowerUps.
 */
public class BoatUpdater extends PowerUp {

    public BoatUpdater() {
        super();
    }


    @Override
    public void update(Boat boat, double time) {
        super.update(boat, time);
        double speed = boat.getSpeed(); // knots
        if (boat.isSailOut()) {
            speed = 0;
        }
        GPSCalculator gps = new GPSCalculator();
        double mpsSpeed = new SpeedConverter().knotsToMs(speed); // convert to meters/second
        double secondsTime = time / 1000.0d;
        double distanceTravelled = mpsSpeed * secondsTime;
        Coordinate nextPosition = gps.toCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled);
        boat.setCoordinate(nextPosition);
    }


    @Override
    public boolean isTerminated() {
        return false;
    }


    @Override
    public PowerType getType() {
        return null;
    }

}
