package seng302.team18.model;

import javafx.scene.shape.Polyline;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.SpeedConverter;

/**
 * Created by csl62 on 7/09/17.
 */
public class TigerShark extends Projectile{



    public TigerShark(int source_id, double radius, double weight, Coordinate location, double heading, double speed) {
        super(source_id, radius, weight, location, heading, speed);
    }

    @Override
    public void update(double time) {
        GPSCalculator gps = new GPSCalculator();
        double mpsSpeed = new SpeedConverter().knotsToMs(getSpeed()); // convert to meters/second
        double secondsTime = time / 1000.0d;
        double distanceTravelled = mpsSpeed * secondsTime;
        setLocation(gps.toCoordinate(getLocation(),getHeading(),distanceTravelled));
    }
}
