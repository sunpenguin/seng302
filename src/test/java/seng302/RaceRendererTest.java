//package seng302;
//
//import javafx.collections.ObservableList;
//import javafx.scene.Group;
//import javafx.scene.Node;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.Assert.assertEquals;
//
//
///**
// * Created by dhl25 on 17/03/17.
// */
//public class RaceRendererTest {
//
//    @Test
//    public void renderBoatsTest() {
//        Group g = new Group();
//        ObservableList<Node> expected = g.getChildren();
//        Circle c = new Circle(10.0, Color.BLACK);
//        c.setCenterX(0); // temporary
//        c.setCenterY(0); // temporary
//        expected.add(c);
//
//
//        Group group = new Group();
//        ArrayList<Boat> boats = new ArrayList<>();
//        boats.add(new Boat("a", "b"));
//        Course course = new Course(new ArrayList<>());
//        Race race = new Race(boats, course);
//        RaceRenderer raceRenderer = new RaceRenderer(race, group);
//        raceRenderer.renderBoats();
//        ObservableList<Node> actual = raceRenderer.getGroup().getChildren();
//
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < expected.size(); i++) {
//            assertEquals(expected.get(i), actual.get(i));
//        }
//    }
//
//
//}
