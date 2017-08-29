package seng302.team18.model;

import seng302.team18.util.GPSCalculator;
import seng302.team18.util.SpeedConverter;

/**
 * Created by dhl25 on 29/08/17.
 */
public class BoatUpdate extends UpdateStrategy {

    private Boat boat;

    public BoatUpdate(Boat boat) {
        super();
        this.boat = boat;
    }

    @Override
    public void update(double time) {
        super.update(time);
        double speed = boat.getSpeed(); // knots
        if (boat.isSailOut()) {
            speed = 0;
        }

        GPSCalculator gps = new GPSCalculator();
        double mpsSpeed = new SpeedConverter().knotsToMs(speed); // convert to meters/second
        double secondsTime = time / 1000.0d;
        double distanceTravelled = mpsSpeed * secondsTime;

        // set next position based on current coordinate, distance travelled, and heading.
        boat.setCoordinate(gps.toCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

}
