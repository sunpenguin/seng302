package seng302.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML
    private Label timerLabel;

    @FXML
    private Button startRaceButton;

    @FXML
    private TableView tableView;
    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private RaceClock raceClock;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        try {
            Course course = XMLParser.parseCourse(new File("src/main/resources/course.xml"));
            ArrayList<Boat> boats = XMLParser.parseBoats(new File("src/main/resources/boats.xml"));
            race = new Race(boats, course);
            RaceRenderer rr = new RaceRenderer(race, group);
            rr.renderCourse();
            rr.renderBoats();

            raceLoop = new RaceLoop(race, rr);
            raceClock = new RaceClock(timerLabel);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        ObservableList<Boat> boats = FXCollections.observableArrayList();
        boats.addAll(race.getStartingList());
        tableView.setItems(boats);
        TableColumn<Boat, String> boatPos = new TableColumn("Position");
        boatPos.setCellValueFactory(new PropertyValueFactory<Boat, String>("boatName"));
        TableColumn<Boat, String> boatName = new TableColumn("Name");
        boatName.setCellValueFactory(new PropertyValueFactory<Boat, String>("boatName"));
        tableView.getColumns().setAll(boatName);

    }

    public void startRace() {
        raceClock.start();
        raceLoop.start();
    }

    public void closeProgram() {
        System.exit(0);
    }
}
