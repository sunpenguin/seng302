package seng302.team18.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to hold polar patterns of boats
 */
public class Polar {

    private double windSpeed;
    private Map<Double, Double> mapSpeedAtAngles;
    private double upWindAngle;
    private double upWindSpeed;
    private double downWindAngle;
    private double downWindSpeed;


    public Polar(double windSpeed, double upWindAngle, double upWindSpeed, double downWindAngle, double downWindSpeed){
        this.windSpeed = windSpeed;
        this.upWindAngle = upWindAngle;
        this.upWindSpeed = upWindSpeed;
        this.downWindAngle = downWindAngle;
        this.downWindSpeed = downWindSpeed;
        mapSpeedAtAngles = new HashMap<>();
    }

    /**
     * Method to add a value and key to the hashmap of angles and speeds.
     * @param trueWindAngle True wind angle for boat
     * @param boatSpeed Speed boat travel at the angle
     */
    public void addToMap(double trueWindAngle, double boatSpeed){
        mapSpeedAtAngles.put(trueWindAngle,boatSpeed);
    }

}
