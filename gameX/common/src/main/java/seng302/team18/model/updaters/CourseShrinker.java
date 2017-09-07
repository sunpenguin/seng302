package seng302.team18.model.updaters;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Created by dhl25 on 7/09/17.
 */
public class CourseShrinker implements Updater {

    private final double interval;
    private double accumulatedTime = 0;
    private Coordinate topLeft;
    private Coordinate topRight;
    private Coordinate bottomLeft;
    private Coordinate bottomRight;

    /**
     * Constructor for the CourseShrinker.
     *
     * @param topLeft corner of the course.
     * @param topRight corner of the course.
     * @param bottomLeft corner of the course.
     * @param bottomRight corner of the course.
     * @param interval the boundaries update.
     */
    public CourseShrinker(Coordinate topLeft, Coordinate topRight,
                          Coordinate bottomLeft, Coordinate bottomRight,
                          double interval) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.interval = interval;
    }


    @Override
    public void update(Race race, double time) {
        if (!RaceStatus.STARTED.equals(race.getStatus())) {
            return;
        }
        accumulatedTime += time;
        if (accumulatedTime >= interval) {
            accumulatedTime = 0;
            shrinkBoundaries(race.getCourse());
        }
    }


    private void shrinkBoundaries(Course course) {

    }


}
