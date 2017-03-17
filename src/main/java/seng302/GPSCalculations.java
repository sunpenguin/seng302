package seng302;

/**
 * Class for making GPS calculations
 * source: http://www.movable-type.co.uk/scripts/latlong.html
 */

public class GPSCalculations {

    private double minX = Double.MAX_VALUE;
    private double maxX = -(Double.MAX_VALUE);
    private double minY = Double.MAX_VALUE;
    private double maxY = -(Double.MAX_VALUE);
    private static int i = 0;

    /**
     * Given 2 sets of GPS coordinates in signed decimal degrees, return the distance
     * in metres between them.
     * @param point1 Coordinates for point1
     * @param point2 Coordinates for point2
     * @return The distance in metres
     */
    public static double GPSDistance(Coordinate point1, Coordinate point2) {

        double  lat1 = point1.getLatitude();
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


    public void findMinMaxPoints(Course course) {

        for (CompoundMark compoundMark : course.getCompoundMarks()) {
            for (Mark mark : compoundMark.getMarks()) {
                Coordinate markCoordinates = mark.getMarkCoordinates();
                XYPair markXYValues = GPSxy(markCoordinates);

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
     * @return minimum x value
     */
    public double getMinX() {
        return this.minX;
    }


    /**
     * Getter for the maximum xValue
     * @return maximum x value
     */
    public double getMaxX() {
        return this.maxX;
    }


    /**
     * Getter for the minimum yValue
     * @return minimum y value
     */
    public double getMinY() {
        return this.minY;
    }


    /**
     * Getter for the maximum yValue
     * @return maximum y value
     */
    public double getMaxY() {
        return this.maxY;
    }


    /**
     * Given 2 sets GPS coordinates in signed decimal degrees, return the coordinates
     * of the midpoint of these 2 points
     * @param point1 Coordinates for point1
     * @param point2 Coordinates for point2
     * @return The coordinates of the midpoint
     */
    public static Coordinate GPSMidpoint(Coordinate point1, Coordinate point2) {

        double lat1 = point1.getLatitude();
        double long1 = point1.getLongitude();

        double lat2 = point2.getLatitude();
        double long2 = point2.getLongitude();

        double bearingX = Math.cos(lat2) * Math.cos(long2 - long1);
        double bearingY = Math.cos(lat2) * Math.sin(long2 - long1);

        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2),
                Math.sqrt((Math.cos(lat1) + bearingX) * (Math.cos(lat1) + bearingX) + bearingY * bearingY));

        double long3 = long1 + Math.atan2(bearingY, Math.cos(lat1) + bearingX);


        return new Coordinate(lat3, long3); // NOTE: latitude not computing correctly!!!
    }


    /**
     * Method to convert a given Coordinate from longitude and latitude to
     * x, y values.
     * Source: http://stackoverflow.com/questions/16266809/convert-from-latitude-longitude-to-x-y
     * @param point The coordinates to convert
     */
    public static XYPair GPSxy (Coordinate point) {

        double earthRadius = 6371e3; // meters
        double aspectLat = Math.cos(32.308046); // Aspect ratio, use a latitude that is the mean of all given
        System.out.println(i++ + " " + point.getLongitude());
        double x = earthRadius * Math.toRadians(point.getLongitude()) * aspectLat;
        double y = earthRadius * Math.toRadians(point.getLatitude());

        return new XYPair(x, y);
    }
}


