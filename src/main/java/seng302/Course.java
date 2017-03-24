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
        Coordinate dep = getLegs().get(0).getDeparture().getMidCoordinate();
        Coordinate dest = getLegs().get(0).getDestination().getMidCoordinate();
        for (int i = 1; i < getCompoundMarks().size()-1; i++){
            distance += GPSCalculations.GPSDistance(dep, dest);
            dep = getLegs().get(i).getDeparture().getMidCoordinate();
            dest = getLegs().get(i).getDestination().getMidCoordinate();
        }
        return distance;
    }
}

