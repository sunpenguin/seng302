package seng302.team18.racemodel.model;

import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.model.MarkRounding;
import seng302.team18.util.GPSCalculator;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The course to be used for bumper boats mode.
 *
 * NOTE: Course currently looks fine with the PixelMapper. Will need adjusting once distortions fixed.
 */
public class CourseBuilderBumper extends AbstractCourseBuilder {

    private double count = 0;

    @Override
    protected List<MarkRounding> getMarkRoundings() {
        List<MarkRounding> markRoundings = new ArrayList<>();

        markRoundings.add(new MarkRounding(1, getCompoundMarks().get(0), MarkRounding.Direction.PS, 3));
        markRoundings.add(new MarkRounding(2, getCompoundMarks().get(1), MarkRounding.Direction.PS, 3));

        return markRoundings;
    }


    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        // Note that these marks should be able to be removed during the task of removing unnecessary things.
        // Start Line
        Mark start1 = new Mark(231, new Coordinate(5.00100, 4.00125));
        start1.setHullNumber("LC21");
        start1.setStoweName("PRO");
        start1.setShortName("S1");
        start1.setBoatName("Start Line 1");

        Mark start2 = new Mark(232, new Coordinate(5.00200, 4.00125));
        start2.setHullNumber("LC22");
        start2.setStoweName("PIN");
        start2.setShortName("S2");
        start2.setBoatName("Start Line 2");

        CompoundMark startGate = new CompoundMark("Start Line", Arrays.asList(start1, start2), 11);

        // Finish Line
        Mark finish1 = new Mark(238, new Coordinate(5.00100, 3.99975));
        finish1.setHullNumber("LC28");
        finish1.setStoweName("PRO");
        finish1.setShortName("F1");
        finish1.setBoatName("Finish Line 1");

        Mark finish2 = new Mark(239, new Coordinate(5.00200, 3.99975));
        finish2.setHullNumber("LC29");
        finish2.setStoweName("PIN");
        finish2.setShortName("F2");
        finish2.setBoatName("Finish Line 2");

        CompoundMark finishGate = new CompoundMark("Finish Line", Arrays.asList(finish1, finish2), 12);

        return Arrays.asList(startGate, finishGate);
    }


    @Override
    protected List<Coordinate> getBoundaryMarks() {
        // The course can shrink, just need to update like spyro type
        List<Coordinate> boundaryMarks = new ArrayList<>();
        GPSCalculator calculator = new GPSCalculator();

        Coordinate origin = new Coordinate(5.00150, 4.0005);
        double distance = 200; // - count);

        Coordinate top = calculator.toCoordinate(origin, 0, distance);
        Coordinate topRight = calculator.toCoordinate(origin, 45, distance);
        Coordinate right = calculator.toCoordinate(origin, 90, distance);
        Coordinate bottomRight = calculator.toCoordinate(origin, 135, distance);
        Coordinate bottom = calculator.toCoordinate(origin, 180, distance);
        Coordinate bottomLeft = calculator.toCoordinate(origin, 225, distance);
        Coordinate left = calculator.toCoordinate(origin, 270, distance);
        Coordinate topLeft = calculator.toCoordinate(origin, 315, distance);

        boundaryMarks.add(top);
        boundaryMarks.add(topRight);
        boundaryMarks.add(right);
        boundaryMarks.add(bottomRight);
        boundaryMarks.add(bottom);
        boundaryMarks.add(bottomLeft);
        boundaryMarks.add(left);
        boundaryMarks.add(topLeft);

//        count += 2;

        return boundaryMarks;
    }


    @Override
    protected double getWindDirection() {
        return 180;
    }


    @Override
    protected double getWindSpeed() {
        return 30;
    }


    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(12));
    }
}
