package seng302.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TableView tableView;
    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        try {
            Course course = XMLParser.parseCourse(new File("src/main/resources/course.xml"));
            ArrayList<Boat> boats = XMLParser.parseBoats(new File("src/main/resources/boats.xml"));
            race = new Race(boats, course);
            RaceRenderer rr = new RaceRenderer(race, group);
            rr.renderCourse();

            RaceTimer raceClock = new RaceTimer(group, timerLabel);
            raceClock.start();

            RaceLoop rl = new RaceLoop(race, rr);
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
        tableView.getColumns().setAll(boatPositionColumn, boatNameColumn);
    }

    public void closeProgram() {
        System.exit(0);
    }

}
