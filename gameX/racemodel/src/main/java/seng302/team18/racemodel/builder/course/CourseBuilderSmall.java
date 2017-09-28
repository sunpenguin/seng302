package seng302.team18.racemodel.builder.course;

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
 * Builder with a small default course.
 */
public class CourseBuilderSmall extends AbstractCourseBuilder {

    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        List<CompoundMark> compoundMarks = new ArrayList<>();

        // start
        Mark start1 = new Mark(131, new Coordinate(32.292393, -64.857638));
        start1.setHullNumber("LC21");
        start1.setStoweName("PRO");
        start1.setShortName("S1");
        start1.setBoatName("StartLine1");
        Mark start2 = new Mark(132, new Coordinate(32.294425, -64.85678));
        start2.setHullNumber("LC22");
        start2.setStoweName("PIN");
        start2.setShortName("S2");
        start2.setBoatName("StartLine2");
        compoundMarks.add(new CompoundMark("Start Line", Arrays.asList(start1, start2), 11));

        // finish
        Mark finish1 = new Mark(138, new Coordinate(32.292396, -64.857635));
        finish1.setHullNumber("LC28");
        finish1.setStoweName("G1");
        finish1.setShortName("F1");
        finish1.setBoatName("FinishLine1");
        Mark finish2 = new Mark(139, new Coordinate(32.294428, -64.85675));
        finish2.setHullNumber("LC29");
        finish2.setStoweName("G2");
        finish2.setShortName("F2");
        finish2.setBoatName("FinishLine2");
        compoundMarks.add(new CompoundMark("Finish Line", Arrays.asList(finish1, finish2), 15));

        return compoundMarks;
    }

    @Override
    public List<Coordinate> getBoundaryMarks() {
        return new ArrayList<>();
    }

    @Override
    protected double getWindDirection() {
        return 315;
    }

    @Override
    protected double getWindSpeed() {
        return 45;
    }


    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-3));
    }


    @Override
    protected List<MarkRounding> getMarkRoundings() {
        List<MarkRounding> markRoundings = new ArrayList<>();

        List<CompoundMark> marks = getCompoundMarks();
        for (int i = 0; i < marks.size(); i++) {
            markRoundings.add(new MarkRounding(i + 1, getCompoundMarks().get(i), MarkRounding.Direction.SP, 3));
        }
        return markRoundings;
    }
}
