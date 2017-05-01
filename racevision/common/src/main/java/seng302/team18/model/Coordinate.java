package seng302.team18.model;

/**
 * A class that contains both a latitude and a longitude angle to represent a coordinate used to determine the position
 * of both boats and Compound marks on the course
 */
public class Coordinate {

    private double latitude;
    private double longitude;


    /**
     * Coordinate class constructor
     *
     * @param latitude  value representing latitude
     * @param longitude value representing longitude
     */
    public Coordinate(double latitude, double longitude) {
        if (-90.0 <= latitude && latitude <= 90.0) {
            if (-180.0 <= longitude && longitude <= 180.0) {
                this.latitude = latitude;
                this.longitude = longitude;
            } else {
                System.err.println("Longitude must be between -180 and 180"); // TODO maybe change to exceptions
            }
        } else {
            System.err.println("Latitude must be between -90 and 90");
        }
    }


    /**
     * latitude getter
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }


    /**
     * latitude setter
     *
     * @param latitude value for the latitude
     */
    public void setLatitude(double latitude) { // TODO throw exception?
        if (-90 <= latitude && latitude <= 90.0) {
            this.latitude = latitude;
        }
    }


    /**
     * longitude getter
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }


    /**
     * longitude setter
     *
     * @param longitude value for the longitude
     */
    public void setLongitude(double longitude) { // TODO throw exception?
        if (-180 <= longitude && longitude <= 180) {
            this.longitude = longitude;
        }
    }

    public double retrieveHeading(Coordinate destination) {
        double lon1 = Math.toRadians(longitude);
        double lat1 = Math.toRadians(latitude);
        double lon2 = Math.toRadians(destination.getLongitude());
        double lat2 = Math.toRadians(destination.getLatitude());

        double x = Math.cos(lat2) * Math.sin(lon2 - lon1);
        double y = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
        return Math.toDegrees(Math.atan2(x, y));
    }

    public double distance(Coordinate point) {
        double lat1 = getLatitude();
        double long1 = getLongitude();
        double lat2 = point.getLatitude();
        double long2 = point.getLongitude();
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        return Double.compare(that.longitude, longitude) == 0;
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
