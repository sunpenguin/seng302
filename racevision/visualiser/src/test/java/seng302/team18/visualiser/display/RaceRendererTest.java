package seng302.team18.visualiser.display;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created by dhl25 on 17/03/17.
 */
public class RaceRendererTest {

    private RaceRenderer actualRaceRenderer;

//    @Before
//    public void setUp() {
//        AnchorPane raceViewAnchorPane = new AnchorPane();
//        // Create expected
//
//        // Create boat for race
//        ArrayList<Yacht> boats = new ArrayList<>();
//        boats.add(new Yacht("a", "b", 0));
//
//        // Create CompoundMark needed for Course
//        Mark testMark0 = new Mark("testMark0", new Coordinate(0, 0));
//        Mark testMark1 = new Mark("testMark1", new Coordinate(90, 180));
//        List<Mark> testMarks0 = new ArrayList<>();
//        List<Mark> testMarks1 = new ArrayList<>();
//        testMarks0.add(testMark0);
//        testMarks1.add(testMark1);
//        CompoundMark testCompoundMark0 = new CompoundMark("test0", testMarks0);
//        CompoundMark testCompoundMark1 = new CompoundMark("test1", testMarks1);
//        ArrayList<CompoundMark> compoundMarks = new ArrayList<>();
//        compoundMarks.add(testCompoundMark0);
//        compoundMarks.add(testCompoundMark1);
//        Course course = new Course(compoundMarks, new ArrayList<>(), 45, ZoneId.of("Pacific/Auckland"));
//
//        // Create everything needed for RaceRenderer + RaceRenderer
//        Group group = new Group();
//        Race race = new Race(boats, course);
//        actualRaceRenderer = new RaceRenderer(race, group, raceViewAnchorPane);
//    }


    /**
     * test just for the boats ie no wake or annotations
     */
//    @Test
    public void boatTest() {
        ObservableList<Polyline> expected = FXCollections.observableArrayList();
        Polyline boatImage = new Polyline();
        boatImage.setLayoutX(60); // padding size
        boatImage.setLayoutY(-360); // - ( size of screen / 2)
        boatImage.getPoints().addAll(
                5.0, 0.0,
                10.0, 10.0,
                0.0, 10.0,
                5.0, 0.0,
                5.0, 10.0);
        boatImage.setFill(Color.VIOLET);
        expected.add(boatImage);


//        raceRenderer.renderBoats(true, 0);
        ObservableList<Node> actual = actualRaceRenderer.getGroup().getChildren();

        List<Polyline> actualPolylines = new ArrayList<>();
        for (Node node: actual) {
            if (node instanceof Polyline) {
                actualPolylines.add((Polyline) node);
            }
        }

        // check equality
        assertEquals(expected.size(), actualPolylines.size());

        for (int i = 0; i < expected.size(); i++) {
            Polyline act = actualPolylines.get(i);
            Polyline exp = expected.get(i);
            assertEquals(exp.getFill(), act.getFill());
            assertEquals(exp.getPoints().size(), act.getPoints().size());
            for (int j = 0; j < exp.getPoints().size(); j++) {
                assertEquals(exp.getPoints().get(j), act.getPoints().get(j), 0.001);
            }
        }
    }


}
