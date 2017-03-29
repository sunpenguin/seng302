package seng302;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csl62 on 15/03/17.
 */
public class CompoundMark {

    public static int GATE_SIZE = 2;
    public static int MARK_SIZE = 1;
    private String name;
    private List<Mark> marks;
    private List<Boat> passed;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompoundMark that = (CompoundMark) o;

        if (!name.equals(that.name)) return false;
        if (!marks.equals(that.marks)) return false;
        return passed.equals(that.passed);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + marks.hashCode();
        result = 31 * result + passed.hashCode();
        return result;
    }
}
