package seng302.team18.visualiser.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.messageparsing.SocketMessageReceiver;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.visualiser.display.ZoneTimeClock;

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
    @FXML private Text raceNameText;

    private ZoneTimeClock preRaceClock;

    /**
     * Initialises the variables associated with the beginning of the race. Shows the pre-race window for a specific
     * duration before the race starts.
     * @param race The race to be set up in the pre-race.
     */
    public void setUp(Race race) {
        preRaceClock = new ZoneTimeClock(timeLabel, DateTimeFormatter.ofPattern("HH:mm:ss"), race.getStartTime());
        raceNameText.setText(race.getRaceName());
        displayTimeZone(race.getStartTime());
        startTimeLabel.setText(race.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        setUpLists(race.getStartingList());
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
}
