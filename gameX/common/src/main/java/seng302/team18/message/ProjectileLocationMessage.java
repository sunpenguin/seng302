package seng302.team18.message;

import seng302.team18.model.Coordinate;

/**
 * Created by csl62 on 7/09/17.
 */
public class ProjectileLocationMessage implements MessageBody {

    private int id;
    private double heading;
    private Coordinate location;
    private double speed;

    public ProjectileLocationMessage(int id, double latitude, double longitude, double heading, double speed) {
        this.location = new Coordinate(latitude, longitude);
        this.id = id;
        this.heading = heading;
        this.speed = speed;
    }

    @Override
    public int getType() {
        return AC35MessageType.PROJECTILE_LOCATION.getCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
