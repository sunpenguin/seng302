package seng302.team18.racemodel.builder.course;

import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.model.MarkRounding;

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

        markRoundings.add(new MarkRounding(1, getCompoundMarks().get(0), MarkRounding.Direction.PS, 3));
        markRoundings.add(new MarkRounding(2, getCompoundMarks().get(1), MarkRounding.Direction.STARBOARD, 3));
        markRoundings.add(new MarkRounding(3, getCompoundMarks().get(3), MarkRounding.Direction.SP, 6));
        markRoundings.add(new MarkRounding(4, getCompoundMarks().get(2), MarkRounding.Direction.SP, 6));
        markRoundings.add(new MarkRounding(5, getCompoundMarks().get(3), MarkRounding.Direction.SP, 6));
        markRoundings.add(new MarkRounding(6, getCompoundMarks().get(4), MarkRounding.Direction.SP, 3));

        return markRoundings;
    }


    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        // Start Line
        Mark start1 = new Mark(231, new Coordinate(5.27428, -4.42534));
        start1.setHullNumber("LC21");
        start1.setStoweName("PRO");
        start1.setShortName("S1");
        start1.setBoatName("Start Line 1");

        Mark start2 = new Mark(232, new Coordinate(5.27381, -4.42483));
        start2.setHullNumber("LC22");
        start2.setStoweName("PIN");
        start2.setShortName("S2");
        start2.setBoatName("Start Line 2");

        CompoundMark startGate = new CompoundMark("Start Line", Arrays.asList(start1, start2), 11);

        // Mark 1
        Mark mark1 = new Mark(233, new Coordinate(5.27502, -4.42309));
        mark1.setHullNumber("LC23");
        mark1.setStoweName("M1");
        mark1.setShortName("M1");
        mark1.setBoatName("Mark 1");

        CompoundMark compoundMark1 = new CompoundMark("Mark 1", Collections.singletonList(mark1), 12);

        // Leeward Mark
        Mark lee1 = new Mark(234, new Coordinate(5.27656, -4.42442));
        lee1.setHullNumber("LC24");
        lee1.setStoweName("PRO");
        lee1.setShortName("LG1");
        lee1.setBoatName("Leeward Gate 1");

        Mark lee2 = new Mark(235, new Coordinate(5.27584, -4.42505));
        lee2.setHullNumber("LC25");
        lee2.setStoweName("PIN");
        lee2.setShortName("LG2");
        lee2.setBoatName("Leeward Gate 2");

        CompoundMark leewardGate = new CompoundMark("Leeward Gate", Arrays.asList(lee1, lee2), 13);

        // Windward Mark
        Mark wind1 = new Mark(236, new Coordinate(5.27317, -4.42187));
        wind1.setHullNumber("LC26");
        wind1.setStoweName("PRO");
        wind1.setShortName("WG1");
        wind1.setBoatName("Windward Gate 1");

        Mark wind2 = new Mark(237, new Coordinate(5.27378, -4.42118));
        wind2.setHullNumber("LC27");
        wind2.setStoweName("PIN");
        wind2.setShortName("WG2");
        wind2.setBoatName("Windward Gate 2");

        CompoundMark windwardGate = new CompoundMark("Windward Gate", Arrays.asList(wind1, wind2), 14);

        // Finish Line
        Mark finish1 = new Mark(238, new Coordinate(5.27227, -4.42245));
        finish1.setHullNumber("LC28");
        finish1.setStoweName("PRO");
        finish1.setShortName("F1");
        finish1.setBoatName("Finish Line 1");

        Mark finish2 = new Mark(239, new Coordinate(5.27161, -4.42228));
        finish2.setHullNumber("LC29");
        finish2.setStoweName("PIN");
        finish2.setShortName("F2");
        finish2.setBoatName("Finish Line 2");

        CompoundMark finishGate = new CompoundMark("Finish Line", Arrays.asList(finish1, finish2), 15);

        return Arrays.asList(startGate, compoundMark1, leewardGate, windwardGate, finishGate);
    }


    @Override
    public List<Coordinate> getBoundaryMarks() {
        return Arrays.asList(
                new Coordinate(5.27735, -4.42376),
                new Coordinate(5.27729, -4.42503),
                new Coordinate(5.27629, -4.42593),
                new Coordinate(5.27507, -4.42568),
                new Coordinate(5.27476, -4.42530),
                new Coordinate(5.27418, -4.42561),
                new Coordinate(5.27348, -4.42630),
                new Coordinate(5.27279, -4.42560),
                new Coordinate(5.27354, -4.42492),
                new Coordinate(5.27369, -4.42404),
                new Coordinate(5.27247, -4.42259),
                new Coordinate(5.27209, -4.42314),
                new Coordinate(5.27141, -4.42298),
                new Coordinate(5.27145, -4.42146),
                new Coordinate(5.27334, -4.42017),
                new Coordinate(5.27442, -4.42039)
        );
    }


    @Override
    protected double getWindDirection() {
        return 325;
    }


    @Override
    protected double getWindSpeed() {
        return 30;
    }


    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(5));
    }
}
