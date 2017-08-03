package seng302.team18.racemodel.model;

import seng302.team18.model.*;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CourseBuilderRealistic extends AbstractCourseBuilder {


    @Override
    protected List<MarkRounding> getMarkRoundings() {
        List<MarkRounding> markRoundings = new ArrayList<>();

        markRoundings.add(new MarkRounding(1, getCompoundMarks().get(0), MarkRounding.Direction.SP, 3));
        markRoundings.add(new MarkRounding(2, getCompoundMarks().get(1), MarkRounding.Direction.PORT, 3));
        markRoundings.add(new MarkRounding(3, getCompoundMarks().get(2), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(4, getCompoundMarks().get(3), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(5, getCompoundMarks().get(2), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(6, getCompoundMarks().get(4), MarkRounding.Direction.SP, 3));

        return markRoundings;
    }


    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        // Start Line
        Mark start1 = new Mark(231, new Coordinate(51.577251, -3.987752));
        start1.setHullNumber("LC21");
        start1.setStoweName("PRO");
        start1.setShortName("S1");
        start1.setBoatName("Start Line 1");

        Mark start2 = new Mark(232, new Coordinate(51.576266, -3.985881));
        start2.setHullNumber("LC22");
        start2.setStoweName("PIN");
        start2.setShortName("S2");
        start2.setBoatName("Start Line 2");

        CompoundMark startGate = new CompoundMark("Start Line", Arrays.asList(start1, start2), 11);

        // Mark 1
        Mark mark1 = new Mark(233, new Coordinate(51.581681, -3.980099));
        mark1.setHullNumber("LC23");
        mark1.setStoweName("M1");
        mark1.setShortName("M1");
        mark1.setBoatName("Mark 1");

        CompoundMark compoundMark1 = new CompoundMark("Mark 1", Collections.singletonList(mark1), 12);

        // Windward Mark
        Mark wind1 = new Mark(234, new Coordinate(51.585468, -3.987611));
        wind1.setHullNumber("LC24");
        wind1.setStoweName("PRO");
        wind1.setShortName("WG1");
        wind1.setBoatName("Windward Gate 1");

        Mark wind2 = new Mark(235, new Coordinate(51.586557, -3.986318));
        wind2.setHullNumber("LC25");
        wind2.setStoweName("PIN");
        wind2.setShortName("WG2");
        wind2.setBoatName("Windward Gate 2");

        CompoundMark windwardGate = new CompoundMark("Windward Gate", Arrays.asList(wind1, wind2), 13);

        // Leeward Mark
        Mark lee1 = new Mark(236, new Coordinate(51.577927, -3.974071));
        lee1.setHullNumber("LC26");
        lee1.setStoweName("PRO");
        lee1.setShortName("LG1");
        lee1.setBoatName("Leeward Gate 1");

        Mark lee2 = new Mark(237, new Coordinate(51.578840, -3.972075));
        lee2.setHullNumber("LC27");
        lee2.setStoweName("PIN");
        lee2.setShortName("LG2");
        lee2.setBoatName("Leeward Gate 2");

        CompoundMark leewardGate = new CompoundMark("Leeward Gate", Arrays.asList(lee1, lee2), 14);

        // Finish Line
        Mark finish1 = new Mark(238, new Coordinate(51.589288, -3.982857));
        finish1.setHullNumber("LC28");
        finish1.setStoweName("PRO");
        finish1.setShortName("F1");
        finish1.setBoatName("Finish Line 1");

        Mark finish2 = new Mark(239, new Coordinate(51.588255, -3.982149));
        finish2.setHullNumber("LC29");
        finish2.setStoweName("PIN");
        finish2.setShortName("F2");
        finish2.setBoatName("Finish Line 2");

        CompoundMark finishGate = new CompoundMark("Finish Line", Arrays.asList(finish1, finish2), 15);

        return Arrays.asList(startGate, compoundMark1, windwardGate, leewardGate, finishGate);
    }


    @Override
    protected List<BoundaryMark> getBoundaryMarks() {
        return new ArrayList<>();
    }


    @Override
    protected double getWindDirection() {
        return 145;
    }


    @Override
    protected double getWindSpeed() {
        return 30;
    }


    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(0));
    }
}
