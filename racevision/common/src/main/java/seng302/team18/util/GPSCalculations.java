package seng302.team18.util;

import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for making GPS calculations
 * source: http://www.movable-type.co.uk/scripts/latlong.html
 */

public class GPSCalculations {

//    private double aveLat;
    private double minX = Double.MAX_VALUE;
    private double maxX = -(Double.MAX_VALUE);
    private double minY = Double.MAX_VALUE;
    private double maxY = -(Double.MAX_VALUE);
    private Course course;


    /**
     * Constructor for GPSCalculations.  It also calculates the center point of all given latitude.
     *
     * @param course a given course
     */
    public GPSCalculations(Course course) {
        this.course = course;
    }

//    /**
//     * Given 2 sets of GPS coordinates in signed decimal degrees, return the distance
//     * in metres between them.
//     *
//     * @param point1 Coordinates for point1
//     * @param point2 Coordinates for point2
//     * @return The distance in metres
//     */
//    public double gpsDistance(Coordinate point1, Coordinate point2) {
//
//        double lat1 = point1.getLatitude();
//        double long1 = point1.getLongitude();
//
//        double lat2 = point2.getLatitude();
//        double long2 = point2.getLongitude();
//
//        double earthRadius = 6371e3; // meters
//
//        double lat1Rad = Math.toRadians(lat1);
//        double lat2Rad = Math.toRadians(lat2);
//
//        double difLatitudeRad = Math.toRadians(lat2 - lat1);
//        double difLongitudeRad = Math.toRadians(long2 - long1);
//
//        double a = Math.sin(difLatitudeRad / 2) * Math.sin(difLatitudeRad / 2) +
//                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
//                        Math.sin(difLongitudeRad / 2) * Math.sin(difLongitudeRad / 2);
//
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//
//        return earthRadius * c;
//    }

    public Coordinate coordinateToCoordinate(Coordinate initialCoord, double bearing, double distance) {

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


    public double retrieveHeading(Coordinate c1, Coordinate c2) {
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
    public static Coordinate gpsMidpoint(Coordinate point1, Coordinate point2) {

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
    public XYPair coordinateToPixel(Coordinate point) {
        double earthRadius = 6371e3; // meters
        double aspectLat = Math.cos(course.getCentralCoordinate().getLatitude());
        double x = earthRadius * Math.toRadians(point.getLongitude()) * aspectLat;
        double y = earthRadius * Math.toRadians(point.getLatitude());
        return new XYPair(x, y);
    }

    public Coordinate pixelToCoordinate(XYPair point) {
        double earthRadius = 6371e3; // meters
        double aspectLat = Math.cos(course.getCentralCoordinate().getLatitude());
        double latitude = Math.toDegrees(point.getY() / earthRadius);
        double longitude = Math.toDegrees((point.getX() / earthRadius) / aspectLat);
        return new Coordinate(latitude, longitude);
    }

    public double findAngle(Coordinate departure, Coordinate destination) {
        XYPair origin = coordinateToPixel(departure);
        XYPair target = coordinateToPixel(destination);
        double angle = Math.toDegrees(Math.atan2(target.getY() - origin.getY(), target.getX() - origin.getX())) - 90;
        if (angle < 0) {
            angle += 360;
        } else if (angle > 360) {
            angle -= 360;
        }
        return 360 - angle;
    }

    public void findMinMaxPoints() {
        for (BoundaryMark boundary : course.getBoundaries()) {
            XYPair boundaryXYValues = coordinateToPixel(boundary.getCoordinate());
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
                XYPair markXYValues = coordinateToPixel(mark.getCoordinate());
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


    public Coordinate getCentralCoordinate(List<Coordinate> geoCoordinates) {
        double x = 0;
        double y = 0;
        double z = 0;

        for (Coordinate geoCoordinate : geoCoordinates) {
            double latitude = geoCoordinate.getLatitude() * Math.PI / 180;
            double longitude = geoCoordinate.getLongitude() * Math.PI / 180;

            x += Math.cos(latitude) * Math.cos(longitude);
            y += Math.cos(latitude) * Math.sin(longitude);
            z += Math.sin(latitude);
        }

        double total = geoCoordinates.size();

        x = x / total;
        y = y / total;
        z = z / total;

        double centralLongitude = Math.atan2(y, x);
        double centralSquareRoot = Math.sqrt(x * x + y * y);
        double centralLatitude = Math.atan2(z, centralSquareRoot);

        return new Coordinate(centralLatitude * 180 / Math.PI, centralLongitude * 180 / Math.PI);

//        double totalLatitude = 0;
//        double totalLongitude = 0;
//
//        for (Coordinate coordinate : geoCoordinates) {
//            totalLatitude += coordinate.getLatitude();
//            totalLongitude += coordinate.getLongitude();
//        }
//
//        double latitude = totalLatitude / 4;
//        double longitude = totalLongitude / 4;
//
//        return new Coordinate(latitude, longitude);
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


