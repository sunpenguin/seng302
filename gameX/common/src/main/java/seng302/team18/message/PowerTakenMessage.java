package seng302.team18.message;

/**
 * Created by dhl25 on 3/09/17.
 */
public class PowerTakenMessage implements MessageBody {

    private int boatId;
    private int powerId;
    private double duration; // seconds

    public PowerTakenMessage(int boatId, int powerId, double duration) {
        this.boatId = boatId;
        this.powerId = powerId;
        this.duration = duration;
    }


    public PowerTakenMessage() {}


    @Override
    public int getType() {
        return AC35MessageType.POWER_TAKEN.getCode();
    }


    public int getPowerId() {

        return powerId;
    }


    public void setPowerId(int powerId) {
        this.powerId = powerId;
    }


    public int getBoatId() {
        return boatId;
    }


    public void setBoatId(int boatId) {
        this.boatId = boatId;
    }


    public double getDuration() {
        return duration;
    }


    public void setDuration(double duration) {
        this.duration = duration;
    }


}
