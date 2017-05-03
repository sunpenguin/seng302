package seng302.team18.model;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A class which represents a course that is used in a race event.
 */
public class Course {

    private List<CompoundMark> compoundMarks;
    private List<Leg> legs;
    private double windDirection;
    private double windSpeed;
    private List<BoundaryMark> boundaries;
    //private List<MarkRounding> markRoundings;
    private Coordinate centralCoordinate;
    private ZoneId timeZone;
    private List<MarkRounding> markRoundings;

    public Course(Collection<CompoundMark> marks, Collection<BoundaryMark> boundaries, double windDirection, ZoneId timeZone, List<MarkRounding> markRoundings) {
        this.compoundMarks = new ArrayList<>(marks);
        this.windDirection = windDirection;
        this.boundaries = new ArrayList<>(boundaries);
        this.timeZone = timeZone;
        this.markRoundings = markRoundings;
        setupLegs();
        centralCoordinate = new Coordinate(0d, 0d);
    }

    public Course() {
        compoundMarks = new ArrayList<>();
        boundaries = new ArrayList<>();
        //markRoundings = new ArrayList<>();
        timeZone = ZoneId.systemDefault();
        windDirection = 0d;
        windSpeed = 0d;
        centralCoordinate = new Coordinate(0d, 0d);
    }

    /**
     * A getter for the CompoundMarks in the course
     *
     * @return the list of CompoundMarks
     */
    public List<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }

    public void setCompoundMarks(Collection<CompoundMark> compoundMarks) {
        this.compoundMarks.clear();
        this.compoundMarks.addAll(compoundMarks);
    }


    public double getWindDirection() {
        return windDirection;
    }


    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }


    public List<Mark> getMarks(){
        List<Mark> marks = new ArrayList<>();
        for (CompoundMark cMark : compoundMarks) {
            marks.addAll(cMark.getMarks());
        }
        return marks;
    }

    public List<BoundaryMark> getBoundaries() {
        return new ArrayList<>(boundaries);
    }

    public void setBoundaries(Collection<BoundaryMark> boundaries) {
        this.boundaries.clear();
        this.boundaries.addAll(boundaries);
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

//    public void setMarkRoundings(List<MarkRounding> markRoundings) {
//        this.markRoundings = markRoundings;
//    }

//    public List<MarkRounding> getMarkRoundings() {return markRoundings;}

    public void setCentralCoordinate(Coordinate centralCoordinate) {
        if (this.centralCoordinate.getLatitude() == 0d && this.centralCoordinate.getLongitude() == 0d) {
            this.centralCoordinate = centralCoordinate;
        }
    }

    public Coordinate getCentralCoordinate() {
        return centralCoordinate;
    }

    public List<Leg> getLegs() {
        return legs;
    }


    public Leg getNextLeg(Leg leg) {
        if (leg.getLegNumber() == legs.size()) {
            return leg;
        }
        return legs.get(leg.getLegNumber());
    }

    private void setupLegs(){
        legs = new ArrayList<>();
         for (int i = 0; i < markRoundings.size() - 1; i++) {
             CompoundMark dep = markRoundings.get(i).getCompoundMark();
             CompoundMark dest = markRoundings.get(i + 1).getCompoundMark();
             legs.add(new Leg(dep, dest, i + 1));
         }
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
}

