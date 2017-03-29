package seng302.model;

import seng302.raceutil.GPSCalculations;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent a Compound mark or gate comprising of one or two marks. Compound marks are drawn using
 * information retrieved from instances of this class
 */
public class CompoundMark {

    public static int GATE_SIZE = 2;
    public static int MARK_SIZE = 1;
    private String name;
    private List<Mark> marks;
    private List<Boat> passed;

    /**
     * A constructor for the CompoundMark class
     * @param name The name of the CompoundMark
     * @param marks The arraylist of Marks for the Compound Mark
     */
    public CompoundMark(String name, List<Mark> marks) {
        if (0 < marks.size() && marks.size() <= 2) {
            this.name = name;
            this.marks = marks;
            passed = new ArrayList<>();
        } else {
            System.err.println("A compound mark cannot have more than 2 marks");
        }
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


    /**
     * Returns the coordniate located at the midpoint of the two Marks of the CompoundMark
     * @return coordinate
     */
    public Coordinate getMidCoordinate() {
        if (marks.size() == GATE_SIZE) {
            return GPSCalculations.GPSMidpoint(marks.get(0).getCoordinates(), marks.get(1).getCoordinates());
        } else if (marks.size() == MARK_SIZE) {
            return marks.get(0).getCoordinates();
        } else {
            return null;
        }
    }


    public List<Boat> getPassed() {
        return passed;
    }


    public void addPassed(Boat passed) {
        this.passed.add(passed);
    }

    /**
     * An override of the equals method for use in testing
     * @param o The object to be checked against
     * @return The boolean value of the test
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompoundMark that = (CompoundMark) o;

        if (!name.equals(that.name)) return false;
        if (!marks.equals(that.marks)) return false;
        return passed.equals(that.passed);
    }

    /**
     * An override of the hashCode method to map the Marks
     * @return an integer that is used for the key
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + marks.hashCode();
        result = 31 * result + passed.hashCode();
        return result;
    }
}
