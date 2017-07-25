package seng302.team18.test_mock.interpret;

import seng302.team18.model.Boat;

import java.util.List;


/**
 * Rotates boats based as commanded by a boat action message
 */
public class BoatRotater {

    private List<Boat> boats;
    private double angle;
    private Boolean clockwise;
    private Boolean upwind;

    /**
     * Constructor for BoatRotater.
     *
     * @param boats boats to rotate.
     * @param angle to rotate them
     */
    public BoatRotater(List<Boat> boats, double angle) {
        this.boats = boats;
        this.angle = angle;
    }


    /**
     * Rotates a boat upwind.
     *
     * @param windDirection in degrees
     * @param windSpeed     in knots
     */
    public void rotateUpwind(double windDirection, double windSpeed) {
        if (upwind == null) {
            upwind = false;
        }
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
            boat.setHeading((newHeading + 360) % 360);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
        }
    }


    /**
     * Rotates a boat downwind.
     *
     * @param windDirection in degrees
     * @param windSpeed     in knots
     */
    public void rotateDownwind(double windDirection, double windSpeed) {
        if (upwind == null) {
            upwind = true;
        }
        for (Boat boat : boats) {
            double oldHeading = boat.getHeading();
            double newHeading = headTowardsWind(boat.getHeading(), windDirection, angle);
            boolean clockwise = isClockwiseRotation(oldHeading, newHeading);
            if (!upwind && clockwise != this.clockwise) {
                newHeading = this.clockwise ? oldHeading + angle : oldHeading - angle;
                newHeading = (newHeading + 360) % 360;
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
     * Finds the new angle a boat should travel at to move towards the wind if headingChange is positive
     * or away from the wind if headingChange is negative.
     *
     * @param boatHeading   the boats current heading.
     * @param windHeading   heading of the wind.
     * @param headingChange how much the boat should head towards the boat.
     * @return double, the new angle.
     */
    private double headTowardsWind(double boatHeading, double windHeading, double headingChange) {
        double boatToWindClockwiseAngle = boatHeading - windHeading;

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


    /**
     * Maximises the boats vmg to either upwind or downwind if it is not within the dead zone.
     * Note: If dead zone is 10 degrees the boat will not rotate if it is heading from 80 to 100 and 250 to 280.
     *
     * @param windDirection direction of the wind.
     * @param windSpeed     speed of the wind in knots.
     * @param deadZone      the heading at which the boat will not rotate.
     */
    public void setVMG(double windDirection, double windSpeed, double deadZone) {
        System.out.println("BoatRotater:setVMG");
        final double left = (90 + windDirection) % 360;
        final double right = (270 + windDirection) % 360;
        System.out.println("right = " + right);
        System.out.println("left = " + left);
        for (Boat boat : boats) {
            double flippedWindDirection = (windDirection + 180) % 360;
            double headingDifference = (flippedWindDirection - boat.getHeading() + 360) % 360;
            System.out.println("heading = " + boat.getHeading());
            System.out.println("windDirection = " + flippedWindDirection);
            System.out.println("headingDifference = " + headingDifference);
            boolean isUpWind = headingDifference < 90 - deadZone;
            boolean isDownWind = headingDifference > 90 + deadZone;
            if (isUpWind) {
                System.out.println("up wind");
                boat.optimalDownWind(windSpeed, windDirection);
            } else if (isDownWind) {
                System.out.println("down wind");
                boat.optimalUpWind(windSpeed, windDirection);
            }
        }
    }
}
