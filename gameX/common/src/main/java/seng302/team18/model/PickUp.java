package seng302.team18.model;

/**
 * Created by dhl25 on 1/09/17.
 */
public class PickUp {

    private BodyMass bodyMass;
    private PowerUp powerUp;

    public PickUp() {

    }

    public Coordinate getLocation() {
        return bodyMass.getLocation();
    }


    public void setLocation(Coordinate location) {
        bodyMass.setLocation(location);
    }


    public BodyMass getBodyMass() {
        return bodyMass;
    }


    public void setBodyMass(BodyMass bodyMass) {
        this.bodyMass = bodyMass;
    }


    public void setPower(PowerUp powerUp) {
        this.powerUp = powerUp;
    }


    public PowerUp getPower() {
        return powerUp;
    }

}
