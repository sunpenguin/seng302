package seng302.team18.message;

public class PowerTakenMessage implements MessageBody {

    private int boatId;
    private int powerId;
    private double duration; // seconds

    public PowerTakenMessage(int boatId, int powerId, double duration) {
        this.boatId = boatId;
        this.powerId = powerId;
        this.duration = duration;
    }


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


    @Override
    public String toString() {
        return "PowerTakenMessage{" +
                "boatId=" + boatId +
                ", powerId=" + powerId +
                ", duration=" + duration +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PowerTakenMessage that = (PowerTakenMessage) o;

        if (boatId != that.boatId) return false;
        if (powerId != that.powerId) return false;
        return Double.compare(that.duration, duration) == 0;
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        result = boatId;
        result = 31 * result + powerId;
        temp = Double.doubleToLongBits(duration);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
