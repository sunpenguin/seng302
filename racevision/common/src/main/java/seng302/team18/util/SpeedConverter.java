package seng302.team18.util;

/**
 * Created by dhl25 on 30/06/17.
 */
public class SpeedConverter {

    public SpeedConverter() {}


    /**
     * Converts from millimeters per second to knots.
     * @param speed in millimeters per second
     * @return speed in knots
     */
    public double mmsToKnots(double speed) {
        final double MMS_TO_KNOTS_MULTIPLIER = 0.0019438445;
        return speed * MMS_TO_KNOTS_MULTIPLIER;
    }


    /**
     * Converts from knots to millimeters per second.
     * @param speed in knots
     * @return speed in millimeters per second
     */
    public double knotsToMms(double speed) {
        final double KNOTS_TO_MMS_MULTIPLIER = 0.514444 * 1000;
        return speed * KNOTS_TO_MMS_MULTIPLIER;
    }


    /**
     * Converts from knots to meters per second.
     * @param speed in knots
     * @return speed in meters per second
     */
    public double knotsToMs(double speed) {
        final double KNOTS_TO_MS_MULTIPLIER = 0.514444;
        return speed * KNOTS_TO_MS_MULTIPLIER;
    }
}
