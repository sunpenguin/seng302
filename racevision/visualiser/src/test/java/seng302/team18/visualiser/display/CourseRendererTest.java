package seng302.team18.visualiser.display;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.Mark;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by dhl25 on 30/03/17.
 */
public class CourseRendererTest {

    //    @Test
    public void renderCourseTest() {
        // Setting up Course
        Pane pane = new Pane();
        pane.setPrefWidth(1280);
        pane.setPrefHeight(720);
        Mark testMark0 = new Mark("testMark0", new Coordinate(0, 0));
        Mark testMark1 = new Mark("testMark1", new Coordinate(90, 180));
        List<Mark> testMarks0 = new ArrayList<>();
        List<Mark> testMarks1 = new ArrayList<>();
        testMarks0.add(testMark0);
        testMarks1.add(testMark1);
        CompoundMark testCompoundMark0 = new CompoundMark("test0", testMarks0);
        CompoundMark testCompoundMark1 = new CompoundMark("test1", testMarks1);
        ArrayList<CompoundMark> compoundMarks = new ArrayList<>();
        compoundMarks.add(testCompoundMark0);
        compoundMarks.add(testCompoundMark1);
        Course course = new Course(compoundMarks, new ArrayList<>(), 45, ZoneId.of("Pacific/Auckland"));

        // Setting up expected values
        Rectangle r1 = new Rectangle(10, 10, Color.BLACK);
        r1.setX(20 - 10 / 2d); // size of padding - size of rectangle / 2
        r1.setY(-1 * (20 + 10 / 2d)); // -(size of padding + size of rectangle / 2)
        Rectangle r2 = new Rectangle(10, 10, Color.BLACK);
        r2.setX(695.0); // size of screen - size of padding - size of rectangle / 2 OLD
        r2.setY(-1385.0); // -(size of screen - size of padding + size of rectangle / 2) OLD
        ObservableList<Node> expected = FXCollections.observableArrayList();
//        Polyline[points=[], fill=null, stroke=0x000000ff, strokeWidth=1.0]
        Polyline polyline = new Polyline();
        expected.add(r1);
        expected.add(r2);
        expected.add(polyline);

        // making actual values
        Group group = new Group();
        CourseRenderer courseRenderer = new CourseRenderer(course, group, pane);
        ObservableList<Node> actual = courseRenderer.getGroup().getChildren();

        // check for equality
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size() - 1; i++) {
            Rectangle act = (Rectangle) actual.get(i);
            Rectangle exp = (Rectangle) expected.get(i);
            assertEquals(exp.getFill(), act.getFill());
            assertEquals(exp.getHeight(), act.getHeight(), 0.001);
            assertEquals(exp.getWidth(), act.getWidth(), 0.001);
            assertEquals(exp.getX(), act.getX(), 0.001);
            assertEquals(exp.getY(), act.getY(), 0.001);
        }
        // check polyline equality
        List<Double> expectedPoints = ((Polyline) expected.get(expected.size() - 1)).getPoints();
        List<Double> actualPoints = ((Polyline) actual.get(actual.size() - 1)).getPoints();
        assertEquals(expectedPoints.size(), actualPoints.size());
        for (int i = 0; i < expectedPoints.size(); i++) {
            assertEquals(expectedPoints.get(i), actualPoints.get(i), 0.1);
        }
    }

}
