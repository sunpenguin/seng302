package seng302.team18.racemodel.model;

import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.MarkRounding;
import seng302.team18.util.GPSCalculator;

import java.time.ZoneId;
import java.util.List;

/**
 * Base implementation class for course builders.
 * <p>
 * This class provides a skeleton for classes that build a course.
 */
public abstract class AbstractCourseBuilder {

    /**
     * The compound marks in the course
     */
    private List<CompoundMark> compoundMarks = buildCompoundMarks();


    /**
     * Builds a course
     *
     * @return the constructed course
     */
    public Course buildCourse() {
        Course course = new Course(compoundMarks, getBoundaryMarks(), getMarkRoundings());

        GPSCalculator gpsCalculator = new GPSCalculator();
        List<Coordinate> extremes = gpsCalculator.findMinMaxPoints(course);
        Coordinate center = gpsCalculator.midPoint(extremes.get(0), extremes.get(1));
        course.setCentralCoordinate(center);
        course.setWindDirection(getWindDirection());
        course.setWindSpeed(getWindSpeed());
        course.setTimeZone(getTimeZone());
        return course;
    }


    /**
     * @return the compound marks to initialise the course with
     */
    protected List<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }


    /**
     * @return the mark roundings to initialise the course  with
     */
    protected abstract List<MarkRounding> getMarkRoundings();


    /**
     * @return the compound marks to initialise the course with
     */
    protected abstract List<CompoundMark> buildCompoundMarks();


    /**
     * @return the boundary marks to initialise the course with
     */
    protected abstract List<Coordinate> getBoundaryMarks();


    /**
     * @return the wind direction to initialise the course with
     */
    protected abstract double getWindDirection();


    /**
     * @return the wind speed to initialise the course with
     */
    protected abstract double getWindSpeed();


    /**
     * @return the time zone to initialize the course withL
     */
    protected abstract ZoneId getTimeZone();
}
