package seng302.team18.visualiser.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng302.team18.interpreting.CompositeMessageInterpreter;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.*;
import seng302.team18.messageparsing.Receiver;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.visualiser.display.PreRaceTimes;
import seng302.team18.visualiser.messageinterpreting.*;
import seng302.team18.send.Sender;

import java.io.IOException;
import java.net.Socket;
import java.time.ZonedDateTime;

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
    private Text raceNameText;
    @FXML
    private Label ipLabel;
    @FXML
    private Label portLabel;
    @FXML
    private Pane pane;

    private Interpreter interpreter;
    private Sender sender;
    private Race race;

    private boolean hasChanged = false;

    private Stage stage;


    public void initialize() {
    }


    /**
     * Initialises the variables associated with the beginning of the race. Shows the pre-race window for a specific
     * duration before the race starts.
     *
     * @param race     The race to be set up in the pre-race.
     * @param receiver the receiver
     * @param sender   the sender
     */
    public void setUp(Race race, Receiver receiver, Sender sender) {
        this.sender = sender;
        this.race = race;
        raceNameText.setText(race.getRegatta().getName());
        displayTimeZone(race.getStartTime());

        setUpLists();
        listView.setItems(FXCollections.observableList(race.getStartingList()));
        PreRaceTimes preRaceTimes = new PreRaceTimes(startTimeLabel, timeZoneLabel, timeLabel, race);
        preRaceTimes.start();

        Stage stage = (Stage) listView.getScene().getWindow();
        this.interpreter = new Interpreter(receiver);
        interpreter.setInterpreter(initialiseInterpreter());
        showNetWorkInfo();

        stage.setOnCloseRequest((event) -> {
            interpreter.close();
            while (!receiver.close()) {
            }
            System.out.println("shutting down");
            System.exit(0);
        });
    }


    public void initConnection(Color color) {
        interpreter.start();

        RequestType requestType;
        switch (race.getMode()) {
            case RACE:
                requestType = RequestType.RACING;
                break;
            case CONTROLS_TUTORIAL:
                requestType = RequestType.CONTROLS_TUTORIAL;
                break;
            default:
                requestType = RequestType.RACING;
        }
        sender.send(new RequestMessage(requestType));

        MessageInterpreter acceptanceResponse = new ColourResponder(color, sender);
        interpreter.getInterpreter().add(AC35MessageType.ACCEPTANCE.getCode(), acceptanceResponse);

    }


    /**
     * Shows the time zone of the race
     *
     * @param zoneTime USED TO GET THE UTC OFFSET
     */
    private void displayTimeZone(ZonedDateTime zoneTime) {
        timeZoneLabel.setText("UTC " + zoneTime.getOffset().toString());
    }


    /**
     * Sets the list view of the participants in the race.
     */
    private void setUpLists() {
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
     * Set up and initialise interpreter variables, adding interpreters of each relevant type to the global interpreter.
     *
     * @return the message interpreter
     */
    private MessageInterpreter initialiseInterpreter() {
        MessageInterpreter interpreter = new CompositeMessageInterpreter();

        interpreter.add(AC35MessageType.ACCEPTANCE.getCode(), new AcceptanceInterpreter(race));
        interpreter.add(AC35MessageType.XML_RACE.getCode(), new XMLRaceInterpreter(race));
        interpreter.add(AC35MessageType.XML_BOATS.getCode(), new XMLBoatInterpreter(race));
        interpreter.add(AC35MessageType.XML_REGATTA.getCode(), new XMLRegattaInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceStatusInterpreter(this));
        interpreter.add(AC35MessageType.XML_BOATS.getCode(), new BoatListInterpreter(this));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new PreRaceTimeInterpreter(race));

        return interpreter;
    }


    /**
     * Switches from the pre-race screen to the race screen.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public void showRace() throws IOException {
        if (hasChanged) {
            return;
        }
        hasChanged = true;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        Parent root = loader.load(); // throws IOException
        RaceController controller = loader.getController();
        Stage stage = (Stage) raceNameText.getScene().getWindow();
        pane.getScene().setRoot(root);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
        controller.setUp(race, interpreter, sender);
        controller.updateControlsTutorial();
        controller.updateControlsTutorial();
    }


    /**
     * Updates the list of boats.
     */
    public void updateBoatList() {
        listView.setItems(FXCollections.observableList(race.getStartingList()));
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void showNetWorkInfo() {
        Socket socket = interpreter.getSocket();
        ipLabel.setText(socket.getInetAddress().toString());
        portLabel.setText(String.valueOf(socket.getPort()));
    }

}
