package seng302.team18.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhl25 on 1/05/17.
 */
public class Polar {

    private Map<Double, Double> polarUpAngle;
    private Map<Double, Double> polarDownAngle;
    private Map<Double, Double> polarUpSpeed;
    private Map<Double, Double> polarDownSpeed;


    public Polar(Map<Double, Double> polarUpAngle,
                 Map<Double, Double> polarDownAngle,
                 Map<Double, Double> polarUpSpeed,
                 Map<Double, Double> polarDownSpeed) {
        this.polarUpAngle = polarUpAngle;
        this.polarDownAngle = polarDownAngle;
        this.polarUpSpeed = polarUpSpeed;
        this.polarDownSpeed = polarDownSpeed;
    }
}
