package seng302.team18.model;

import seng302.team18.message.PowerType;


public abstract class PowerUp {

    private double timeSinceActivation = 0d;
    private double duration = Double.POSITIVE_INFINITY;


    public PowerUp() {}


    /**
     * sets how long the power up will last after activation / first use.
     * @param duration in milliseconds
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }


    public double getDuration() {
        return duration;
    }


    /**
     * Updates the power up and increments time since activation.
     * time in seconds.
     */
    public void update(Boat boat, double time) {
        timeSinceActivation += time;
    }


    /**
     * returns if the power up is terminated.
     *
     * @return the power up has terminated
     */
    public boolean isTerminated() {
        return timeSinceActivation > duration;
    }


    public abstract PowerType getType();


    public abstract PowerUp clone();

}
