package seng302.team18.visualiser.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.display.ZoneTimeClock;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller for the pre race view
 */
public class PreRaceController {

    @FXML private Label timeLabel;
    @FXML private Label startTimeLabel;
    @FXML private ListView<Boat> listView;
    @FXML private Label timeZoneLabel;

    private ZoneTimeClock preRaceClock;

    @FXML
    public void initialize() {
        preRaceClock = new ZoneTimeClock(timeLabel, DateTimeFormatter.ofPattern("HH:mm:ss"));

    }

    /**
     * Initialises the variables associated with the beginning of the race. Shows the pre-race window for a specific
     * duration before the race starts.
     * @param startTime The official start time of the race.
     * @param boats The boats participatin gin the race.
     */
    public void setUp(ZonedDateTime startTime, List<Boat> boats) {
        displayTimeZone(startTime);
        startTimeLabel.setText(startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        setUpLists(boats);
        preRaceClock.start();
    }


    /**
     * SHOWS THE TIME ZONE OF THE RACE
     * @param zoneTime USED TO GET THE UTC OFFSET
     */
    private void displayTimeZone(ZonedDateTime zoneTime) {
        timeZoneLabel.setText("UTC " + zoneTime.getOffset().toString());
    }


    /**
     * Sets the list view of the participants in the race.
     * @param boats The race participants
     */
    private void setUpLists(List<Boat> boats) {
        listView.setItems(FXCollections.observableList(boats));
        listView.setCellFactory(param -> new ListCell<Boat>() {
            @Override
            protected void updateItem(Boat boat, boolean empty) {
                super.updateItem(boat, empty);
                if (empty || boat == null) {
                    setText(null);
                } else {
                    setText(boat.getName());
                }
            }
        });
    }

    public ZoneTimeClock getClock() {
        return preRaceClock;
    }

}
