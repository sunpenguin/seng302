package seng302.team18.model;

/**
 * BodyMass class.
 */
public class BodyMass {

    private double weight;
    private Coordinate location;
    private double radius;

    public BodyMass() {}


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }


    @Override
    public String toString() {
        return "BodyMass{" +
                "weight=" + weight +
                ", location=" + location +
                ", radius=" + radius +
                '}';
    }
}
