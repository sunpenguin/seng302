package seng302.team18.racemodel.model;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.model.MarkRounding;
import seng302.team18.util.GPSCalculator;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Created by hqi19 on 2/09/17.
 */
public class CourseBuilderChallenge extends AbstractCourseBuilder {

    private List<Coordinate> boundaryMarks = new ArrayList<>();
    private List<CompoundMark> compoundMarks = new ArrayList<>();
    private List<MarkRounding> markRoundings = new ArrayList<>();


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
    protected List<CompoundMark> buildCompoundMarks() {
        final double MARK_WIDTH = 65;
        final double ESTIMATED_NUM_GATES = 10;
        Random random = new Random();

        Coordinate previous = boundaryMarks.get(0);
        GPSCalculator calculator = new GPSCalculator();

        double leftBoundary = boundaryMarks.get(0).getLongitude();
        double rightBoundary = boundaryMarks.get(1).getLongitude();
        double topBoundary = boundaryMarks.get(2).getLatitude();

        for (Integer i = 0; true; i++) {
            Coordinate left;
            Coordinate right;

            while (true) {
                double bearing = i == 0? 45 : random.nextDouble() * (450 - 270) + 270;
                double distance = i == 0? 130 : random.nextDouble() * (150 - (1500 / (ESTIMATED_NUM_GATES))) + (1500 / (ESTIMATED_NUM_GATES));

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

            Mark mark1 = new Mark(i+1, left);
            mark1.setHullNumber(i.toString());
            mark1.setStoweName(i.toString());
            mark1.setShortName(i.toString());
            mark1.setBoatName(i.toString());

            Mark mark2 = new Mark(i+100, right);
            mark2.setHullNumber(i.toString());
            mark2.setStoweName(i.toString());
            mark2.setShortName(i.toString());
            mark2.setBoatName(i.toString());

            CompoundMark gate = new CompoundMark(i.toString(), Arrays.asList(mark1, mark2), i+1000);
            compoundMarks.add(gate);

            previous = left;
        }

        return compoundMarks;
    }


    /**
     * Create boundary marks for the challenge mode.
     *
     * The boundary is a rectangle area with narrow width and long height.
     *
     * @return a list boundary marks
     */
    @Override
    protected List<Coordinate> getBoundaryMarks() {
        GPSCalculator calculator = new GPSCalculator();

        Coordinate bottomLeft = new Coordinate(38.21748,-106.52344);
        Coordinate bottomRight = calculator.toCoordinate(bottomLeft, 90, 300);
        Coordinate topRight = calculator.toCoordinate(bottomRight, 0, 1500);
        Coordinate topLeft = calculator.toCoordinate(topRight, 270, 300);

        boundaryMarks.add(bottomLeft);
        boundaryMarks.add(bottomRight);
        boundaryMarks.add(topRight);
        boundaryMarks.add(topLeft);

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
