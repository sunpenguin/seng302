package seng302.team18.util;

import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.Mark;

import java.util.ArrayList;

/**
 * Class for making GPS calculations
 * source: http://www.movable-type.co.uk/scripts/latlong.html
 */

public class GPSCalculations {

    private static double aveLat;
    private double minX = Double.MAX_VALUE;
    private double maxX = -(Double.MAX_VALUE);
    private double minY = Double.MAX_VALUE;
    private double maxY = -(Double.MAX_VALUE);


    /**
     * Constructor for GPSCalculations.  It also calculates the center point of all given latitude.
     *
     * @param course a given course
     */
    public GPSCalculations(Course course) {
        ArrayList<XYPair> XY = new ArrayList<>();
        int totalMarks = 0;

        for (CompoundMark cM : course.getCompoundMarks()) {
            for (Mark m : cM.getMarks()) {
                double lat = m.getCoordinates().getLatitude();
                double lon = m.getCoordinates().getLongitude();
                XYPair xy = GPSxy(new Coordinate(lat, lon));
                XY.add(totalMarks, xy);
                totalMarks += 1;
            }
        }

        int i;
        double latTotal = 0;
        double lonTotal = 0;
        double Z = 0;

        for (i = 0; i < XY.size(); i++) {
            latTotal += XY.get(i).getX();
            lonTotal += XY.get(i).getY();
            Z += Math.sin(XY.get(i).getX());
        }

        double x = latTotal / totalMarks;
        double y = lonTotal / totalMarks;
        double z = Z / totalMarks;
        double hyp = Math.sqrt(x * x + y * y);
        aveLat = Math.atan2(z, hyp);
    }

    /**
     * Given 2 sets of GPS coordinates in signed decimal degrees, return the distance
     * in metres between them.
     *
     * @param point1 Coordinates for point1
     * @param point2 Coordinates for point2
     * @return The distance in metres
     */
    public static double GPSDistance(Coordinate point1, Coordinate point2) {

        double lat1 = point1.getLatitude();
        double long1 = point1.getLongitude();

        double lat2 = point2.getLatitude();
        double long2 = point2.getLongitude();

        double earthRadius = 6371e3; // meters

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double difLatitudeRad = Math.toRadians(lat2 - lat1);
        double difLongitudeRad = Math.toRadians(long2 - long1);

        double a = Math.sin(difLatitudeRad / 2) * Math.sin(difLatitudeRad / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(difLongitudeRad / 2) * Math.sin(difLongitudeRad / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    public static Coordinate coordinateToCoordinate(Coordinate initialCoord, double bearing, double distance) {

        double bR = Math.toRadians(bearing);
        double lat1R = Math.toRadians(initialCoord.getLatitude());
        double lon1R = Math.toRadians(initialCoord.getLongitude());
        double dR = (distance / 1000.0) / 6371.0;

        double a = Math.sin(dR) * Math.cos(lat1R);
        double lat2 = Math.asin(Math.sin(lat1R) * Math.cos(dR) + a * Math.cos(bR));
        double lon2 = lon1R
                + Math.atan2(Math.sin(bR) * a, Math.cos(dR) - Math.sin(lat1R) * Math.sin(lat2));
        return new Coordinate(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }


    public static double retrieveHeading(Coordinate c1, Coordinate c2) {
        double lon1 = Math.toRadians(c1.getLongitude());
        double lat1 = Math.toRadians(c1.getLatitude());
        double lon2 = Math.toRadians(c2.getLongitude());
        double lat2 = Math.toRadians(c2.getLatitude());

        double x = Math.cos(lat2) * Math.sin(lon2 - lon1);
        double y = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
        return Math.toDegrees(Math.atan2(x, y));
    }

    /**
     * Given 2 sets GPS coordinates in signed decimal degrees, return the coordinates
     * of the midpoint of these 2 points
     *
     * @param point1 Coordinates for point1
     * @param point2 Coordinates for point2
     * @return The coordinates of the midpoint
     */
    public static Coordinate GPSMidpoint(Coordinate point1, Coordinate point2) {

        double lat1 = point1.getLatitude();
        double long1 = point1.getLongitude();

        double lat2 = point2.getLatitude();
        double long2 = point2.getLongitude();

        double dLon = Math.toRadians(long2 - long1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        long1 = Math.toRadians(long1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double long3 = long1 + Math.atan2(By, Math.cos(lat1) + Bx);

        long3 = Math.toDegrees(long3);
        lat3 = Math.toDegrees(lat3);

        return new Coordinate(lat3, long3);
    }

    /**
     * Method to convert a given Coordinate from longitude and latitude to
     * x, y values.
     * Source: http://stackoverflow.com/questions/16266809/convert-from-latitude-longitude-to-x-y
     *
     * @param point The coordinates to convert
     */
    public static XYPair GPSxy(Coordinate point) {
        double earthRadius = 6371e3; // meters
        double aspectLat = Math.cos(aveLat);
        double x = earthRadius * Math.toRadians(point.getLongitude()) * aspectLat;
        double y = earthRadius * Math.toRadians(point.getLatitude());
        return new XYPair(x, y);
    }

    public static Coordinate XYToCoordinate(XYPair point) {
        double earthRadius = 6371e3; // meters
        double aspectLat = Math.cos(aveLat);
        double latitude = Math.toDegrees(point.getY() / earthRadius);
        double longitude = Math.toDegrees((point.getX() / earthRadius) / aspectLat);
        return new Coordinate(latitude, longitude);
    }

    public static double findAngle(Coordinate departure, Coordinate destination) {
        XYPair origin = GPSCalculations.GPSxy(departure);
        XYPair target = GPSCalculations.GPSxy(destination);
        double angle = Math.toDegrees(Math.atan2(target.getY() - origin.getY(), target.getX() - origin.getX())) - 90;
        if (angle < 0) {
            angle += 360;
        } else if (angle > 360) {
            angle -= 360;
        }
        return 360 - angle;
    }

    public void findMinMaxPoints(Course course) {
        for (Coordinate boundary : course.getBoundaries()) {
            XYPair boundaryXYValues = GPSxy(boundary);
            double xValue = boundaryXYValues.getX();
            double yValue = boundaryXYValues.getY();
            if (xValue < minX) {
                minX = xValue;
            }
            if (xValue > maxX) {
                maxX = xValue;
            }
            if (yValue < minY) {
                minY = yValue;
            }
            if (yValue > maxY) {
                maxY = yValue;
            }
        }
        for (CompoundMark compoundMark : course.getCompoundMarks()) {
            for (Mark mark : compoundMark.getMarks()) {
                XYPair markXYValues = GPSxy(mark.getCoordinates());
                double xValue = markXYValues.getX();
                double yValue = markXYValues.getY();
                if (xValue < minX) {
                    minX = xValue;
                }
                if (xValue > maxX) {
                    maxX = xValue;
                }
                if (yValue < minY) {
                    minY = yValue;
                }
                if (yValue > maxY) {
                    maxY = yValue;
                }
            }
        }
    }

    /**
     * Getter for the minimum xValue
     *
     * @return minimum x value
     */
    public double getMinX() {
        return this.minX;
    }

    /**
     * Getter for the maximum xValue
     *
     * @return maximum x value
     */
    public double getMaxX() {
        return this.maxX;
    }

    /**
     * Getter for the minimum yValue
     *
     * @return minimum y value
     */
    public double getMinY() {
        return this.minY;
    }

    /**
     * Getter for the maximum yValue
     *
     * @return maximum y value
     */
    public double getMaxY() {
        return this.maxY;
    }
}

