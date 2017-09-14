package seng302.team18.model;

/**
 * A class to represent and hold information for a projectile in the race
 */
public abstract class Projectile {

    private int id;
    private BodyMass bodyMass  = new BodyMass();
    private double heading;
    private double speed;

    public Projectile(int id, double radius, double weight, Coordinate location, double heading, double speed) {
        this.id = id;
        this.bodyMass = new BodyMass(location,radius,weight);
        this.heading = heading;
        this.speed = speed;
    }

    public abstract void update(double time);



    public int getId() {
        return id;
    }

    public void setId(int source_id) {
        this.id = source_id;
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
