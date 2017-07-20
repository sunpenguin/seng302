package seng302.team18.test_mock.interpret;

import seng302.team18.model.Boat;

import java.util.List;

/**
 * Created by dhl25 on 20/07/17.
 */
public class BoatRotater {

    private List<Boat> boats;
    private double angle;
    private boolean clockwise = false;
    private boolean upwind = false;

    public BoatRotater(List<Boat> boats, double angle) {
        this.boats = boats;
        this.angle = angle;
    }


    /**
     * Rotates a boat upwind.
     *
     * @param windDirection in degrees
     * @param windSpeed in knots
     */
    public void rotateUpwind(double windDirection, double windSpeed) {
        for (Boat boat : boats) {
            double oldHeading = boat.getHeading();
            double flippedWindDirection = (windDirection + 180) % 360; // flipping wind direction
            double newHeading = headTowardsWind(boat.getHeading(), flippedWindDirection, angle);
            boolean clockwise = isClockwiseRotation(oldHeading, newHeading);
            if (upwind && clockwise != this.clockwise) {
                newHeading = this.clockwise ? oldHeading + angle : oldHeading - angle;
                newHeading = (newHeading + 360) % 360;
            }
            this.clockwise = clockwise;
            upwind = true;
            boat.setHeading(newHeading);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
        }
    }


    /**
     * Rotates a boat downwind.
     *
     * @param windDirection in degrees
     * @param windSpeed in knots
     */
    public void rotateDownwind(double windDirection, double windSpeed) {
        for (Boat boat : boats) {
            double oldHeading = boat.getHeading();
            double newHeading = headTowardsWind(boat.getHeading(), windDirection, angle);
            boolean clockwise = isClockwiseRotation(oldHeading, newHeading);
            if (!upwind && clockwise != this.clockwise) {
                newHeading = this.clockwise ? oldHeading + angle : oldHeading - angle;
            }
            this.clockwise = clockwise;
            upwind = false;
            boat.setHeading(newHeading);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
        }
    }


    /**
     * Determines if a boat has turned clockwise.
     * Assumes rotations are less than 180 degrees.
     *
     * @param oldHeading of the boat.
     * @param newHeading of the boat.
     * @return if the boat has rotated clockwise.
     */
    private boolean isClockwiseRotation(double oldHeading, double newHeading) {
        double clockwiseChange = oldHeading - newHeading;
        clockwiseChange = (clockwiseChange + 360) % 360;
        return clockwiseChange > 180;
    }



    /**
     * Determines if a number n is between the two limits.
     * The order of limit1 and limit2 does not matter.
     *
     * @param n number we are testing
     * @param limit1 either the upper or lower limit
     * @param limit2 either the upper or lower limit
     * @return if n is between limit1 and limit2
     */
    private boolean isBetween(double n, double limit1, double limit2) {
        return limit1 < n && n < limit2 || limit2 < n && n < limit1;
    }



    /**
     * Finds the new angle a boat should travel at to move towards the wind if headingChange is positive
     * or away from the wind if headingChange is negative.
     *
     * @param boatHeading the boats current heading.
     * @param windHeading heading of the wind.
     * @param headingChange how much the boat should head towards the boat.
     * @return double, the new angle.
     */
    private double headTowardsWind(double boatHeading, double windHeading, double headingChange) {
        double boatToWindClockwiseAngle = boatHeading - windHeading;
//        double newHeading;

        if (boatToWindClockwiseAngle < 0) {
            boatToWindClockwiseAngle += 360;
        }

        if (boatToWindClockwiseAngle < 180) {
            return boatHeading + headingChange;
        }

        double newHeading = boatHeading - headingChange;

        if (newHeading < 0) {
            newHeading += 360;
        }

        return newHeading;
    }
}
