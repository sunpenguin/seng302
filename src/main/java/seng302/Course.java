package seng302;

import java.util.ArrayList;


/**
 * A class which represents a course that is used in a race event.
 */
public class Course {

    private ArrayList<CompoundMark> compoundMarks;
    private ArrayList<Leg> legs;
    private double windDirection;

    public Course(ArrayList<CompoundMark> marks, double windDirection) {
        this.compoundMarks = marks;
        this.windDirection = windDirection;
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

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public double getCourseDistance(){
        double distance = 0;
        Coordinate prev = getCompoundMarks().get(0).getMidCoordinate();
        Coordinate cur = getCompoundMarks().get(1).getMidCoordinate();
        for (int i = 2; i < getCompoundMarks().size(); i++){
            distance += GPSCalculations.GPSDistance(prev, cur);
            prev = cur;
            cur = getCompoundMarks().get(i).getMidCoordinate();
        }
        return distance;
    }
}

