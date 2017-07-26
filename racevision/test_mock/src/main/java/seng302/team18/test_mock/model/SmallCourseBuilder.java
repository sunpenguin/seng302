package seng302.team18.test_mock.model;

import seng302.team18.model.*;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Builder with a small default course.
 */
public class SmallCourseBuilder extends AbstractCourseBuilder {

    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        List<CompoundMark> compoundMarks = new ArrayList<>();

        // start
        Mark start1 = new Mark(131, new Coordinate(32.292393,-64.857638));
        start1.setHullNumber("LC21");
        start1.setStoweName("PRO");
        start1.setShortName("S1");
        start1.setBoatName("StartLine1");
        Mark start2 = new Mark(132, new Coordinate(32.294425,-64.85678));
        start2.setHullNumber("LC22");
        start2.setStoweName("PIN");
        start2.setShortName("S2");
        start2.setBoatName("StartLine2");
        compoundMarks.add(new CompoundMark("Start Line", Arrays.asList(start1, start2), 11));

        // first mark
        Mark center = new Mark(133, new Coordinate(32.293663,-64.853389));
        center.setHullNumber("LC23");
        center.setStoweName("M1");
        center.setShortName("Mark");
        center.setBoatName("Mark");
        compoundMarks.add(new CompoundMark("Mark", Collections.singletonList(center), 12));

        // windward
        Mark wind1 = new Mark(134, new Coordinate(32.30069, -64.83325));
        wind1.setHullNumber("LC24");
        wind1.setStoweName("G1");
        wind1.setShortName("WG1");
        wind1.setBoatName("WindGate1");
        Mark wind2 = new Mark(135, new Coordinate(32.30269, -64.83325));
        wind2.setHullNumber("LC25");
        wind2.setStoweName("G2");
        wind2.setShortName("WG2");
        wind2.setBoatName("WindGate2");
        compoundMarks.add(new CompoundMark("WindWardGate", Arrays.asList(wind1, wind2), 13));

        // down wind
        Mark downWind1 = new Mark(136, new Coordinate(32.28543, -64.85178));
        downWind1.setHullNumber("LC26");
        downWind1.setStoweName("G1");
        downWind1.setShortName("DW1");
        downWind1.setBoatName("DownWind1");
        Mark downWind2 = new Mark(137, new Coordinate(32.28551, -64.84766));
        downWind2.setHullNumber("LC27");
        downWind2.setStoweName("G1");
        downWind2.setShortName("DW2");
        downWind2.setBoatName("DownWind2");
        compoundMarks.add(new CompoundMark("DownWindGate", Arrays.asList(downWind1, downWind2), 14));

        // finish
        Mark finish1 = new Mark(138, new Coordinate(32.30938, -64.84603));
        finish1.setHullNumber("LC28");
        finish1.setStoweName("G1");
        finish1.setShortName("F1");
        finish1.setBoatName("FinishLine1");
        Mark finish2 = new Mark(139, new Coordinate(32.30771, -64.84105));
        finish2.setHullNumber("LC29");
        finish2.setStoweName("G2");
        finish2.setShortName("F2");
        finish2.setBoatName("FinishLine2");
        compoundMarks.add(new CompoundMark("Finish Line", Arrays.asList(finish1, finish2), 15));

        return compoundMarks;
    }

    @Override
    protected List<BoundaryMark> getBoundaryMarks() {
        return new ArrayList<>();
    }

    @Override
    protected double getWindDirection() {
        return 59;
    }

    @Override
    protected double getWindSpeed() {
        return 15;
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
