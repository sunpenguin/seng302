package seng302.team18.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A class which stores all polar information used for tacking/gybing.
 */
public class Polar {

    private double windSpeed;
    private Map<Double, Double> mapSpeedAtAngles;
    private double upWindAngle;
    private double upWindSpeed;
    private double downWindAngle;
    private double downWindSpeed;


    /**
     * Constructor for Polar.
     * Note: Adds Upwind and down wind angles to hashmap also.
     *
     * @param windSpeed     Speed of the wind
     * @param upWindAngle   Upwind threshold angle
     * @param upWindSpeed   Upwind speed
     * @param downWindAngle Downwind threshold angle
     * @param downWindSpeed Downwind speed
     */
    public Polar(double windSpeed, double upWindAngle, double upWindSpeed, double downWindAngle, double downWindSpeed) {
        this.windSpeed = windSpeed;
        this.upWindAngle = upWindAngle;
        this.upWindSpeed = upWindSpeed;
        this.downWindAngle = downWindAngle;
        this.downWindSpeed = downWindSpeed;
        mapSpeedAtAngles = new HashMap<>();
        addToMap(this.upWindAngle, this.upWindSpeed);
        addToMap(this.downWindAngle, this.downWindSpeed);
    }


    /**
     * Method to add a value and key to the hashmap of angles and speeds.
     *
     * @param trueWindAngle True wind angle for boat
     * @param boatSpeed     Speed boat travel at the angle
     */
    public void addToMap(double trueWindAngle, double boatSpeed) {
        mapSpeedAtAngles.put(trueWindAngle, boatSpeed);
    }


    public double getWindSpeed() {
        return windSpeed;
    }


    public Map<Double, Double> getMapSpeedAtAngles() {
        return mapSpeedAtAngles;
    }


    public double getUpWindAngle() {
        return upWindAngle;
    }


    public double getUpWindSpeed() {
        return upWindSpeed;
    }


    public double getDownWindAngle() {
        return downWindAngle;
    }


    public double getDownWindSpeed() {
        return downWindSpeed;
    }


    public double getMinAngle() {
        double currentMin = upWindAngle;
        for (Double angle : mapSpeedAtAngles.keySet()) {
            if (angle < currentMin) {
                currentMin = angle;
            }
        }
        return currentMin;
    }


    public double getMaxAngle() {
        double currentMax = downWindAngle;
        for (Double angle : mapSpeedAtAngles.keySet()) {
            if (angle > currentMax) {
                currentMax = angle;
            }
        }
        return currentMax;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Polar polar = (Polar) o;

        if (Double.compare(polar.windSpeed, windSpeed) != 0) return false;
        if (Double.compare(polar.upWindAngle, upWindAngle) != 0) return false;
        if (Double.compare(polar.upWindSpeed, upWindSpeed) != 0) return false;
        if (Double.compare(polar.downWindAngle, downWindAngle) != 0) return false;
        if (Double.compare(polar.downWindSpeed, downWindSpeed) != 0) return false;
        return mapSpeedAtAngles.equals(polar.mapSpeedAtAngles);
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(windSpeed);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + mapSpeedAtAngles.hashCode();
        temp = Double.doubleToLongBits(upWindAngle);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upWindSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(downWindAngle);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(downWindSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
