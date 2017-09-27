package seng302.team18.visualiser.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng302.team18.encode.Sender;
import seng302.team18.message.AC35MessageType;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.display.ui.PreRaceTimes;
import seng302.team18.visualiser.interpret.Interpreter;
import seng302.team18.visualiser.interpret.americascup.BoatListInterpreter;
import seng302.team18.visualiser.interpret.americascup.PreRaceTimeInterpreter;
import seng302.team18.visualiser.interpret.americascup.PreRaceToMainRaceInterpreter;
import seng302.team18.visualiser.interpret.xml.XMLBoatInterpreter;
import seng302.team18.visualiser.interpret.xml.XMLRaceInterpreter;
import seng302.team18.visualiser.interpret.xml.XMLRegattaInterpreter;
import seng302.team18.visualiser.sound.ThemeTunePlayer;

import java.io.IOException;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.List;

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
    private Label raceNameText;
    @FXML
    private Label ipLabel;
    @FXML
    private Label portLabel;
    @FXML
    private Pane pane;

    private Interpreter interpreter;
    private Sender sender;
    private ClientRace race;

    private boolean hasChanged = false;

    public void initialize() {
    }


    /**
     * Initialises the variables associated with the beginning of the race. Shows the pre-race window for a specific
     * duration before the race starts.
     *
     * @param race     The race to be set up in the pre-race.
     * @param sender   the sender\
     * @param interpreter the interpreter
     */
    public void setUp(ClientRace race, Sender sender, Interpreter interpreter) {
        this.sender = sender;
        this.race = race;
        System.out.println(race.getRegatta().getName());
        raceNameText.setText(race.getRegatta().getName());
        displayTimeZone(race.getStartTime());

        setUpLists();
        listView.setItems(FXCollections.observableList(race.getStartingList()));
        PreRaceTimes preRaceTimes = new PreRaceTimes(startTimeLabel, timeZoneLabel, timeLabel, race);
        preRaceTimes.start();

        Stage stage = (Stage) listView.getScene().getWindow();
        this.interpreter = interpreter;
        addInterpreters();
        showNetWorkInfo();

        stage.setOnCloseRequest((event) -> {
            interpreter.close();
            while (!interpreter.closeReceiver()) {
            }
            System.out.println("shutting down");
            System.exit(0);
        });
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
    private void addInterpreters() {

        //interpreter.add(AC35MessageType.ACCEPTANCE.getCode(), new AcceptanceInterpreter(race, new GameConnection()));
        interpreter.getInterpreter().add(AC35MessageType.XML_RACE.getCode(), new XMLRaceInterpreter(race));
        interpreter.getInterpreter().add(AC35MessageType.XML_BOATS.getCode(), new XMLBoatInterpreter(race));
        interpreter.getInterpreter().add(AC35MessageType.XML_REGATTA.getCode(), new XMLRegattaInterpreter(race));
        interpreter.getInterpreter().add(AC35MessageType.RACE_STATUS.getCode(), new PreRaceToMainRaceInterpreter(this));
        interpreter.getInterpreter().add(AC35MessageType.XML_BOATS.getCode(), new BoatListInterpreter(this));

        interpreter.getInterpreter().add(AC35MessageType.RACE_STATUS.getCode(), new PreRaceTimeInterpreter(race));

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
        ThemeTunePlayer.stopTrack();
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
    public void updateBoatList(List<Boat> boats) {
        listView.setItems(FXCollections.observableList(boats));
    }


    private void showNetWorkInfo() {
        Socket socket = interpreter.getSocket();
        ipLabel.setText(socket.getInetAddress().toString());
        portLabel.setText(String.valueOf(socket.getPort()));
    }

}
