package seng302.team18.test_mock.model;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculations;

import java.time.ZoneId;
import java.util.List;

/**
 * Created by afj19 on 10/07/17.
 */
public abstract class BaseCourseBuilder {

    private List<CompoundMark> compoundMarks = buildCompoundMarks();


    public Course buildCourse() {
        Course course = new Course(compoundMarks, getBoundaryMarks(), getWindDirection(), getWindSpeed(), getTimeZone(), getMarkRoundings());

        GPSCalculations gpsCalculations = new GPSCalculations();
        List<Coordinate> extremes = gpsCalculations.findMinMaxPoints(course);
        Coordinate center = gpsCalculations.midPoint(extremes.get(0), extremes.get(1));
        course.setCentralCoordinate(center);

        return course;
    }


    protected List<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }


    protected abstract List<MarkRounding> getMarkRoundings();

    protected abstract List<CompoundMark> buildCompoundMarks();

    protected abstract List<BoundaryMark> getBoundaryMarks();

    protected abstract double getWindDirection();

    protected abstract double getWindSpeed();

    protected abstract ZoneId getTimeZone();
}
