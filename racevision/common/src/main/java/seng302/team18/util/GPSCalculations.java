package seng302.team18.util;

import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for making GPS calculations
 * source: http://www.movable-type.co.uk/scripts/latlong.html
 */

public class GPSCalculations {

    /**
     * Constructor for GPSCalculations.
     */
    public GPSCalculations() {
    }

    /**
     * Given 2 sets of GPS coordinates in signed decimal degrees, return the distance in metres between them.
     *
     * @param point1 Coordinates for point1
     * @param point2 Coordinates for point2
     * @return The distance in metres
     */
    public double distance(Coordinate point1, Coordinate point2) {

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


    /**
     * Calculates a new coordinate given a source coordinate, a distance,
     * and a bearing from that coordinate to the destination coordinate
     *
     * @param initialCoord the initial coordinate
     * @param bearing the bearing from the initial coordinate to the destination coordinate
     * @param distance the distance from the initial coordinate to the destination coordinate
     * @return the destination coordinate
     */
    public Coordinate toCoordinate(Coordinate initialCoord, double bearing, double distance) {

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


    /**
     * Given 2 sets of coordinates in signed decimal degrees, return the coordinates
     * of the midPoint of these 2 points
     *
     * @param point1 Coordinates for point1
     * @param point2 Coordinates for point2
     * @return The coordinates of the midPoint
     */
    public Coordinate midPoint(Coordinate point1, Coordinate point2) {

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
     * Returns the bearing (angle from north going clockwise) between the origin and destination.
     *
     * Pre condition: origin and destination are not null, have latitude between -90 and 90,
     * longitude between -180 and 180.
     *
     * Post condition: angle from north to destination calculated at origin.
     *
     * @param origin starting point.
     * @param destination end point.
     * @return angle from north to destination calculated at origin.
     */
    public double getBearing(Coordinate origin, Coordinate destination) {
        double deltaLong = (destination.getLongitude() - origin.getLongitude());

        double y = Math.sin(deltaLong) * Math.cos(destination.getLatitude());
        double x = Math.cos(origin.getLatitude()) * Math.sin(destination.getLatitude()) - Math.sin(origin.getLatitude())
                * Math.cos(destination.getLatitude()) * Math.cos(deltaLong);

        double bearing = Math.atan2(y, x);

        bearing = Math.toDegrees(bearing);
        bearing = (bearing + 360) % 360;

        return bearing;
    }


    /**
     * Gets the central coordinate calculated from the list of given coordinates.
     * @param coordinates you want to find the center of
     * @return the central coordinate.
     */
    public Coordinate getCentralCoordinate(List<Coordinate> coordinates) {
        double x = 0;
        double y = 0;
        double z = 0;

        for (Coordinate coordinate : coordinates) {
            double latitude = coordinate.getLatitude() * Math.PI / 180;
            double longitude = coordinate.getLongitude() * Math.PI / 180;

            x += Math.cos(latitude) * Math.cos(longitude);
            y += Math.cos(latitude) * Math.sin(longitude);
            z += Math.sin(latitude);
        }

        double total = coordinates.size();

        x = x / total;
        y = y / total;
        z = z / total;

        double centralLongitude = Math.atan2(y, x);
        double centralSquareRoot = Math.sqrt(x * x + y * y);
        double centralLatitude = Math.atan2(z, centralSquareRoot);

        return new Coordinate(centralLatitude * 180 / Math.PI, centralLongitude * 180 / Math.PI);
    }


}


