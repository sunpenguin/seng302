package seng302.team18.racemodel.builder.course;

import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.model.MarkRounding;
import seng302.team18.util.GPSCalculator;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Class for building a course for Challenge Mode.
 * Positions of gates are randomly generated within certain limits.
 */
public class CourseBuilderChallenge extends AbstractCourseBuilder {

    private List<Coordinate> boundaryMarks;
    private List<CompoundMark> compoundMarks = new ArrayList<>();
    private List<MarkRounding> markRoundings = new ArrayList<>();
    private Coordinate origin = new Coordinate(38.21748,-106.52344);

    private int count = 1;
    private double BOUNDARY_SPEED = 0.85;
    private final int COURSE_LENGTH = 1500;
    private final int COURSE_WIDTH = 300;


    @Override
    protected List<MarkRounding> getMarkRoundings() {
        for (int i = 0; i < compoundMarks.size(); i ++) {
            markRoundings.add(new MarkRounding(i + 1, compoundMarks.get(i), MarkRounding.Direction.PS, 3));
        }

        return markRoundings;
    }


    /**
     * Randomly generates gates for the challenge mode.
     *
     * If the generated gate is outside the boundary, it will get regenerated.
     *
     * @return a list of compoundmarks
     */
    @Override
    public List<CompoundMark> buildCompoundMarks() {
        compoundMarks.clear();
        final double MARK_WIDTH = 65;
        Random random = new Random();

        Coordinate previous = boundaryMarks.get(0);
        GPSCalculator calculator = new GPSCalculator();

        double leftBoundary = boundaryMarks.get(0).getLongitude();
        double rightBoundary = boundaryMarks.get(1).getLongitude();
        double topBoundary = boundaryMarks.get(2).getLatitude();

        final int MIN_ANGLE = 295;
        final int ANGLE_RANGE = 145;
        final int START_ANGLE = 45;
        final int START_DISTANCE = 130;
        final int MIN_DISTANCE = 100;
        final int DISTANCE_RANGE = 50;

        for (Integer i = 0; true; i++) {
            Coordinate left;
            Coordinate right;

            while (true) {
                double bearing = i == 0? START_ANGLE : random.nextDouble() * ANGLE_RANGE + MIN_ANGLE;
                double distance = i == 0? START_DISTANCE : random.nextDouble() * DISTANCE_RANGE + MIN_DISTANCE;

                left = calculator.toCoordinate(previous, bearing, distance);
                right = calculator.toCoordinate(left, 90, MARK_WIDTH);

                if (left.getLongitude() < leftBoundary || right.getLongitude() > rightBoundary) {
                    continue;
                } else {
                    break;
                }
            }

            if (left.getLatitude() > topBoundary) {
                break;
            }

            Mark mark1 = new Mark(i+500, left);
            mark1.setHullNumber(i.toString());
            mark1.setStoweName(i.toString());
            mark1.setShortName(i.toString());
            mark1.setBoatName(i.toString());

            Mark mark2 = new Mark(i+600, right);
            mark2.setHullNumber(i.toString());
            mark2.setStoweName(i.toString());
            mark2.setShortName(i.toString());
            mark2.setBoatName(i.toString());

            CompoundMark gate = new CompoundMark(i.toString(), Arrays.asList(mark1, mark2), i+700);
            compoundMarks.add(gate);

            previous = left;
        }

        return compoundMarks;
    }


    /**
     * Create boundary marks for the challenge mode.
     *
     * The boundary is a rectangular area with narrow width and long height.
     * Each time this method is called, the bottom boundary will shift closer to the top boundary, so that the player
     * has to outrun it to stay in the game.
     *
     * @return a list boundary marks
     */
    @Override
    public List<Coordinate> getBoundaryMarks() {
        boundaryMarks = new ArrayList<>();
        GPSCalculator calculator = new GPSCalculator();

        Coordinate bottomLeft = calculator.toCoordinate(origin,0, count * BOUNDARY_SPEED);
        Coordinate bottomRight = calculator.toCoordinate(bottomLeft, 90, COURSE_WIDTH);
        Coordinate topRight = calculator.toCoordinate(bottomRight, 0, COURSE_LENGTH - (count * BOUNDARY_SPEED));
        Coordinate topLeft = calculator.toCoordinate(topRight, 270, COURSE_WIDTH);

        boundaryMarks.add(bottomLeft);
        boundaryMarks.add(bottomRight);
        boundaryMarks.add(topRight);
        boundaryMarks.add(topLeft);

        count++;

        return boundaryMarks;
    }


    @Override
    protected double getWindDirection() {
        return 180;
    }


    @Override
    protected double getWindSpeed() {
        return 25;
    }


    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(5));
    }
}
