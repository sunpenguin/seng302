package seng302.team18.model;

/**
 * Created by csl62 on 7/09/17.
 */
public abstract class Projectile {

    private int id;
    private Coordinate location;
    private double heading;
    private double speed;

    public Projectile(int id, Coordinate location, double heading, double speed) {
        this.id = id;
        this.location = location;
        this.heading = heading;
        this.speed = speed;
    }

    public abstract void update();



    public int getId() {
        return id;
    }

    public void setId(int source_id) {
        this.id = source_id;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
