package seng302.team18.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbe67 on 5/07/17.
 */
public abstract class PolarPattern {

    private Map<Double, Polar> windSpeedToPolarMap;


    public PolarPattern() {
        windSpeedToPolarMap = createMap();
    }


    /**
     * Adds a new polar to the maps of polars.
     * Note: Polars are mapped by their windSpeed
     *
     * @param polarToAdd Polar, polar to be added to the map of polars
     */
    abstract protected Map<Double, Polar> createMap();


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
        Polar closestPolar = windSpeedToPolarMap.values().iterator().next();
        double closestDistance = Math.abs(windSpeed - closestPolar.getWindSpeed());

        for (Polar currentPolar : windSpeedToPolarMap.values()) {

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
