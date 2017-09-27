package seng302.team18.racemodel.interpret;

import seng302.team18.model.Boat;
import seng302.team18.util.VMGAngles;


/**
 * Rotates boats based as commanded by a boat action message
 */
public class BoatRotater {

    private Boat boat;
    private boolean fromLeft = false;

    /**
     * Constructor for BoatRotater.
     *
     * @param boat boats to rotate.
     */
    public BoatRotater(Boat boat) {
        this.boat = boat;
    }


    /**
     * Rotates a boat upwind.
     *
     * @param windDirection in degrees
     * @param windSpeed     in knots
     */
    public void rotateUpwind(double windDirection, double windSpeed) {
        double flippedWind = (windDirection + 180) % 360;
        double heading = (boat.getHeading() - windDirection + 360) % 360;

        if (Math.abs(boat.getHeading() - windDirection) < 3.5) {
            boat.setHeading(windDirection);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));

        } else if (Math.abs(boat.getHeading() - flippedWind) < 2.5) {
            if (fromLeft) {
                boat.setHeading((windDirection + 177) % 360);
            } else {
                boat.setHeading((windDirection + 183) % 360);
            }
        } else if (heading > 180 && heading < 360) {
            boat.setHeading((boat.getHeading() + 3) % 360);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
            fromLeft = true;
        } else {
            boat.setHeading((boat.getHeading() - 3 + 360) % 360);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
            fromLeft = false;
        }

    }


    /**
     * Rotates a boat downwind.
     *
     * @param windDirection in degrees
     * @param windSpeed     in knots
     */
    public void rotateDownwind(double windDirection, double windSpeed) {
        double flippedWind = (windDirection + 180) % 360;
        double heading = (boat.getHeading() - windDirection + 360) % 360;

        if (Math.abs(boat.getHeading() - windDirection) < 2.5) {
            if (fromLeft) {
                boat.setHeading((windDirection + 3) % 360);
            } else {
                boat.setHeading((windDirection + 357) % 360);
            }
        } else if (Math.abs(boat.getHeading() - flippedWind) < 3.5) {
            boat.setHeading(flippedWind);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
        } else if (heading > 0 && heading < 180) {
            boat.setHeading((boat.getHeading() + 3 + 360) % 360);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
            fromLeft = false;
        } else {
            boat.setHeading((boat.getHeading() - 3) % 360);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
            fromLeft = true;
        }
    }


    /**
     * Maximises the boats vmg to either upwind or downwind if it is not within the dead zone.
     * Note: If dead zone is 10 degrees the boat will not rotate if it is heading from 80 to 100 and 250 to 280.
     *
     * @param windDirection direction of the wind.
     * @param windSpeed     speed of the wind in knots.
     * @param deadZone      the heading at which the boat will not rotate.
     */
    public void setVMG(double windDirection, double windSpeed, double deadZone) {
        final double right = VMGAngles.RIGHT.getValue();
        final double left = VMGAngles.LEFT.getValue();
        double relativeHeading = (boat.getHeading() - windDirection + 360) % 360;
        if (left - deadZone > relativeHeading && relativeHeading > right + deadZone) {
            boat.optimalDownWind(windSpeed, windDirection);
        } else if (left + deadZone < relativeHeading || relativeHeading < right - deadZone) {
            boat.optimalUpWind(windSpeed, windDirection);
        }

    }


    /**
     * Change the boat heading to either tack or gybe depending on the direction the boat is moving upwind or downwind.
     *
     * @param windDirection the direction of the wind.
     * @param boat the boat to tack/gybe.
     */
    public void setTackGybe(double windDirection, Boat boat) {
        double newHeading = (2 * windDirection - boat.getHeading() + 360) % 360;
        boat.setHeading(newHeading);
    }


    public void setBoat(Boat boat) {
        if (boat != null) {
            this.boat = boat;
        }
    }
}
