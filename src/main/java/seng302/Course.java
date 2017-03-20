package seng302;

import java.util.ArrayList;


/**
 * A class which represents a course that is used in a race event.
 */
public class Course {

    private ArrayList<CompoundMark> compoundMarks;
    private ArrayList<Leg> legs;


    public Course(ArrayList<CompoundMark> marks) {

        this.compoundMarks = marks;
        legs = new ArrayList<>();
        for (int i = 0; i < marks.size() - 1; i++) {
            legs.add(new Leg(marks.get(i), marks.get(i + 1), i));
        }
    }


    /**
     * A getter for the CompoundMarks in the course
     *
     * @return the Arraylist of CompoundMarks
     */
    public ArrayList<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }


    public ArrayList<Leg> getLegs() {
        return legs;
    }


    public Leg getNextLeg(Leg leg) {
        if (leg.getLegNumber() + 1 >= legs.size()) {
            return leg;
        }
        return legs.get(leg.getLegNumber() + 1);
    }
}
