package seng302.team18.message;

import seng302.team18.model.Coordinate;

/**
 * Created by dhl25 on 3/09/17.
 */
public class PowerUpMessage implements MessageBody {
    private int id;
    private Coordinate location = new Coordinate(0, 0);
    private double radius; // metres
    private long timeout; // epoch millis
    private PowerType power;
    private double duration; // seconds

    public PowerUpMessage() {}


    public PowerUpMessage(int id, double latitude, double longitude, double radius, long timeout,
                          PowerType power, double duration) {
        this.id = id;
        this.location = new Coordinate(latitude, longitude);
        this.radius = radius;
        this.timeout = timeout;
        this.power = power;
        this.duration = duration;
    }


    @Override
    public int getType() {
        return AC35MessageType.POWER_UP.getCode();
    }


    public double getLatitude() {
        return location.getLatitude();
    }


    public void setLatitude(double latitude) {
        location.setLatitude(latitude);
    }


    public double getLongitude() {
        return location.getLongitude();
    }


    public void setLongitude(double longitude) {
        location.setLongitude(longitude);
    }


    public double getRadius() {
        return radius;
    }


    public void setRadius(double radius) {
        this.radius = radius;
    }


    public long getTimeout() {
        return timeout;
    }


    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }


    public PowerType getPower() {
        return power;
    }


    public void setPower(PowerType power) {
        this.power = power;
    }


    public double getDuration() {
        return duration;
    }


    public void setDuration(double duration) {
        this.duration = duration;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public Coordinate getLocation() {
        return location;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PowerUpMessage that = (PowerUpMessage) o;

        if (id != that.id) return false;
        if (Double.compare(that.radius, radius) != 0) return false;
        if (timeout != that.timeout) return false;
        if (Double.compare(that.duration, duration) != 0) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        return power == that.power;
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        temp = Double.doubleToLongBits(radius);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (timeout ^ (timeout >>> 32));
        result = 31 * result + (power != null ? power.hashCode() : 0);
        temp = Double.doubleToLongBits(duration);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public String toString() {
        return "PowerUpMessage{" +
                "id=" + id +
                ", location=" + location +
                ", radius=" + radius +
                ", timeout=" + timeout +
                ", power=" + power +
                ", duration=" + duration +
                '}';
    }
}
