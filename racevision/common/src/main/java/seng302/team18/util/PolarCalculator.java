package seng302.team18.util;

import seng302.team18.model.Polar;

import java.util.*;

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
     * @param windSpeed Speed of the wind (must match the windspeed of a polar)
     * @param heading The heading of the boats destination
     * @param windHeading The heading of the wind
     * @return A list holding the speed at which the boat with travel and angle from the original heading it should travel
     */
    public XYPair getBest(double windSpeed, double heading, double windHeading) {
        //Find polar with correct wind speed
        Polar polarToUse = polars.get(0);
        for(Polar polar : polars){
            if(polar.getWindSpeed() == windSpeed){
                polarToUse = polar;
            }
        }

        //get boats true wind angle
        double trueWindAngle = getTrueWindAngle(windHeading, heading);

        double angle;
        double speed = 0;

        //get angle from heading
        if (trueWindAngle <= polarToUse.getUpWindAngle()) {
            angle = Math.abs(polarToUse.getUpWindAngle() - trueWindAngle);
            speed = polarToUse.getUpWindSpeed();
        } else if (trueWindAngle >= polarToUse.getDownWindAngle()) {
            angle = Math.abs(polarToUse.getDownWindAngle() - trueWindAngle);
            speed = polarToUse.getDownWindSpeed();
        } else {
            angle = 0;

            //initialize minDifference
            double minDifference = Math.abs(polarToUse.getDownWindAngle() - trueWindAngle);
            speed = polarToUse.getDownWindSpeed();

            //Iterate through hashmap
            Iterator it = polarToUse.getMapSpeedAtAngles().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                //get difference between current and true wind angle
                double currentDifference = Math.abs((double)pair.getKey() - trueWindAngle);

                if(currentDifference < minDifference) {
                    //set min diff
                    minDifference = currentDifference;
                    speed = (double)pair.getValue();
                }
                it.remove();
            }
        }
        return new XYPair(speed, angle);
    }

    private static double getTrueWindAngle(double windHeading, double heading){
        double offset = heading - windHeading;

        if (offset < 0){
            offset = 360 + offset;
        }

        if(offset > 180){
            offset = offset - (offset-180)*2;
        }
        return offset;
    }
}
