package seng302.team18.util;

import seng302.team18.model.Polar;

import java.util.List;
import java.util.Map;

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
     *
     * @param windHeading Heading of the wind
     * @param heading     Heading of the boat
     * @return True wind angle for the boat.
     */
    public double getTrueWindAngle(double windHeading, double heading) {
        double theta = 180 - windHeading;
        double boatPlusTheta = heading + theta;
        double windPlusTheta = windHeading + theta; //will be 180

        if (boatPlusTheta > 360) {
            boatPlusTheta = boatPlusTheta - 360;
        }

        if (boatPlusTheta < 0) {
            boatPlusTheta = 360 + boatPlusTheta;
        }

        double trueWindAngle;

        if (boatPlusTheta > 180) {
            double angleFrom180 = boatPlusTheta - 180;
            trueWindAngle = 180 - angleFrom180;
        } else {
            trueWindAngle = boatPlusTheta;
        }

        return trueWindAngle;
    }


    /**
     * From the polars list, a singular polar is selected.
     * The polar with the closest windSpeed is selected.
     * Note: If distance from two polars is the same, the lower speed polar is selected.
     *
     * @param windSpeed double, the speed of the wind.
     * @return polar with closest windSpeed to given windSpeed
     */
    public Polar getPolarForWindSpeed(double windSpeed) {
        //Set initial
        Polar closestPolar = polars.get(0);
        double closestDistance = Math.abs(windSpeed - closestPolar.getWindSpeed());

        for (Polar currentPolar : polars) {

            double currentDistance = Math.abs(windSpeed - currentPolar.getWindSpeed());

            if (closestDistance > currentDistance) {
                closestPolar = currentPolar;
                closestDistance = currentDistance;
            } else if (closestDistance == currentDistance && currentPolar.getWindSpeed() < closestPolar.getWindSpeed()) {
                closestPolar = currentPolar;
                closestDistance = currentDistance;
            }
        }

        return closestPolar;
    }


    /**
     * Calculates the speed at which the boat would travel.
     *
     * @param boatTWA   double, true wind angle of the boat
     * @param windSpeed     double, speed of the wind
     * @return  double, the speed of the boat
     */
    public double getSpeedForBoat(double boatTWA, double windSpeed) {
        //Get polar to be used
        Polar polar = getPolarForWindSpeed(windSpeed);
        //Set initial
        double bestAngle = polar.getUpWindAngle();
        double bestDifference = Math.abs(boatTWA - bestAngle);

        for(Map.Entry<Double,Double> currentSet: polar.getMapSpeedAtAngles().entrySet()) {
            double currentDifference = Math.abs(boatTWA - currentSet.getKey());

            if (currentDifference < bestDifference) {
                bestAngle = currentSet.getKey();
                bestDifference = currentDifference;
            } else if (currentDifference == bestDifference && currentSet.getKey() < bestAngle) {
                bestAngle = currentSet.getKey();
                bestDifference = currentDifference;
            }
        }

        return polar.getMapSpeedAtAngles().get(bestAngle);
    }

}
