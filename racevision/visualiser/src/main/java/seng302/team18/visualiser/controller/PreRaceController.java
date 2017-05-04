package seng302.team18.visualiser.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.display.ZoneTimeClock;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by dhl25 on 27/03/17.
 */
public class PreRaceController {
    @FXML
    private Label timeLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private ListView<Boat> listView;
    @FXML
    private Label timeZoneLabel;

    private ControllerManager manager;
    private ZoneTimeClock preRaceClock;

    @FXML
    public void initialize() {

    }

    /**
     * Initialises the variables associated with the beginning of the race. Shows the pre-race window for a specific
     * duration before the race starts.
     * @param manager The controller manager for this instance of the program.
     * @param currentTime The current time at the location of the race.
     * @param startTime The official start time of the race.
     * @param duration The length of time for which the controller should display the pre-race window.
     * @param boats The boats participatin gin the race.
     */
    public void setUp(ControllerManager manager, ZonedDateTime currentTime, ZonedDateTime startTime, long duration, List<Boat> boats) {
        this.manager = manager;
        startClock(currentTime, currentTime.getZone());
        displayTimeZone(currentTime);
        startTimeLabel.setText(startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        setUpLists(boats);
        if (duration > 0) {
            Timeline showLive = new Timeline(new KeyFrame(
                    Duration.seconds(duration),
                    event -> {
                        showLiveRaceView();
                    }));

            showLive.setCycleCount(1);
            showLive.play();
        }else{
            showLiveRaceView();
        }
    }


    /**
     * Shows the live race view window.
     */
    public void showLiveRaceView() {
        try {
            manager.showMainView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startClock(ZonedDateTime currentTime, ZoneId timeZone) {
        preRaceClock = new ZoneTimeClock(timeLabel, timeZone, currentTime);
        preRaceClock.start();
    }

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
                    setText(boat.getBoatName());
                }
            }
        });
    }

}
