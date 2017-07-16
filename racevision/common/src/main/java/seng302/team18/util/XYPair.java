package seng302.team18.util;

/**
 * Class to hold x, y cartesian coordinates
 */
public class XYPair {
    private double x;
    private double y;

    /**
     * Constructor for XYPair class.
     * Used to hold x, y catersian coordinates
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public XYPair(double x, double y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Getter for the x coordinate
     *
     * @return x coordinate
     */
    public double getX() {
        return this.x;
    }


    /**
     * Getter for the y coordinate
     *
     * @return y coordinate
     */
    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XYPair xyPair = (XYPair) o;

        if (Double.compare(xyPair.x, x) != 0) return false;
        return Double.compare(xyPair.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
