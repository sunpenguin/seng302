package seng302;

import java.util.ArrayList;

/**
 * Class for making GPS calculations
 * source: http://www.movable-type.co.uk/scripts/latlong.html
 */

public class GPSCalculations {

    private double minX = Double.MAX_VALUE;
    private double maxX = -(Double.MAX_VALUE);
    private double minY = Double.MAX_VALUE;
    private double maxY = -(Double.MAX_VALUE);
    private static double aveLat;

//

    /**
     * Constructor for GPSCalculations.  It also calculates the center point of all given latitude.
     * @param course a given course
     */
    public GPSCalculations(Course course){
//        double start1 = course.getCompoundMarks().get(0).getMarks().get(0).getMarkCoordinates().getLatitude();
//        double start2 = course.getCompoundMarks().get(0).getMarks().get(1).getMarkCoordinates().getLatitude();
//
//        double mark1 = course.getCompoundMarks().get(1).getMarks().get(0).getMarkCoordinates().getLatitude();
//
//        double northGate1 = course.getCompoundMarks().get(2).getMarks().get(0).getMarkCoordinates().getLatitude();
//        double northGate2 = course.getCompoundMarks().get(2).getMarks().get(1).getMarkCoordinates().getLatitude();
//
//        double southGate1 = course.getCompoundMarks().get(3).getMarks().get(0).getMarkCoordinates().getLatitude();
//        double southGate2 = course.getCompoundMarks().get(3).getMarks().get(1).getMarkCoordinates().getLatitude();
//
//        double finish1 = course.getCompoundMarks().get(5).getMarks().get(0).getMarkCoordinates().getLatitude();
//        double finish2 = course.getCompoundMarks().get(5).getMarks().get(1).getMarkCoordinates().getLatitude();
//
//
//
//        double start11 = course.getCompoundMarks().get(0).getMarks().get(0).getMarkCoordinates().getLongitude();
//        double start22 = course.getCompoundMarks().get(0).getMarks().get(1).getMarkCoordinates().getLongitude();
//
//        double mark11 = course.getCompoundMarks().get(1).getMarks().get(0).getMarkCoordinates().getLongitude();
//
//        double northGate11 = course.getCompoundMarks().get(2).getMarks().get(0).getMarkCoordinates().getLongitude();
//        double northGate22 = course.getCompoundMarks().get(2).getMarks().get(1).getMarkCoordinates().getLongitude();
//
//        double southGate11 = course.getCompoundMarks().get(3).getMarks().get(0).getMarkCoordinates().getLongitude();
//        double southGate22 = course.getCompoundMarks().get(3).getMarks().get(1).getMarkCoordinates().getLongitude();
//
//        double finish11 = course.getCompoundMarks().get(5).getMarks().get(0).getMarkCoordinates().getLongitude();
//        double finish22 = course.getCompoundMarks().get(5).getMarks().get(1).getMarkCoordinates().getLongitude();

        ArrayList<XYPair> XY = new ArrayList<>();
        int totalMarks = 0;

        for (CompoundMark cM : course.getCompoundMarks()) {
            for (Mark m : cM.getMarks()) {
                double lat = m.getMarkCoordinates().getLatitude();
                double lon = m.getMarkCoordinates().getLongitude();
                XYPair xy = GPSxy(new Coordinate(lat, lon));
                XY.add(totalMarks,xy);
                totalMarks += 1;
            }
        }

        int i;
        double latTotal = 0;
        double lonTotal = 0;
        double Z = 0;

        for (i = 0; i < XY.size();i++ ){
            latTotal += XY.get(i).getX();
            lonTotal += XY.get(i).getY();
            Z += Math.sin(XY.get(i).getX());
        }

        double x = latTotal / totalMarks;
        double y = lonTotal / totalMarks;
        double z = Z / totalMarks;
        double hyp = Math.sqrt(x * x + y * y);
        aveLat = Math.atan2(z, hyp);

//        double lat = Math.atan2(z, hyp);
//        double lon = Math.atan2(y, x);

//        XYPair aves = new XYPair(x,y);
//        Coordinate aveLatLon = XYToCoordinate(aves);
//        aveLat = aveLatLon.getLatitude();

//
//        XYPair xy1 = GPSxy(new Coordinate(start1,start11));
//        XYPair xy2 = GPSxy(new Coordinate(start2,start22));
//        XYPair xy3 = GPSxy(new Coordinate(mark1,mark11));
//        XYPair xy4 = GPSxy(new Coordinate(southGate1,southGate11));
//        XYPair xy5 = GPSxy(new Coordinate(southGate2,southGate22));
//        XYPair xy6 = GPSxy(new Coordinate(northGate1,northGate11));
//        XYPair xy7 = GPSxy(new Coordinate(northGate2,northGate22));
//        XYPair xy8 = GPSxy(new Coordinate(finish1,finish11));
//        XYPair xy9 = GPSxy(new Coordinate(finish2,finish22));

//        double x = (xy1.getX() + xy2.getX() +xy3.getX() + xy4.getX() + xy5.getX() +xy6.getX() +xy7.getX()
//                +xy8.getX() +xy9.getX()) / 9;
//
//        double y = (xy1.getY() + xy2.getY() +xy3.getY() + xy4.getY() + xy5.getY() +xy6.getY() +xy7.getY()
//                +xy8.getY() +xy9.getY()) / 9;
//
//        double z = (Math.sin(start1) + Math.sin(start2) + Math.sin(mark1) + Math.sin(northGate1) + Math.sin(northGate2)
//                + Math.sin(southGate1) + Math.sin(southGate2)
//                + Math.sin(finish1) + Math.sin(finish2)) / 9;
//
//        double hyp = Math.sqrt(x * x + y * y);

//        aveLat = Math.atan2(z, hyp);
    }

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
     * @param point The coordinates to convert
     */
    public static XYPair GPSxy(Coordinate point) {
        double earthRadius = 6371e3; // meters
//        double aspectLat = Math.cos(32.308046); // TODO Aspect ratio, use a latitude that is the mean of all given
        double aspectLat = Math.cos(aveLat);
        System.out.println(aveLat);
        double x = earthRadius * Math.toRadians(point.getLongitude()) * aspectLat;
        double y = earthRadius * Math.toRadians(point.getLatitude());
        return new XYPair(x, y);
    }

    public static Coordinate XYToCoordinate(XYPair point) {
        double earthRadius = 6371e3; // meters
//        double aspectLat = Math.cos(32.308046); // TODO Aspect ratio, use a latitude that is the mean of all given
        double aspectLat = Math.cos(aveLat);
        System.out.println(aveLat);
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
}


