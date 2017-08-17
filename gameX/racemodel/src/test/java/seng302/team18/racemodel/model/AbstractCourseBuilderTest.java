package seng302.team18.racemodel.model;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.*;
import seng302.team18.util.GPSCalculations;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class AbstractCourseBuilderTest {
    private final List<CompoundMark> compoundMarks = setUpCompoundMarks();
    private final List<MarkRounding> markRoundings = setUpMarkRoundings();
    private final List<Coordinate> boundaryMarks = setUpBoundaryMarks();
    private final double windDirection = 50.5;
    private final double windSpeed = 15;
    private final ZoneId timeZone = ZoneId.of(ZoneId.systemDefault().getId());

    private Course course;


    @Before
    public void setUp() throws Exception {
        course = (new ConcreteCourseBuilder().buildCourse());
    }

    @Test
    public void buildCourse_compoundMarks() throws Exception {
        assertEquals("incorrect number of compound marks", compoundMarks.size(), course.getCompoundMarks().size());

        for (int i = 0; i < compoundMarks.size(); i++) {
            CompoundMark expected = compoundMarks.get(i);
            CompoundMark actual = course.getCompoundMarks().get(i);

            assertEquals("compound mark" + i + "is not as expected", expected, actual);
        }
    }


    @Test
    public void buildCourse_boundary() throws Exception {
        assertEquals("incorrect number of course boundary marks", boundaryMarks.size(), course.getCourseLimits().size());

        for (int i = 0; i < boundaryMarks.size(); i++) {
            Coordinate expected = boundaryMarks.get(i);
            Coordinate actual = course.getCourseLimits().get(i);

            assertEquals("course boundary" + i + "is not as expected", expected, actual);
        }
    }


    @Test
    public void buildCourse_windDirection() throws Exception {
        assertEquals("wind direction is not as expected", windDirection, course.getWindDirection(), 1e-6);
    }


    @Test
    public void buildCourse_windSpeed() throws Exception {
        assertEquals("wind speed is not as expected", windSpeed, course.getWindSpeed(), 1e-6);
    }


    @Test
    public void buildCourse_timeZone() throws Exception {
        assertEquals("time zone is not as expected", timeZone, course.getTimeZone());
    }


    @Test
    public void buildCourse_markRoundings() throws Exception {
        assertEquals("incorrect number of mark roundings", markRoundings.size(), course.getMarkSequence().size());

        for (int i = 0; i < markRoundings.size(); i++) {
            MarkRounding expected = markRoundings.get(i);
            MarkRounding actual = course.getMarkSequence().get(i);

            assertEquals("mark rounding " + i + "is not as expected", expected, actual);
        }
    }


    @Test
    public void buildCourse_centreCoordinates() throws Exception {
        GPSCalculations gpsCalculations = new GPSCalculations();
        List<Coordinate> extremes = gpsCalculations.findMinMaxPoints(course);
        Coordinate centralCoordinate = gpsCalculations.midPoint(extremes.get(0), extremes.get(1));

        assertEquals("centre coordinate is not as expected", centralCoordinate, course.getCentralCoordinate());
    }


    private List<MarkRounding> setUpMarkRoundings() {
        List<MarkRounding> markRoundings = new ArrayList<>();

        markRoundings.add(new MarkRounding(1, compoundMarks.get(0), MarkRounding.Direction.SP, 3));
        markRoundings.add(new MarkRounding(2, compoundMarks.get(1), MarkRounding.Direction.PORT, 3));
        markRoundings.add(new MarkRounding(3, compoundMarks.get(2), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(4, compoundMarks.get(3), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(6, compoundMarks.get(1), MarkRounding.Direction.SP, 3));
        markRoundings.add(new MarkRounding(7, compoundMarks.get(2), MarkRounding.Direction.SP, 3));

        return markRoundings;
    }


    private List<CompoundMark> setUpCompoundMarks() {
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


    private List<Coordinate> setUpBoundaryMarks() {
        return Arrays.asList(
                new Coordinate(32.31056, -64.84599),
                new Coordinate(32.30125, -64.82783),
                new Coordinate(32.28718, -64.83796),
                new Coordinate(32.29022, -64.86268),
                new Coordinate(32.30510, -64.85530)
        );
    }


    private class ConcreteCourseBuilder extends AbstractCourseBuilder {
        @Override
        protected List<MarkRounding> getMarkRoundings() {
            return markRoundings;
        }

        @Override
        protected List<CompoundMark> buildCompoundMarks() {
            return compoundMarks;
        }

        @Override
        protected List<Coordinate> getBoundaryMarks() {
            return boundaryMarks;
        }

        @Override
        protected double getWindDirection() {
            return windDirection;
        }

        @Override
        protected double getWindSpeed() {
            return windSpeed;
        }

        @Override
        protected ZoneId getTimeZone() {
            return timeZone;
        }
    }
}