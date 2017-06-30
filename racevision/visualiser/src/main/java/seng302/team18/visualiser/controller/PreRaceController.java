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
    @FXML private Text errorText;
    @FXML private TextField customPortField;
    @FXML private TextField customHostField;

    private ZoneTimeClock preRaceClock;

    @FXML
    public void initialise() {}

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

    /**
     * Called when the live connection button is selected, sets up a connection with the live AC35 feed
     */
    @FXML
    public void openLiveStream() {
        openStream("livedata.americascup.com", 4940);
    }

    /**
     * Called when the test connection button is selected, sets up a connection with the UC test feed
     */
    @FXML
    public void openTestStream() {
        openStream("livedata.americascup.com", 4941);
    }

    /**
     * Called when the mock connection button is selected, sets up a connection with the mock feed
     */
    @FXML
    public void openMockStream() {
        System.out.println(123);
        openStream("127.0.0.1", 5005);
    }

    @FXML
    public void openCustomStream() {
        String host = customHostField.getText();
        String portString = customPortField.getText();

        if (host.isEmpty() || portString.isEmpty()) {
            errorText.setText("Please enter a custom host and port");
            return;
        }

        try {
            int port = Integer.parseInt(portString);
            openStream(host, port);
        } catch (NumberFormatException e) {
            errorText.setText("Please enter a valid port number");
            return;
        }
    }

    private void openStream(String host, int port) {
        try {
            startConnection(new SocketMessageReceiver(host, port, new AC35MessageParserFactory()));
        } catch (Exception e) {
            errorText.setText(String.format("Could not establish connection to stream at: %s:%d", host, port));
        }
    }

    /**
     * Creates a controller manager object and begins an instance of the program.
     * @throws Exception A connection error
     */
    private  void startConnection(SocketMessageReceiver receiver) throws Exception {

        Stage s = (Stage) errorText.getScene().getWindow();
        ControllerManager manager = new ControllerManager(s, "MainWindow.fxml", "PreRace.fxml");
        manager.setReceiver(receiver);
        manager.start();
        System.out.println(1);
    }

    public ZoneTimeClock getClock() {
        return preRaceClock;
    }

}
