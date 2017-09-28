package seng302.team18.model.updaters;

import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.RaceStatus;
import seng302.team18.util.GPSCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseShrinker to shrink boundaries in the challenge mode.
 */
public class ChallengeCourseShrinker implements Updater {

    private final double interval;
    private double timeSinceUpdate = 0;
    private double totalTime = 0;
    private List<Coordinate> boundaryMarks = new ArrayList<>();
    private GPSCalculator calculator = new GPSCalculator();
    private Coordinate origin;
    private double boundaryDistance = 0.5;
    private double speed;
    private double multiplier;
    private static final int COURSE_LENGTH = 1500;
    private static final int COURSE_WIDTH = 300;



    /**
     * Constructor for the ChallengeCourseShrinker.
     *
     * @param origin of the course.
     * @param interval the boundaries update.
     * @param multiplier how much speed is multiplied by.
     * @param speed initial speed of the boundary.
     */
    public ChallengeCourseShrinker(Coordinate origin, double interval, double multiplier, double speed) {
        this.origin = origin;
        this.interval = interval;
        this.multiplier = multiplier;
        this.speed = speed;
    }


    /**
     * Update the distance by which the boundary will move if time since the last update exceeds interval given to the
     * constructor.
     *
     * @param race to update
     * @param time to update by (in milliseconds)
     */
    @Override
    public void update(Race race, double time) {
        if (!RaceStatus.STARTED.equals(race.getStatus())) {
            return;
        }
        timeSinceUpdate += time;
        totalTime += time;
        if (timeSinceUpdate >= interval) {
            speed = totalTime * multiplier;
            boundaryDistance = totalTime * speed;
            repositionBoundaries(race.getCourse());
            timeSinceUpdate = 0;
        }
    }


    /**
     * Position the boundaries of the course.
     * Bottom boundary is updated to be closer to the top boundary according to the current boundaryDistance.
     *
     * @param course course to position boundaries for.
     */
    private void repositionBoundaries(Course course) {
        boundaryMarks.clear();

        Coordinate bottomLeft = calculator.toCoordinate(origin,0, boundaryDistance);
        Coordinate bottomRight = calculator.toCoordinate(bottomLeft, 90, COURSE_WIDTH);
        Coordinate topRight = calculator.toCoordinate(bottomRight, 0, COURSE_LENGTH - boundaryDistance);
        Coordinate topLeft = calculator.toCoordinate(topRight, 270, COURSE_WIDTH);

        boundaryMarks.add(bottomLeft);
        boundaryMarks.add(bottomRight);
        boundaryMarks.add(topRight);
        boundaryMarks.add(topLeft);

        course.setCourseLimits(boundaryMarks);
    }
}
