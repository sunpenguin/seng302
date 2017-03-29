package seng302.util;

/**
 * Class to hold x, y cartesian coordinates
 */
public class XYPair {
    private double x;
    private double y;

    /**
     * Constructor for XYPair class.
     * Used to hold x, y catersian coordinates
     * @param x x coordinate
     * @param y y coordinate
     */
    public XYPair(double x, double y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Getter for the x coordinate
     * @return x coordinate
     */
    public double getX() {
        return this.x;
    }


    /**
     * Getter for the y coordinate
     * @return y coordinate
     */
    public double getY() {
        return this.y;
    }
}
