package seng302.team18.model;

import seng302.team18.util.GPSCalculator;
import seng302.team18.util.SpeedConverter;

/**
 * A type of projectile that travels in a straight line when fired
 */
public class TigerShark extends Projectile {

    public TigerShark(int source_id, Coordinate location, double heading) {
        super(source_id, 5, 15, location, heading, 300); // Thing needs to be sent/changed
    }

    @Override
    public void update(double time) {
        GPSCalculator gps = new GPSCalculator();
        double mpsSpeed = new SpeedConverter().knotsToMs(getSpeed()); // convert to meters/second
        double secondsTime = time / 1000.0d;
        double distanceTravelled = mpsSpeed * secondsTime;
        setLocation(gps.toCoordinate(getLocation(), getHeading(), distanceTravelled));
    }
}
