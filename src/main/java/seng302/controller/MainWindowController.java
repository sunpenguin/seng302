package seng302.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Polygon;
import org.xml.sax.SAXException;
import seng302.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by dhl25 on 15/03/17.
 */
public class MainWindowController {
    @FXML
    private Group group;
    @FXML
    private Label timerLabel;
    @FXML
    private ToggleButton playPauseToggleButton;
    @FXML
    private ToggleButton fpsToggler;
    @FXML
    private Label fpsLabel;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Boat, Integer> boatPositionColumn;
    @FXML
    private TableColumn<Boat, String> boatNameColumn;
    @FXML
    private TableColumn<Boat, Integer> boatSpeedColumn;

    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private RaceClock raceClock;

    @FXML
    private Polygon arrow;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        try {
            Course course = XMLParser.parseCourse(new File("src/main/resources/course.xml"));
            ArrayList<Boat> boats = XMLParser.parseBoats(new File("src/main/resources/boats.xml"));

            race = new Race(boats, course);
            raceRenderer = new RaceRenderer(race, group);
            raceRenderer.renderCourse();
            raceRenderer.renderBoats();
            raceClock = new RaceClock(timerLabel, race);
            raceLoop = new RaceLoop(race, raceRenderer, new FPSReporter(fpsLabel));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        tableView.setItems(race.getStartingList());
        boatPositionColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        boatNameColumn.setCellValueFactory(new PropertyValueFactory<>("boatName"));
        boatSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));

        tableView.getColumns().setAll(boatPositionColumn, boatNameColumn, boatSpeedColumn);
        Random ran = new Random();
        double windDegree = ran.nextDouble() * 360;
        arrow.setRotate(windDegree);
    }


    public void playPauseRace() {
        if (playPauseToggleButton.isSelected()) {
            raceClock.start();
            raceLoop.start();
        } else {
            raceClock.stop();
            raceLoop.stop();
        }
    }


    public void toggleFPS() {
        fpsLabel.setVisible(!fpsToggler.isSelected());
    }


    public void setFullAnnotationLevel() {
        HashMap<String, Boolean> visibleAnnotations = raceRenderer.getVisibleAnnotations();
        visibleAnnotations.put("Speed", true);
        visibleAnnotations.put("Name", true);

        raceRenderer.setVisibleAnnotations(visibleAnnotations);
    }


    public void setNoneAnnotationLevel() {
        HashMap<String, Boolean> visibleAnnotations = raceRenderer.getVisibleAnnotations();
        visibleAnnotations.put("Speed", false);
        visibleAnnotations.put("Name", false);

        raceRenderer.setVisibleAnnotations(visibleAnnotations);
    }


    public void setImportantAnnotationLevel() {
        HashMap<String, Boolean> visibleAnnotations = raceRenderer.getVisibleAnnotations();
        visibleAnnotations.put("Speed", false);
        visibleAnnotations.put("Name", true);

        raceRenderer.setVisibleAnnotations(visibleAnnotations);
    }


    public void closeProgram() {
        System.exit(0);
    }

//    public void start() {
//        graphicsContext.setFill(Color.BEIGE);
//        graphicsContext.fillOval(20, 50, 20,20);
//    }

}
