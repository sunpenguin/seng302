package seng302.team18.model;

import seng302.team18.util.XYPair;

import java.util.ArrayList;
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

        Polar polarAt0 = new Polar(0, 45, 0, 155, 0);
        polarAt0.addToMap(0, 0);
        polarAt0.addToMap(15, 0);
        polarAt0.addToMap(30, 0);
        polarAt0.addToMap(60, 0);
        polarAt0.addToMap(75, 0);
        polarAt0.addToMap(90, 0);
        polarAt0.addToMap(115, 0);
        polarAt0.addToMap(140, 0);
        polarAt0.addToMap(170, 0);
        polarAt0.addToMap(165, 0);
        polarAt0.addToMap(180, 0);

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
     * Method to find the two polars with the closest windSpeeds to given wind speed.
     * One polar will be less than the windspeed, one will be greater.
     * Note: May return a list on length 1.
     * Cases in which a list of length 1 is returned:
     * 1. The windspeed given in greater than the windspeed of the polar with the highest wind speed
     * 2. The windspeed is equal to the windspeed of a polar
     *
     * @param windSpeed double, the speed of the wind
     * @return List<Polar>, a list of polar, either of length 1 or 2
     */
    public List<Polar> getClosestPolars(double windSpeed) {
        List<Polar> closestPolars = new ArrayList<>();

        if (windSpeedIsAboveMaxPolar(windSpeed)) {
            Polar polarAtHighestWindSpeed = windSpeedToPolarMap.values().iterator().next();

            for (Polar polar : windSpeedToPolarMap.values()) {
                if (polar.getWindSpeed() > polarAtHighestWindSpeed.getWindSpeed()) {
                    polarAtHighestWindSpeed = polar;
                }
            }
            closestPolars.add(polarAtHighestWindSpeed);

            return closestPolars;
        }

        Polar equalWindSpeedPolar = windSpeedEqualPolarWindSpeed(windSpeed);

        if (equalWindSpeedPolar != null) {
            closestPolars.add(equalWindSpeedPolar);

            return closestPolars;
        }

        //if windspeed is < max windspeed && windspeed is not equal to a windspeed of a polar
        //Get closest polar with windspeed less than given wind speed
        Polar closestLowerPolar = windSpeedToPolarMap.values().iterator().next();
        double closestLowerDistance = Math.abs(windSpeed - closestLowerPolar.getWindSpeed());

        for (Polar currentPolar : windSpeedToPolarMap.values()) {
            if (currentPolar.getWindSpeed() < windSpeed) {
                double currentDistance = Math.abs(windSpeed - currentPolar.getWindSpeed());

                if (closestLowerDistance > currentDistance || closestLowerPolar.getWindSpeed() > windSpeed) {
                    closestLowerPolar = currentPolar;
                    closestLowerDistance = currentDistance;
                }
            }
        }

        //Get closest polar with windspeed greater than given wind speed
        Polar closestUpperPolar = windSpeedToPolarMap.values().iterator().next();
        double closestUpperDistance = Math.abs(windSpeed - closestUpperPolar.getWindSpeed());

        for (Polar currentPolar : windSpeedToPolarMap.values()) {
            if (currentPolar.getWindSpeed() > windSpeed) {
                double currentDistance = Math.abs(windSpeed - currentPolar.getWindSpeed());
                if (closestUpperDistance > currentDistance || closestUpperPolar.getWindSpeed() < windSpeed) {
                    closestUpperPolar = currentPolar;
                    closestUpperDistance = currentDistance;
                }
            }
        }

        closestPolars.add(closestLowerPolar);
        closestPolars.add(closestUpperPolar);

        return closestPolars;
    }


    /**
     * Method to find the point with the closest angle to a given TWA
     * For each of the two polars, the two closest point on that polar are returned.
     * One point will be greater than the given TWA and one will be less than.
     * A polar will return a singular point of the TWA is equal to an angle on the given polar
     * A polar will return a singular point if the TWA is above the max value in the polar
     * A polar will  return a singular point in the TWA is below the min value in the polar
     *
     * @param boatTWA       double, the direction of the wind
     * @param closestPolars List<Polar>, list containing 1 or 2 polars
     * @return List<XYPair>, for each item: X = Windspeed of polar, Y = Angle
     */
    public List<XYPair> getClosestPoints(double boatTWA, List<Polar> closestPolars) {
        List<XYPair> closestPoints = new ArrayList<>();

        for (Polar polar : closestPolars) {

            if (boatTWA > polar.getMaxAngle()) {
                double maxAngle = -1;

                for (double angle : polar.getMapSpeedAtAngles().keySet()) {
                    if (angle > maxAngle) {
                        maxAngle = angle;
                    }
                }

                XYPair maxAnglePair = new XYPair(polar.getWindSpeed(), maxAngle);

                closestPoints.add(maxAnglePair);
                continue;
            }

            if (boatTWA < polar.getMinAngle()) {
                double minAngle = 181;

                for (double angle : polar.getMapSpeedAtAngles().keySet()) {
                    if (angle < minAngle) {
                        minAngle = angle;
                    }
                }

                XYPair minAnglePair = new XYPair(polar.getWindSpeed(), minAngle);

                closestPoints.add(minAnglePair);
                continue;
            }

            if (polar.getMapSpeedAtAngles().keySet().contains(boatTWA)) {
                XYPair twaContainedPair = new XYPair(polar.getWindSpeed(), boatTWA);
                closestPoints.add(twaContainedPair);
                continue;
            }

            //If boat TWA is not above max and not below min and not included in polar

            //Find closest below
            double closestLowerAngle = polar.getMapSpeedAtAngles().keySet().iterator().next();
            double closestLowerDistance = Math.abs(boatTWA - closestLowerAngle);

            for (double currentAngle : polar.getMapSpeedAtAngles().keySet()) {
                if (currentAngle < boatTWA) {
                    double currentDistance = Math.abs(boatTWA - currentAngle);

                    if (currentDistance <= closestLowerDistance || closestLowerAngle > boatTWA) {
                        closestLowerAngle = currentAngle;
                        closestLowerDistance = currentDistance;
                    }
                }
            }

            XYPair closestLowerPair = new XYPair(polar.getWindSpeed(), closestLowerAngle);

            //Find closest above
            double closestUpperAngle = polar.getMapSpeedAtAngles().keySet().iterator().next();
            double closestUppperDistacnce = Math.abs(boatTWA - closestUpperAngle);

            for (double currentAngle : polar.getMapSpeedAtAngles().keySet()) {
                if (currentAngle > boatTWA) {
                    double currentDistance = Math.abs(boatTWA - currentAngle);

                    if (currentDistance <= closestUppperDistacnce || closestUpperAngle < boatTWA) {
                        closestUpperAngle = currentAngle;
                        closestUppperDistacnce = currentDistance;
                    }
                }
            }

            XYPair closestUpperPair = new XYPair(polar.getWindSpeed(), closestUpperAngle);

            closestPoints.add(closestLowerPair);
            closestPoints.add(closestUpperPair);

        }

        return closestPoints;
    }


    /**
     * Calculates the boatSpeed of a point on a singular polar
     *
     * @param points List<XYPiar>, list of points on a polar length 1 or 2.
     *               For point in list X = Polar windSpeed Y = angle.
     *               Points must be from the same polar.
     * @param boatTWA double, the true wind angle of the boat
     * @return double, the calculates windSpeed on a polar at the boats TWA
     */
    public double getValueForPolar(List<XYPair> points, double boatTWA){
        Polar polar = getPolarForWindSpeed(points.get(0).getX());
        double speed = 0.0;
        if (points.size() == 2) {
            //List of XYPairs where X = Angle and Y = boatspeed at angle
            List<XYPair> speedAtPoints = new ArrayList<>();

            XYPair pointA = new XYPair(points.get(0).getY(), polar.getMapSpeedAtAngles().get(points.get(0).getY()));
            XYPair pointB = new XYPair(points.get(1).getY(), polar.getMapSpeedAtAngles().get(points.get(1).getY()));

            speedAtPoints.add(pointA);
            speedAtPoints.add(pointB);


            speed = calculateYAtXbetween2points(speedAtPoints, boatTWA);

        } else {
            if (boatTWA > polar.getMaxAngle()) {
                double max = polar.getMaxAngle();
                double dropOffRate = (polar.getWindSpeed()/(180 - max)) / 2;
                double speedAtMax = polar.getMapSpeedAtAngles().get(max);

                speed =  speedAtMax - (dropOffRate * (boatTWA - max));

            } else if (boatTWA < polar.getMinAngle()) {
                double min = polar.getMinAngle();
                double dropOffRate = (polar.getWindSpeed()/(min)) / 2;
                double speedAtMin = polar.getMapSpeedAtAngles().get(min);

                speed =  speedAtMin - (dropOffRate * (boatTWA - min));

            } else { //boatTWA is equal to a value in the polar
                for (Double angle : polar.getMapSpeedAtAngles().keySet()){
                    if (angle == points.get(0).getY()) {
                        speed = polar.getMapSpeedAtAngles().get(angle);
                    }
                }
            }
        }
        return speed;
    }


    public double getSpeedForBoat(double boatTWA,double windSpeed) {
        List<Polar> closestPolars = getClosestPolars(windSpeed);
        List<XYPair> closestPoints = getClosestPoints(boatTWA, closestPolars);

        List<List<XYPair>> splitClosestPoints = new ArrayList<>();

        //Splitting the points into lists based on windSpeed
        for (XYPair point : closestPoints) {
            boolean found = false;

            for (List<XYPair> polar : splitClosestPoints) {
                if (polar.get(0).getX() == point.getX()) {
                    found = true;
                    polar.add(point);
                }
            }

            if (!found) {
                List<XYPair> newPolar = new ArrayList<>();
                newPolar.add(point);
                splitClosestPoints.add(newPolar);
            }
        }

        //List of XYPairs X = windSpeed Y = calculated boatSpeed at TWA
        List<XYPair> valuesForWindspeeds = new ArrayList<>();

        for (List<XYPair> polar : splitClosestPoints) {
            double boatSpeedForPolar = getValueForPolar(polar, boatTWA);
            valuesForWindspeeds.add(new XYPair(polar.get(0).getX(), boatSpeedForPolar));
        }

        double boatSpeed;

        if (valuesForWindspeeds.size() == 2) { //If two polars used
            for (XYPair pair : valuesForWindspeeds){
            }
            boatSpeed = calculateYAtXbetween2points(valuesForWindspeeds, windSpeed);
        } else {//Only one polar
            if (windSpeed > closestPolars.get(0).getWindSpeed()) {  //If above highest polar value
                double maxWindspeed = closestPolars.get(0).getWindSpeed();

                boatSpeed = valuesForWindspeeds.get(0).getY() * (windSpeed / maxWindspeed);
            } else {
                boatSpeed = valuesForWindspeeds.get(0).getY();
            }
        }

        return boatSpeed;
    }


    /**
     * Check is given windSpeed is above the windSpeed of the polar with the max windSpeed
     * Used in getTwoClosestPolars()
     *
     * @param windSpeed double, speed of the wind
     * @return boolean, true if the given windspeed is above the windSpeed of the polar with the max windSpeed
     */
    private boolean windSpeedIsAboveMaxPolar(double windSpeed) {
        boolean aboveMaxGiven = true;

        for (Polar polar : windSpeedToPolarMap.values()) {
            if (polar.getWindSpeed() >= windSpeed) {
                aboveMaxGiven = false;
            }
        }
        return aboveMaxGiven;
    }


    /**
     * Checks if given windSpeed is equal to the windSpeed of a Polar
     * Used om getTwoClosestPolars()
     *
     * @param windSpeed double, the speed of the wind
     * @return Polar, null or polar with windSpeed equal to given windSpeed
     */
    private Polar windSpeedEqualPolarWindSpeed(double windSpeed) {
        Polar equalWindSpeedPolar = null;

        for (Polar polar : windSpeedToPolarMap.values()) {
            if (polar.getWindSpeed() == windSpeed) {
                equalWindSpeedPolar = polar;
            }
        }

        return equalWindSpeedPolar;
    }

    /**
     * Estimates a value for Y at a X where X is between two given points.
     *
     * @param points List<XYPair>, of size two, both points outside of X
     * @param x double, an X value to calculate Y for
     * @return double, the estimated y value
     */
    private double calculateYAtXbetween2points(List<XYPair> points, double x) {
        XYPair pointA = points.get(0);
        XYPair pointB = points.get(1);

        double distanceFromPointA = Math.abs(x - pointA.getX());
        double distanceFromPointB = Math.abs(x - pointB.getX());
        double totalDistance = distanceFromPointA + distanceFromPointB;

        double weightPointA = distanceFromPointB / totalDistance;
        double weightPointB = distanceFromPointA / totalDistance;

        double pointAComponet = (pointA.getY() * weightPointA);
        double pointBComponet = (pointB.getY() * weightPointB);

        return pointAComponet + pointBComponet;
    }



}
