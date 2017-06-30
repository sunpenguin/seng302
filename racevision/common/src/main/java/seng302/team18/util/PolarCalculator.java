package seng302.team18.util;

import seng302.team18.model.Polar;

import java.util.List;

/**
 * Class to apply polar calculations to boats
 */
public class PolarCalculator {

    private List<Polar> polars;

    public PolarCalculator(List<Polar> polars) {
        this.polars = polars;
    }

    /**
     * Gets true wind angle for boat.
     * @param windHeading Heading of the wind
     * @param heading Heading of the boat
     * @return True wind angle for the boat.
     */
    public double getTrueWindAngle(double windHeading, double heading) {
        double theta = 180 - windHeading;
        double boatPlusTheta = heading + theta;
        double windPlusTheta = windHeading + theta; //will be 180
        if (boatPlusTheta>360){
            boatPlusTheta = boatPlusTheta-360;
        }
        if (boatPlusTheta < 0){
            boatPlusTheta = 360 + boatPlusTheta;
        }
        double trueWindAngle;
        if (boatPlusTheta > 180){
            double angleFrom180 = boatPlusTheta - 180;
            trueWindAngle = 180 - angleFrom180;
        }else{
            trueWindAngle = boatPlusTheta;
        }
        return trueWindAngle;
    }
}
