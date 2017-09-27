package seng302.team18.visualiser.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
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
import seng302.team18.visualiser.sound.AudioPlayer;
import seng302.team18.visualiser.sound.StartSoundPlayer;

import java.io.IOException;
import java.net.Socket;
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

    private AudioPlayer audioPlayer;

    private boolean hasChanged = false;


    /**
     * Initialises the variables associated with the beginning of the race. Shows the pre-race window for a specific
     * duration before the race starts.
     *
     * @param race        The race to be set up in the pre-race.
     * @param sender      the sender
     * @param interpreter the interpreter
     * @param player      manages the audio playback from this scene
     */
    public void setUp(ClientRace race, Sender sender, Interpreter interpreter, AudioPlayer player) {
        this.sender = sender;
        this.race = race;
        this.interpreter = interpreter;
        this.audioPlayer = player;

        addInterpreters();
        setUpStartSound();
        setUpLists();

        listView.setItems(FXCollections.observableList(race.getStartingList()));
        PreRaceTimes preRaceTimes = new PreRaceTimes(startTimeLabel, timeLabel, race);
        preRaceTimes.start();

        Stage stage = (Stage) listView.getScene().getWindow();

        showNetworkInfo();

        raceNameText.setText(race.getMode().toString());
        raceNameText.setStyle("-fx-font-size: 42pt");

        stage.setOnCloseRequest((event) -> {
            interpreter.close();
            while (!interpreter.closeReceiver()) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // pass
                }
            }
            System.out.println("shutting down");
            System.exit(0);
        });
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
     */
    private void addInterpreters() {
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
        hasChanged = true;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        Parent root = loader.load(); // throws IOException
        RaceController controller = loader.getController();
        Stage stage = (Stage) raceNameText.getScene().getWindow();
        pane.getScene().setRoot(root);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
        controller.setUp(race, interpreter, sender, audioPlayer);
        controller.updateControlsTutorial();
        controller.updateControlsTutorial();
    }


    /**
     * Updates the list of boats.
     */
    public void updateBoatList(List<Boat> boats) {
        listView.setItems(FXCollections.observableList(boats));
    }


    private void showNetworkInfo() {
        Socket socket = interpreter.getSocket();
        ipLabel.setText(socket.getInetAddress().toString());
        portLabel.setText(String.valueOf(socket.getPort()));
    }


    /**
     * Plays the start sound at the appropriate time
     */
    private void setUpStartSound() {
        new Thread(new StartSoundPlayer(race, audioPlayer)).start();
    }


}
