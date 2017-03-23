package seng302.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
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
    private ToggleButton playPauseToggleButton;

    @FXML
    private Label fpsLabel;
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

            raceClock = new RaceClock(timerLabel);

            RaceLoop rl = new RaceLoop(race, rr, new FPSReporter(fpsLabel));
            rl.start();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

//        ObservableList<Boat> boats = FXCollections.observableArrayList();
//        boats.addAll(race.getFinishedList());
//        tableView.setItems(boats);
        tableView.setItems(race.getFinishedList());
        TableColumn<Boat, Integer> boatPositionColumn = new TableColumn("Position");
        boatPositionColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        TableColumn<Boat, String> boatNameColumn = new TableColumn("Name");
        boatNameColumn.setCellValueFactory(new PropertyValueFactory<>("boatName"));
        TableColumn<Boat, Integer> boatSpeedColumn = new TableColumn("Speed");
        boatSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));

        tableView.getColumns().setAll(boatPositionColumn, boatNameColumn, boatSpeedColumn);
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

    public void closeProgram() {
        System.exit(0);
    }
}
