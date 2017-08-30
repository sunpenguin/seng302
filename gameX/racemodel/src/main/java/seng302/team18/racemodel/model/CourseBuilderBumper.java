package seng302.team18.racemodel.model;

import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.model.MarkRounding;

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


    @Override
    protected List<MarkRounding> getMarkRoundings() {
        List<MarkRounding> markRoundings = new ArrayList<>();

        markRoundings.add(new MarkRounding(1, getCompoundMarks().get(0), MarkRounding.Direction.PS, 3));
        markRoundings.add(new MarkRounding(2, getCompoundMarks().get(1), MarkRounding.Direction.PS, 3));

        return markRoundings;
    }


    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        // Start Line
        Mark start1 = new Mark(231, new Coordinate(5.00125, -3.99850));
        start1.setHullNumber("LC21");
        start1.setStoweName("PRO");
        start1.setShortName("S1");
        start1.setBoatName("Start Line 1");

        Mark start2 = new Mark(232, new Coordinate(5.00175, -3.99850));
        start2.setHullNumber("LC22");
        start2.setStoweName("PIN");
        start2.setShortName("S2");
        start2.setBoatName("Start Line 2");

        CompoundMark startGate = new CompoundMark("Start Line", Arrays.asList(start1, start2), 11);

        // Finish Line
        Mark finish1 = new Mark(238, new Coordinate(5.00125, -4.00300));
        finish1.setHullNumber("LC28");
        finish1.setStoweName("PRO");
        finish1.setShortName("F1");
        finish1.setBoatName("Finish Line 1");

        Mark finish2 = new Mark(239, new Coordinate(5.00175, -4.00300));
        finish2.setHullNumber("LC29");
        finish2.setStoweName("PIN");
        finish2.setShortName("F2");
        finish2.setBoatName("Finish Line 2");

        CompoundMark finishGate = new CompoundMark("Finish Line", Arrays.asList(finish1, finish2), 12);

        return Arrays.asList(startGate, finishGate);
    }


    @Override
    protected List<Coordinate> getBoundaryMarks() {
        double distortion = 0.00500;
        return Arrays.asList(
                new Coordinate(5.00000, -3.99975 + distortion / 2),
                new Coordinate(5.00000, -4.00125 - distortion / 2),
                new Coordinate(5.00100, -4.00200 - distortion),
                new Coordinate(5.00200, -4.00200 - distortion),
                new Coordinate(5.00300, -4.00125 - distortion / 2),
                new Coordinate(5.00300, -3.99975 + distortion / 2),
                new Coordinate(5.00200, -3.99900 + distortion),
                new Coordinate(5.00100, -3.99900 + distortion)
        );
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
