package seng302.team18.model;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A test class for the Course class
 */
public class CourseTest {

    Course course;
    CompoundMark startLine;

//    @Test
//    public void noLegsTest() {
//        Course actual = new Course(new ArrayList<>(), new ArrayList<>(), 45);
//        assertEquals(0, actual.getLegs().size());
//    }
//
//    @Test
//    public void nextLegTest() {
//        Mark testMark = new Mark("testMark", new Coordinate(1, 1));
//        ArrayList<Mark> testMarks = new ArrayList<>();
//        testMarks.add(testMark);
//        CompoundMark testCompoundMark0 = new CompoundMark("test0", testMarks);
//        CompoundMark testCompoundMark1 = new CompoundMark("test1", testMarks);
//        CompoundMark testCompoundMark2 = new CompoundMark("test2", testMarks);
//        Leg firstLeg = new Leg(testCompoundMark0, testCompoundMark1, 0);
//
//        Leg expected = new Leg(testCompoundMark1, testCompoundMark2, 1);
//
//        ArrayList<CompoundMark> testCompoundMarks = new ArrayList<>();
//        testCompoundMarks.add(testCompoundMark0);
//        testCompoundMarks.add(testCompoundMark1);
//        testCompoundMarks.add(testCompoundMark2);
//
//        Course actual = new Course(testCompoundMarks, new ArrayList<>(), 45);
//        assertEquals(expected, actual.getNextLeg(firstLeg));
//    }

    @Before
    public void setUp(){
        Mark mark1 = new Mark(1, new Coordinate(88, 102));
        Mark mark2 = new Mark(2, new Coordinate(88.1, 102.1));
        List<Mark> marks =  new ArrayList<>();
        marks.add(mark2);
        marks.add(mark1);
        startLine = new CompoundMark("Start" , marks, 20);

        Mark mark3 = new Mark(3, new Coordinate(88.5, 102.5));
        List<Mark> centre =  new ArrayList<>();
        centre.add(mark3);
        CompoundMark centreC = new CompoundMark("Mark1" , centre, 20);

        Mark mark4 = new Mark(1, new Coordinate(89, 103));
        Mark mark5 = new Mark(2, new Coordinate(89.1, 103.1));
        List<Mark> finish =  new ArrayList<>();
        finish.add(mark4);
        finish.add(mark5);
        CompoundMark finishLine = new CompoundMark("finish" , finish, 20);

        List<CompoundMark> compoundMarks = new ArrayList<>();
        compoundMarks.add(startLine);
        compoundMarks.add(centreC);
        compoundMarks.add(finishLine);

        List<BoundaryMark> boundaries = new ArrayList<>();

        boundaries.add(new BoundaryMark(0, new Coordinate(90, 90)));
        boundaries.add(new BoundaryMark(1, new Coordinate(90, -90)));
        boundaries.add(new BoundaryMark(2, new Coordinate(-90, -90)));
        boundaries.add(new BoundaryMark(3, new Coordinate(-90, 90)));

        List<MarkRounding> markRoundings = new ArrayList<>();

        markRoundings.add(new MarkRounding(0, startLine));
        markRoundings.add(new MarkRounding(1, centreC));
        markRoundings.add(new MarkRounding(2, finishLine));

        course = new Course(compoundMarks, boundaries, 0, 0, ZoneId.of( "UTC-03:00"), markRoundings);

    }

    @Test
    public void getLegFromLefNumberTest(){
        Leg actualLeg1 = course.getLeg(0);
        Leg actualLeg2 = course.getLeg(1);

        Leg expectedLeg1 = course.getLegs().get(0);
        Leg expectedLeg2 = course.getLegs().get(1);
        assertEquals(actualLeg1, expectedLeg1);
        assertEquals(actualLeg2, expectedLeg2);
    }
}
