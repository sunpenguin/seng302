package seng302.team18.util;

import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Class for making GPS calculations.
 */

public class GPSCalculator {

    /**
     * Constructor for GPSCalculator.
     */
    public GPSCalculator() {
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
     * @param bearing      the bearing from the initial coordinate to the destination coordinate
     * @param distance     the distance (in meters) from the initial coordinate to the destination coordinate
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
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2),
                Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double long3 = long1 + Math.atan2(By, Math.cos(lat1) + Bx);

        long3 = Math.toDegrees(long3);
        lat3 = Math.toDegrees(lat3);

        return new Coordinate(lat3, long3);
    }


    /**
     * Returns the bearing (angle from north going clockwise) between the origin and destination.
     * <p>
     * Pre condition: origin and destination are not null, have latitude between -90 and 90,
     * longitude between -180 and 180.
     * <p>
     * Post condition: angle from north to destination calculated at origin.
     *
     * @param origin      starting point.
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
     * NOTE: Calculates the center of gravity
     *
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


    /**
     * Calculate the coordinates of the upper-left and lower-right coordinates of a bounding box of a
     * given list of coordinates
     *
     * @param points list of coordinates
     * @return The list of coordinates where index 0 is the upper left and index 1 is the lower right
     */
    public synchronized List<Coordinate> findMinMaxPoints(List<Coordinate> points) {
        List<Coordinate> result = new ArrayList<>();
        double minLong = 180;
        double maxLong = -180;
        double minLat = 90;
        double maxLat = -90;

        for (Coordinate point : points) {
            if (point.getLongitude() < minLong) {
                minLong = point.getLongitude();
            }

            if (point.getLongitude() > maxLong) {
                maxLong = point.getLongitude();
            }

            if (point.getLatitude() < minLat) {
                minLat = point.getLatitude();
            }

            if (point.getLatitude() > maxLat) {
                maxLat = point.getLatitude();
            }
        }

        result.add(new Coordinate(maxLat, minLong)); // North East
        result.add(new Coordinate(minLat, maxLong)); // South West

        return result;
    }


    /**
     * Calculate the coordinates of the upper-left and lower-right coordinates of a bounding box of a
     * given course
     *
     * @param course The course
     * @return The list of coordinates where index 0 is the upper left and index 1 is the lower right
     */
    public synchronized List<Coordinate> findMinMaxPoints(Course course) {
        List<Coordinate> points = new ArrayList<>();

        points.addAll(course.getLimits());
        points.addAll(course.getCompoundMarks().stream()
                .flatMap(compoundMark -> compoundMark.getMarks().stream().map(Mark::getCoordinate))
                .collect(Collectors.toList())
        );

        return findMinMaxPoints(points);
    }


    /**
     * Returns true if bearing is between start and finish in a clockwise sense.
     *
     * @param bearing less than or equal to 360 degrees
     * @param start   less than or equal to 360 degrees
     * @param finish  less than or equal to 360 degrees
     * @return if bearing is between start and finish in a clockwise sense
     */
    public boolean isBetween(double bearing, double start, double finish) {
        finish = (finish - start + 360) % 360;
        bearing = (bearing - start + 360) % 360;
        return bearing <= finish;
    }


    /**
     * Detects whether or not a certain coordinate is located inside a polygon of coordinate points.
     *
     * @param location The location point you are checking is inside the polygon.
     * @param boundary The polygon.
     * @return True if the location is inside the polygon, false if it is outside.
     */
    public boolean isInside(Coordinate location, List<Coordinate> boundary) {
        if (boundary.size() < 3) {
            return true;
        }
        Coordinate lastPoint = boundary.get(boundary.size() - 1);
        Boolean isInside = false;
        double x = location.getLongitude();
        for (Coordinate point : boundary) {
            double x1 = lastPoint.getLongitude();
            double x2 = point.getLongitude();
            double dx = x2 - x1;

            if (Math.abs(dx) > 180.0) {
                // we have, most likely, just jumped the dateline (could do further validation to this effect if needed).  normalise the numbers.
                if (x > 0) {
                    while (x1 < 0)
                        x1 += 360;
                    while (x2 < 0)
                        x2 += 360;
                } else {
                    while (x1 > 0)
                        x1 -= 360;
                    while (x2 > 0)
                        x2 -= 360;
                }
                dx = x2 - x1;
            }

            if ((x1 <= x && x2 > x) || (x1 >= x && x2 < x)) {
                double grad = (point.getLatitude() - lastPoint.getLatitude()) / dx;
                double intersectAtLat = lastPoint.getLatitude() + ((x - x1) * grad);

                if (intersectAtLat > location.getLatitude()) {
                    isInside = !isInside;
                }
            }
            lastPoint = point;
        }

        return isInside;
    }


    /**
     * Randomly generate a point inside the given race.
     *
     * @param race to generate a point for.
     * @return a random point inside the boundary of the race.
     */
    public Coordinate randomPoint(Race race) {
        List<Coordinate> bounds = race.getCourse().getLimits();
        List<Coordinate> corners = findMinMaxPoints(bounds);
        Coordinate topLeft = corners.get(0);
        Coordinate bottomRight = corners.get(1);
        return randomPoint(race.getStartingList(), race.getCourse(), topLeft, bottomRight);
    }


    /**
     * Randomly generate a point inside the racing area.
     *
     * @param startingList boats that are participating in the race.
     * @param course course to check for.
     * @param topLeft top left corner
     * @param bottomRight bottom right corner
     * @return a valid random point inside the racing area
     */
    private Coordinate randomPoint(List<Boat> startingList, Course course, Coordinate topLeft, Coordinate bottomRight) {
        Random random = new Random();
        double randY = random.nextDouble();
        double randX = random.nextDouble();
        double newX = (topLeft.getLatitude() - bottomRight.getLatitude()) * randX + bottomRight.getLatitude();
        double newY = (bottomRight.getLongitude() - topLeft.getLongitude()) * randY + topLeft.getLongitude();
        Coordinate randomPoint = new Coordinate(newX, newY);

        double xSize = (bottomRight.getLongitude() - topLeft.getLongitude()) / 40;
        double ySize = (topLeft.getLatitude() - bottomRight.getLatitude()) / 40;

        if (isValidPoint(startingList, course, randomPoint, xSize, ySize)) {
            return randomPoint;
        }

        return randomPoint(startingList, course, topLeft, bottomRight);
    }


    /**
     * Checks if a random point generated overlaps with any existing course feature.
     * First checks if the point is inside the race course.
     * If so, get and construct boxes around each obstacles and check if the point exists inside the box.
     *
     * @param startingList boats that are participating in the race.
     * @param course course to check for.
     * @param randomPoint to be checked.
     * @param xSize x-size of half of the square used to access course elements.
     * @param ySize y-size of half of the square used to access course elements.
     * @return true if the point is in the course and does not overlap with other elements.
     */
    private boolean isValidPoint(List<Boat> startingList, Course course, Coordinate randomPoint, double xSize, double ySize) {
        if (!isInside(randomPoint, course.getLimits())) {
            return false;
        }

        List<Coordinate> coordinates = new ArrayList<>();

        for (Boat boat : startingList) {
            coordinates.add(boat.getCoordinate());
        }

        for (Mark mark : course.getMarks()) {
            coordinates.add(mark.getCoordinate());
        }

        for (Coordinate coordinate : coordinates) {
            List<Coordinate> tempBounds = new ArrayList<>();
            tempBounds.add(new Coordinate(coordinate.getLatitude() - xSize, coordinate.getLongitude() - ySize));
            tempBounds.add(new Coordinate(coordinate.getLatitude() + xSize, coordinate.getLongitude() - ySize));
            tempBounds.add(new Coordinate(coordinate.getLatitude() + xSize, coordinate.getLongitude() + ySize));
            tempBounds.add(new Coordinate(coordinate.getLatitude() - xSize, coordinate.getLongitude() + ySize));

            if (isInside(randomPoint, tempBounds)) {
                return false;
            }
        }

        return true;
    }

}


