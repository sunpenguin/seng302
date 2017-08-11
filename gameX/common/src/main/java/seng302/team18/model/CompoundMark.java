package seng302.team18.model;

import seng302.team18.util.GPSCalculations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class which stores information about a compound mark.
 */
public class CompoundMark implements GeographicLocation {

    public final static int GATE_SIZE = 2;
    public final static int MARK_SIZE = 1;
    private String name;
    private Integer id;
    private List<Mark> marks;
    private List<Boat> passed;
    private double passAngle;

    public CompoundMark(String name, Collection<Mark> marks, int id) {
        this.name = name;
        this.marks = new ArrayList<>(marks);
        this.id = id;
        passed = new ArrayList<>();
    }

    /**
     * Retrieves the central coordinates of the compound mark
     *
     * @return the central coordinate
     */
    public Coordinate getCoordinate() {
        if (marks.size() == GATE_SIZE) {
            GPSCalculations gps = new GPSCalculations();
            return gps.midPoint(marks.get(0).getCoordinate(), marks.get(1).getCoordinate());
        } else if (marks.size() == MARK_SIZE) {
            return marks.get(0).getCoordinate();
        } else {
            return null;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompoundMark that = (CompoundMark) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (marks != null ? !marks.equals(that.marks) : that.marks != null) return false;
        return passed != null ? passed.equals(that.passed) : that.passed == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (marks != null ? marks.hashCode() : 0);
        result = 31 * result + (passed != null ? passed.hashCode() : 0);
        return result;
    }

    public Integer getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public List<Mark> getMarks() {
        return marks;
    }

    public double getPassAngle() {
        return passAngle;
    }

    public void setPassAngle(double passAngle) {
        this.passAngle = passAngle;
    }



}
