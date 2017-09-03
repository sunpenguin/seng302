package seng302.team18.message;

/**
 * Created by dhl25 on 3/09/17.
 */
public class PowerUpMessage implements MessageBody {
    private int id;
    private double latitude;
    private double longitude;
    private double radius; // metres
    private long timeout; // epoch millis
    private PowerType power;
    private double duration; // seconds

    public PowerUpMessage() {}


    public PowerUpMessage(int id, double latitude, double longitude, double radius, long timeout,
                          PowerType power, double duration) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
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
        return latitude;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
}
