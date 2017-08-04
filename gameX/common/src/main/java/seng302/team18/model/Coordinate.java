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
        this.latitude = latitude;
        this.longitude = longitude;

    }


    /**
     * latitude getter
     *
     * @return the latitude
     */
    public Double getLatitude() {
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
    public Double getLongitude() {
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
