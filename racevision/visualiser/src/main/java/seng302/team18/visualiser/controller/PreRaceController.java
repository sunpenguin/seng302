package seng302.team18.visualiser.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.messageparsing.SocketMessageReceiver;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.display.ZoneTimeClock;
import seng302.team18.visualiser.util.Session;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller for the pre race view
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
    @FXML
    private Button liveConnectButton;

    private ZoneTimeClock preRaceClock;

    /**
     * Initialises the variables associated with the beginning of the race. Shows the pre-race window for a specific
     * duration before the race starts.
     * @param startTime The official start time of the race.
     * @param boats The boats participatin gin the race.
     */
    public void setUp(ZonedDateTime startTime, List<Boat> boats) {
        preRaceClock = new ZoneTimeClock(timeLabel, DateTimeFormatter.ofPattern("HH:mm:ss"));
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

    /**
     * Called when the live connection button is selected, sets up a connection with the live AC35 feed
     */
    @FXML
    public void openLiveStream() {
        try {
            Session.getInstance().setReceiver(new SocketMessageReceiver("livedata.americascup.com", 4940, new AC35MessageParserFactory()));
            startConnection();
        } catch (Exception e) {
            System.out.println("Could not establish connection to stream Host/Port");
        }
    }

    /**
     * Called when the test connection button is selected, sets up a connection with the UC test feed
     */
    @FXML
    public void openTestStream() {
        try {
            Session.getInstance().setReceiver(new SocketMessageReceiver("livedata.americascup.com", 4941, new AC35MessageParserFactory()));
            startConnection();
        } catch (Exception e) {
            System.out.println("Could not establish connection to stream Host/Port");
        }
    }

    /**
     * Called when the mock connection button is selected, sets up a connection with the mock feed
     */
    @FXML
    public void openMockStream() {
        try {
            Session.getInstance().setReceiver(new SocketMessageReceiver("127.0.0.1", 5005, new AC35MessageParserFactory()));
            startConnection();
        } catch (Exception e) {
            System.out.println("Could not establish connection to stream Host/Port");
        }
    }

    /**
     * Creates a controller manager object and begins an instance of the program.
     * @throws Exception A connection error
     */
    private  void startConnection() throws Exception {


        Stage s = (Stage) liveConnectButton.getScene().getWindow();
        ControllerManager manager = new ControllerManager(s, "MainWindow.fxml", "PreRace.fxml");
        manager.start();
//        s.close();
    }

    public ZoneTimeClock getClock() {
        return preRaceClock;
    }

}
