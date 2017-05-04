package seng302.team18.util;

import seng302.team18.model.Polar;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sbe67 on 2/05/17.
 */
public class PolarCalculator {

    private List<Polar> polars;

    public PolarCalculator(List<Polar> polars) {
        this.polars = polars;
    }

    /**
     * A method to get the best angles for a boat to move in using its polar pattern
     *
     * @param windSpeed   Speed of the wind (must match the windspeed of a polar)
     * @param heading     The heading of the boats destination (not relative to wind)
     * @param windHeading The heading of the wind
     * @return An XYPair holding the speed at which the boat with travel and the angle the boat should travel relative to the wind.
     */
    public XYPair getBest(double windSpeed, double heading, double windHeading) {
        //Find polar with correct wind speed
        Polar polarToUse = polars.get(0);
        for (Polar polar : polars) {
            if (polar.getWindSpeed() == windSpeed) {
                polarToUse = polar;
            }
        }

        //get boats true wind angle
        double trueWindAngle = getTrueWindAngle(windHeading, heading);

        double angle;
        double speed = 0;

        //get angle from heading
        if (trueWindAngle <= polarToUse.getUpWindAngle()) {
            angle = Math.abs(polarToUse.getUpWindAngle());
            speed = polarToUse.getUpWindSpeed();
        } else if (trueWindAngle >= polarToUse.getDownWindAngle()) {
            angle = Math.abs(polarToUse.getDownWindAngle());
            speed = polarToUse.getDownWindSpeed();
        } else {
            //If between thresholds angle stays the same.
            angle = trueWindAngle;

            //initialize minDifference
            double minDifference = Math.abs(polarToUse.getDownWindAngle() - trueWindAngle);
            speed = polarToUse.getDownWindSpeed();

            //Iterate through hashmap
            Iterator it = polarToUse.getMapSpeedAtAngles().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                //get difference between current and true wind angle
                double currentDifference = Math.abs((double) pair.getKey() - trueWindAngle);

                if (currentDifference <= minDifference) {
                    if (currentDifference < minDifference) {
                        //set min diff
                        minDifference = currentDifference;
                        speed = (double) pair.getValue();
                    }else{ //if equal
                        //min diff is the same
                        double equalAngleSpeed = (double)pair.getValue();
                        //get faster speed
                        if (equalAngleSpeed> speed){
                            speed = equalAngleSpeed;
                        }
                    }
                }
                it.remove();
            }
        }
        return new XYPair(speed, angle);
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
