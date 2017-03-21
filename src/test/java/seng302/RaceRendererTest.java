package seng302;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


/**
 * Created by dhl25 on 17/03/17.
 */
public class RaceRendererTest {

    @Test
    public void renderCourseTest() {
        // Setting up Course
        Mark testMark0 = new Mark("testMark0", new Coordinate(0, 0));
        Mark testMark1 = new Mark("testMark1", new Coordinate(90, 180));
        ArrayList<Mark> testMarks0 = new ArrayList<>();
        ArrayList<Mark> testMarks1 = new ArrayList<>();
        testMarks0.add(testMark0);
        testMarks1.add(testMark1);
        CompoundMark testCompoundMark0 = new CompoundMark("test0", testMarks0);
        CompoundMark testCompoundMark1 = new CompoundMark("test1", testMarks1);
        ArrayList<CompoundMark> compoundMarks = new ArrayList<>();
        compoundMarks.add(testCompoundMark0);
        compoundMarks.add(testCompoundMark1);
        Course course = new Course(compoundMarks);

        // Setting up expected values
        Rectangle r1 = new Rectangle(10, 10, Color.BLACK);
        r1.setX(55.0); // size of padding - size of rectangle / 2
        r1.setY(-65.0); // size of padding + size of rectangle / -2
        Rectangle r2 = new Rectangle(10, 10, Color.BLACK);
        r2.setX(655.0); // size of screen - size of padding - size of rectangle / 2
        r2.setY(-665.0); // (size of screen - size of padding + size of rectangle / 2) * -1
        ObservableList<Node> expected = FXCollections.observableArrayList();
        expected.add(r1);
        expected.add(r2);

        // making actual values
        Group group = new Group();
        Race race = new Race(new ArrayList<>(), course);
        RaceRenderer raceRenderer = new RaceRenderer(race, group);
        raceRenderer.renderCourse();
        ObservableList<Node> actual = raceRenderer.getGroup().getChildren();

        // check for equality
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Rectangle act = (Rectangle) actual.get(i);
            Rectangle exp = (Rectangle) expected.get(i);
            assertEquals(exp.getFill(), act.getFill());
            assertEquals(exp.getHeight(), act.getHeight(), 0.001);
            assertEquals(exp.getWidth(), act.getWidth(), 0.001);
            assertEquals(exp.getX(), act.getX(), 0.001);
            assertEquals(exp.getY(), act.getY(), 0.001);
        }
    }


    @Test
    public void renderBoatsTest() {
        // Create expected
        Group g = new Group();
        ObservableList<Node> expected = g.getChildren();
        Circle c = new Circle(10.0, Color.VIOLET);
        c.setCenterX(60.0); // size of padding
        c.setCenterY(-360.0); // size of screen / -2
        expected.add(c);

        // Create boat for race
        ArrayList<Boat> boats = new ArrayList<>();
        boats.add(new Boat("a", "b", 0));

        // Create CompoundMark needed for Course
        Mark testMark0 = new Mark("testMark0", new Coordinate(0, 0));
        Mark testMark1 = new Mark("testMark1", new Coordinate(90, 180));
        ArrayList<Mark> testMarks = new ArrayList<>();
        testMarks.add(testMark0);
        testMarks.add(testMark1);
        CompoundMark testCompoundMark0 = new CompoundMark("test0", testMarks);
        CompoundMark testCompoundMark1 = new CompoundMark("test1", testMarks);
        ArrayList<CompoundMark> compoundMarks = new ArrayList<>();
        compoundMarks.add(testCompoundMark0);
        compoundMarks.add(testCompoundMark1);

        // Create everything needed for RaceRenderer + RaceRenderer
        Group group = new Group();
        Course course = new Course(compoundMarks);
        Race race = new Race(boats, course);
        RaceRenderer raceRenderer = new RaceRenderer(race, group);

        raceRenderer.renderBoats();
        ObservableList<Node> actual = raceRenderer.getGroup().getChildren();

        // check equality
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Circle act = (Circle) actual.get(i);
            Circle exp = (Circle) expected.get(i);
            assertEquals(exp.getCenterX(), act.getCenterX(), 0.001);
            assertEquals(exp.getCenterY(), act.getCenterY(), 0.001);
            assertEquals(exp.getRadius(), act.getRadius(), 0.001);
            assertEquals(exp.getFill(), act.getFill());
        }
    }


}
