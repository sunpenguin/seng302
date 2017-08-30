package seng302.team18.model;

/**
 * Created by dhl25 on 29/08/17.
 */
public abstract class PowerUp {


    private Coordinate location;
    private double timeSinceActivation = 0d;
    private double timeOut = Double.POSITIVE_INFINITY;
    private BodyMass bodyMass;


    public PowerUp() {
        bodyMass = new BodyMass();
        bodyMass.setWeight(0);
    }


    /**
     * sets how long the power up will last after activation / first use.
     * @param timeOut
     */
    public void setTimeOut(double timeOut) {
        this.timeOut = timeOut;
    }


    /**
     * Updates the power up and increments time since activation.
     * time in seconds.
     */
    public void update(double time) {
        timeSinceActivation += time;
    }


    /**
     * returns if the power up is terminated.
     *
     * @return the power up has terminated
     */
    public boolean isTerminated() {
        return timeSinceActivation > timeOut;
    }


    public Coordinate getLocation() {
        return location;
    }


    public void setLocation(Coordinate location) {
        this.location = location;
    }


    public BodyMass getBodyMass() {
        return bodyMass;
    }

    public void setBodyMass(BodyMass bodyMass) {
        this.bodyMass = bodyMass;
    }
}
