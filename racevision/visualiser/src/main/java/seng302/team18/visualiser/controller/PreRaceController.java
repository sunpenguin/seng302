package seng302.team18.visualiser.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng302.team18.interpreting.CompositeMessageInterpreter;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.messageparsing.Receiver;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.visualiser.display.ZoneTimeClock;
import seng302.team18.visualiser.messageinterpreting.*;
import seng302.team18.visualiser.send.Sender;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private Receiver receiver;
    private Sender sender;
    private Race race;
    private ExecutorService executor;

    /**
     * Initialises the variables associated with the beginning of the race. Shows the pre-race window for a specific
     * duration before the race starts.
     * @param race The race to be set up in the pre-race.
     */
    public void setUp(Race race, Receiver receiver, Sender sender) {
        this.receiver = receiver;
        this.sender = sender;
        this.race = race;
        preRaceClock = new ZoneTimeClock(timeLabel, DateTimeFormatter.ofPattern("HH:mm:ss"), race.getCurrentTime());
        raceNameText.setText(race.getName());
        displayTimeZone(race.getStartTime());
        startTimeLabel.setText(race.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        setUpLists();
        listView.setItems(FXCollections.observableList(race.getStartingList()));
        preRaceClock.start();

        interpretMessages(initialiseInterpreter());
        sender.send(new RequestMessage(true));
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
     * starts interpreting messages from the socket.
     */
    private void interpretMessages(MessageInterpreter interpreter) {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while(true) {
                MessageBody messageBody = null;
                try {
//                    System.out.println(1336);
                    messageBody = receiver.nextMessage();
//                    System.out.println(1337);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                interpreter.interpret(messageBody);
            }
        });

        Stage stage = (Stage) listView.getScene().getWindow();
        stage.setOnCloseRequest((event) -> {
            executor.shutdownNow();
            while (!receiver.close()) {}
        });
    }


    /**
     * Set up and initialise interpreter variables, adding interpreters of each relevant type to the global interpreter.
     */
    private MessageInterpreter initialiseInterpreter() {
        MessageInterpreter interpreter = new CompositeMessageInterpreter();

//        interpreter.add(AC35MessageType.XML_RACE.getCode(), new XMLRaceInterpreter(race));
//        interpreter.add(AC35MessageType.XML_BOATS.getCode(), new XMLBoatInterpreter(race));
//        interpreter.add(AC35MessageType.XML_REGATTA.getCode(), new XMLRegattaInterpreter(race));
//        interpreter.add(AC35MessageType.ACCEPTANCE.getCode(), new AcceptanceInterpreter(race));
//        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceStatusInterpreter(this));
//        interpreter.add(AC35MessageType.XML_BOATS.getCode(), new BoatListInterpreter(this));

        return interpreter;
    }


    /**
     * Switches from the pre-race screen to the race screen.
     *
     * @throws IOException
     */
    public void showRace() throws IOException {
        executor.shutdownNow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        Parent root = loader.load(); // throws IOException
        RaceController controller = loader.getController();
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.setTitle("RaceVision");
        Scene scene = new Scene(root, 1280, 720);
        stage.setResizable(true);
        stage.setMinHeight(700);
        stage.setMinWidth(1000);
        stage.setScene(scene);
        stage.show();
        controller.setUp(race, receiver, sender);
    }


    /**
     * Updates the list of boats.
     */
    public void updateBoatList() {
        listView.setItems(FXCollections.observableList(race.getStartingList()));
    }

}
