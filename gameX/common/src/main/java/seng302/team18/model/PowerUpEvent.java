package seng302.team18.model;

/**
 * Created by dhl25 on 4/09/17.
 */
public class PowerUpEvent {

    private int boatId;
    private PickUp pickUp;

    public PowerUpEvent(int boatId, PickUp pickUp) {
        this.boatId = boatId;
        this.pickUp = pickUp;
    }


    public int getBoatId() {
        return boatId;
    }


    public PickUp getPickUp() {
        return pickUp;
    }


    public int getPowerId() { return pickUp.getId(); }


    public double getPowerDuration() { return pickUp.getPowerDuration(); }

}
