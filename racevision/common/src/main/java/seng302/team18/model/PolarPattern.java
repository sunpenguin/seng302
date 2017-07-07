package seng302.team18.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sbe67 on 5/07/17.
 */
public abstract class PolarPattern {

    private Map<Double, Polar> windSpeedToPolarMap;


    /**
     * Calls createMap()
     * Also adds a polar at 0 wind speed if not already added in createMap
     */
    public PolarPattern() {
        windSpeedToPolarMap = createMap();

        Polar polarAt0 = new Polar(0,45,0,155,0);
        polarAt0.addToMap(0,0);
        polarAt0.addToMap(15,0);
        polarAt0.addToMap(30,0);
        polarAt0.addToMap(60,0);
        polarAt0.addToMap(75,0);
        polarAt0.addToMap(90,0);
        polarAt0.addToMap(115,0);
        polarAt0.addToMap(140,0);
        polarAt0.addToMap(170,0);
        polarAt0.addToMap(165,0);
        polarAt0.addToMap(180,0);

        windSpeedToPolarMap.putIfAbsent(polarAt0.getWindSpeed(), polarAt0);
    }


    /**
     * Creates the map of polars with wind speeds.
     * Called in constructor.
     * Gets overridden by subclasses
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

    /**
     * Method to find the two polars with the closest windSpeeds to given wind speed.
     * Note: May return a list on length 1.
     * Cases in which a list of length 1 is returned:
     * 1. The windspeed given in greater than the windspeed of the polar with the highest wind speed
     * 2. The windspeed is equal to the windspeed of a polar
     *
     * @param windSpeed double, the speed of the wind
     * @return List<Polar>, a list of polar, either of length 1 or 2
     */
    public List<Polar> getTwoClosestPolars(double windSpeed) {
        //Set initials
        Polar closestPolar = windSpeedToPolarMap.values().iterator().next();
        double closestDistance = Math.abs(windSpeed - closestPolar.getWindSpeed());
        Polar secondClosestPolar = windSpeedToPolarMap.values().iterator().next();
        double secondClosestDistance = Math.abs(windSpeed - secondClosestPolar.getWindSpeed());
        List<Polar> closestPolars = new ArrayList<>();
        closestPolars.add(closestPolar);
        closestPolars.add(secondClosestPolar);

        return closestPolars;
    }
}
