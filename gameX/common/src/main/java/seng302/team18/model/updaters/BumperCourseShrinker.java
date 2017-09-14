package seng302.team18.model.updaters;

import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.RaceStatus;
import seng302.team18.util.GPSCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Reduces the size of a bumper boat course as time passes
 */
public class BumperCourseShrinker implements Updater {

    private final double interval;
    private double timeSinceUpdate = 0;
    private double totalTime = 0;
    private List<Coordinate> boundaryMarks = new ArrayList<>();
    private GPSCalculator calculator = new GPSCalculator();
    private Coordinate origin;
    private double boundaryDistance = 0.5;
    private double speed;


    /**
     * Constructs a new BumperCourseShrinker object
     *
     * @param origin          the centre of the course
     * @param initialDistance the initial distance between any given boundary mark and the origin
     * @param interval        the minimum interval between updates (ms)
     * @param speed           the velocity of the boundary mark towards the origin (m / ms)
     */
    public BumperCourseShrinker(Coordinate origin, double initialDistance, double interval, double speed) {
        this.interval = interval;
        this.origin = origin;
        this.speed = speed;
        boundaryDistance = initialDistance;
    }


    @Override
    public void update(Race race, double time) {
        if (!RaceStatus.STARTED.equals(race.getStatus())) {
            return;
        }
        timeSinceUpdate += time;
        totalTime += time;
        if (timeSinceUpdate >= interval) {
            boundaryDistance -= totalTime * speed;
            repositionBoundaries(race.getCourse());
            timeSinceUpdate = 0;
        }
    }


    /**
     * Position the boundaries of the course.
     * Each boundary mark is positioned boundaryDistance meters from the origin.
     *
     * @param course course to position boundaries for.
     */
    private void repositionBoundaries(Course course) {
        boundaryMarks.clear();

        Coordinate top = calculator.toCoordinate(origin, 0, boundaryDistance);
        Coordinate topRight = calculator.toCoordinate(origin, 45, boundaryDistance);
        Coordinate right = calculator.toCoordinate(origin, 90, boundaryDistance);
        Coordinate bottomRight = calculator.toCoordinate(origin, 135, boundaryDistance);
        Coordinate bottom = calculator.toCoordinate(origin, 180, boundaryDistance);
        Coordinate bottomLeft = calculator.toCoordinate(origin, 225, boundaryDistance);
        Coordinate left = calculator.toCoordinate(origin, 270, boundaryDistance);
        Coordinate topLeft = calculator.toCoordinate(origin, 315, boundaryDistance);

        boundaryMarks.add(top);
        boundaryMarks.add(topRight);
        boundaryMarks.add(right);
        boundaryMarks.add(bottomRight);
        boundaryMarks.add(bottom);
        boundaryMarks.add(bottomLeft);
        boundaryMarks.add(left);
        boundaryMarks.add(topLeft);

        course.setCourseLimits(boundaryMarks);
    }
}
