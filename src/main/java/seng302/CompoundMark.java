package seng302;

import java.util.ArrayList;

/**
 * Created by csl62 on 15/03/17.
 */
public class CompoundMark {

    private String name;
    private ArrayList<Mark> marks;
    public static int GATE_SIZE = 2;
    public static int MARK_SIZE = 1;
    private ArrayList<Boat> passed = new ArrayList<>();

    public CompoundMark(String name, ArrayList<Mark> marks) {
        this.name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Mark> getMarks() {
        return marks;
    }

    public void setMarks(ArrayList<Mark> marks) {
        this.marks = marks;
    }

    public Coordinate getMidCoordinate() {
        if (marks.size() == GATE_SIZE) {
            return GPSCalculations.GPSMidpoint(marks.get(0).getMarkCoordinates(), marks.get(1).getMarkCoordinates());
        } else if (marks.size() == MARK_SIZE) {
            return marks.get(0).getMarkCoordinates();
        } else {
            return null;
        }

    }
}
