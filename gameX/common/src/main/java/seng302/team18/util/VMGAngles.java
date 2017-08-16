package seng302.team18.util;

/**
 * Created by sbe67 on 15/08/17.
 */
public enum VMGAngles {
    LEFT(270),
    RIGHT(90),
    DEAD_ZONE(10);

    private double value;

    VMGAngles (double value){
        this.value = value;
    }

    public double getValue(){
        return value;
    }

}
