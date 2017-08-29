package seng302.team18.model;

import seng302.team18.util.GPSCalculations;
import seng302.team18.util.SpeedConverter;

/**
 * Created by dhl25 on 29/08/17.
 */
public class SpeedPowerUp extends PowerUp {


    private Boat boat;


    public SpeedPowerUp(Boat boat) {
        this.boat = boat;
    }


    @Override
    public void update(double time) {
        super.update(time);
        time *= 2d;
        double speed = boat.getSpeed(); // knots
        if (boat.isSailOut()) {
            speed = 0;
        }

        GPSCalculations gps = new GPSCalculations();
        double mpsSpeed = new SpeedConverter().knotsToMs(speed); // convert to meters/second
        double secondsTime = time / 1000.0d;
        double distanceTravelled = mpsSpeed * secondsTime;

        // set next position based on current coordinate, distance travelled, and heading.
        boat.setCoordinate(gps.toCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled));
    }
}
