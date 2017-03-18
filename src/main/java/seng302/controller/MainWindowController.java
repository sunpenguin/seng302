package seng302.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import org.xml.sax.SAXException;
import seng302.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by dhl25 on 15/03/17.
 */
public class MainWindowController {
    @FXML
    private Group group;
    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
//        Circle c = new Circle(10, Color.RED);
//        c.setCenterX(0);
//        c.setCenterY(0);
//        group.getChildren().add(c);


        try {
            // TODO remember to change the path of the xml files
            Course course = XMLParser.parseCourse(new File("/home/cosc/student/jth102/302/project302/team-18/src/main/resources/course.xml"));
            ArrayList<Boat> boats = XMLParser.parseBoats(new File("/home/cosc/student/jth102/302/project302/team-18/src/main/resources/boats.xml"));
            race = new Race(boats, course);
            race.LOL(); // sets intial coordinates of boats
            RaceRenderer rr = new RaceRenderer(race, group);
            rr.renderCourse();
//            rr.renderBoats();
            RaceLoop rl = new RaceLoop(race, rr);
            rl.handle(System.nanoTime()); // first time its called it does nothing
            rl.handle(System.nanoTime()); // this is when the magic happens
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void closeProgram() {
        System.exit(0);
    }

//    public void start() {
//        graphicsContext.setFill(Color.BEIGE);
//        graphicsContext.fillOval(20, 50, 20,20);
//    }

}
