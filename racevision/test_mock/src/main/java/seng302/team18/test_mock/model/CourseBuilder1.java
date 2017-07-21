package seng302.team18.test_mock.model;

import seng302.team18.model.*;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anton J on 11/07/2017.
 */
public class CourseBuilder1 extends BaseCourseBuilder {

    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        List<CompoundMark> compoundMarks = new ArrayList<>();

        // Start & Finish Line
        Mark mark1 = new Mark(131, new Coordinate(32.298577, -64.854304));
        mark1.setHullNumber("LC21");
        mark1.setStoweName("PRO");
        mark1.setShortName("S1");
        mark1.setBoatName("StartLine1");

        Mark mark2 = new Mark(132, new Coordinate(32.295, -64.85600));
        mark2.setHullNumber("LC22");
        mark2.setStoweName("PIN");
        mark2.setShortName("S2");
        mark2.setBoatName("StartLine2");

        CompoundMark compoundMark0 = new CompoundMark("Start/Finish Line", Arrays.asList(mark1, mark2), 11);
        compoundMarks.add(compoundMark0);

        // Mark
        Mark mark3 = new Mark(133, new Coordinate(32.29651, -64.8514));
        mark3.setHullNumber("LC23");
        mark3.setStoweName("M1");
        mark3.setShortName("Mark");
        mark3.setBoatName("Mark");

        CompoundMark compoundMark1 = new CompoundMark("Mark", Collections.singletonList(mark3), 12);
        compoundMarks.add(compoundMark1);

        // East Gate
        Mark mark4 = new Mark(134, new Coordinate(32.30069, -64.83325));
        mark4.setHullNumber("LC24");
        mark4.setStoweName("G1");
        mark4.setShortName("EG1");
        mark4.setBoatName("EastGate1");

        Mark mark5 = new Mark(135, new Coordinate(32.30269, -64.83325));
        mark5.setHullNumber("LC25");
        mark5.setStoweName("G2");
        mark5.setShortName("EG2");
        mark5.setBoatName("EastGate2");

        CompoundMark compoundMark2 = new CompoundMark("EastGate", Arrays.asList(mark4, mark5), 13);
        compoundMarks.add(compoundMark2);

        // South Gate
        Mark mark6 = new Mark(136, new Coordinate(32.28543, -64.85178));
        mark6.setHullNumber("LC26");
        mark6.setStoweName("G1");
        mark6.setShortName("SG1");
        mark6.setBoatName("SouthGate1");

        Mark mark7 = new Mark(137, new Coordinate(32.28551, -64.84766));
        mark7.setHullNumber("LC27");
        mark7.setStoweName("G1");
        mark7.setShortName("SG2");
        mark7.setBoatName("SouthGate2");

        CompoundMark compoundMark3 = new CompoundMark("SouthGate", Arrays.asList(mark6, mark7), 14);
        compoundMarks.add(compoundMark3);

        // North Gate
        Mark mark8 = new Mark(137, new Coordinate(32.30938, -64.84603));
        mark8.setHullNumber("LC28");
        mark8.setStoweName("G1");
        mark8.setShortName("NG1");
        mark8.setBoatName("NorthGate1");
        Mark mark9 = new Mark(138, new Coordinate(32.30771, -64.84105));
        mark9.setHullNumber("LC29");
        mark9.setStoweName("G2");
        mark9.setShortName("NG2");
        mark9.setBoatName("NorthGate2");
        CompoundMark compoundMark4 = new CompoundMark("NorthGate", Arrays.asList(mark8, mark9), 15);
        compoundMarks.add(compoundMark4);

        return compoundMarks;
    }

    @Override
    protected List<BoundaryMark> getBoundaryMarks() {
        List<BoundaryMark> boundaryMarks = new ArrayList<>();

        boundaryMarks.add(new BoundaryMark(1, new Coordinate(32.31056, -64.84599)));
        boundaryMarks.add(new BoundaryMark(2, new Coordinate(32.30125, -64.82783)));
        boundaryMarks.add(new BoundaryMark(3, new Coordinate(32.28718, -64.83796)));
        boundaryMarks.add(new BoundaryMark(4, new Coordinate(32.28108, -64.85023)));
        boundaryMarks.add(new BoundaryMark(5, new Coordinate(32.29022, -64.86268)));
        boundaryMarks.add(new BoundaryMark(6, new Coordinate(32.30510, -64.85530)));

        return boundaryMarks;
    }

    @Override
    protected double getWindDirection() {
        return 0;
    }

    @Override
    protected double getWindSpeed() {
        return 10;
    }

    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(+12));
    }

    @Override
    protected List<MarkRounding> getMarkRoundings() {
        List<MarkRounding> markRoundings = new ArrayList<>();

        markRoundings.add(new MarkRounding(1, getCompoundMarks().get(0), MarkRounding.Direction.SP, 3));
        markRoundings.add(new MarkRounding(2, getCompoundMarks().get(1), MarkRounding.Direction.PORT, 3));
        markRoundings.add(new MarkRounding(3, getCompoundMarks().get(2), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(4, getCompoundMarks().get(3), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(5, getCompoundMarks().get(4), MarkRounding.Direction.SP, 3));
        markRoundings.add(new MarkRounding(6, getCompoundMarks().get(1), MarkRounding.Direction.SP, 3));
        markRoundings.add(new MarkRounding(7, getCompoundMarks().get(2), MarkRounding.Direction.SP, 3));

        return markRoundings;
    }
}
