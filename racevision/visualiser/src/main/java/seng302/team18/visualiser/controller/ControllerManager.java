package seng302.team18.visualiser.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.SocketMessageReceiver;
import seng302.team18.model.Race;
import seng302.team18.visualiser.messageinterpreting.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The App Controller that manages the other controller classes.
 */
public class ControllerManager {
    private MainWindowController mainController;
    private String mainControllerPath;
    private PreRaceController preRaceController;
    private String preRacePath;
    private Stage primaryStage;
    private ViewType currentView = ViewType.NOT_SET;

    private SocketMessageReceiver receiver;
    private MessageInterpreter interpreter;
    private Race race;


    /**
     * Private enum for keeping track of the current view.
     */
    private enum ViewType {
        PRE_RACE(1), MAIN(2), NOT_SET(3);

        private int code;

        ViewType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public boolean differentView(ViewType newView) {
            return newView.getCode() != this.getCode();
        }
    }


    /**
     * Constructor for ControllerManager
     *
     * @param primaryStage for showing views
     * @param mainControllerPath path to the MainController.fxml
     * @param preRacePath path to PreRace.fxml
     */
    public ControllerManager(Stage primaryStage, String mainControllerPath, String preRacePath) {
        this.mainControllerPath = mainControllerPath;
        this.preRacePath = preRacePath;
        this.primaryStage = primaryStage;
    }


    /**
     * Initialises the receiver, race and interpreter. Loops through messages received via the receiver and interprets
     * them.
     *
     * @throws Exception Exception,
     */
    public void start() throws Exception {
        race = new Race();
        initialiseInterpreter();
        interpretMessages();
    }


    /**
     * starts interpreting messages from the socket.
     */
    private void interpretMessages() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while(true) {
                MessageBody messageBody;
                try {
                    messageBody = receiver.nextMessage();
//                    Thread.sleep(10); // Uncomment and set it when using csse streams
                } catch (Exception e) {
                    return; // ignore if anything goes wrong
                }
                interpreter.interpret(messageBody);
            }
        });

        primaryStage.setOnCloseRequest((event) -> {
            executor.shutdownNow();
            while (!receiver.close()) {}
        });
    }


    /**
     * Set up and initialise interpreter variables, adding interpreters of each relevant type to the global interpreter.
     */
    private void initialiseInterpreter() {
        interpreter = new CompositeMessageInterpreter();

        interpreter.add(AC35MessageType.XML_RACE.getCode(), new XMLRaceInterpreter(race));
        interpreter.add(AC35MessageType.XML_BOATS.getCode(), new XMLBoatInterpreter(race));
        interpreter.add(AC35MessageType.XML_REGATTA.getCode(), new XMLRegattaInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceTimeInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new WindDirectionInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new EstimatedTimeInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new FinishersListInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new BoatStatusInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatLocationInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new MarkLocationInterpreter(race));
        interpreter.add(AC35MessageType.MARK_ROUNDING.getCode(), new MarkRoundingInterpreter(race));
        //interpreter.add(AC35MessageType.REGISTRATION.getCode(), new RegistrationInterpreter(race));

        interpreter.add(AC35MessageType.BOAT_ACTION.getCode(), new BoatActionInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceStatusInterpreter(this));
    }


    /**
     * Sets up the main GUI window, controlled by the main window controller.
     *
     * @throws IOException IO Exception thrown when loading FXML loader
     */
    public void showMainView() throws IOException {
        if (currentView.differentView(ViewType.MAIN)) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(mainControllerPath));
            Parent root = loader.load(); // throws IOException
            mainController = loader.getController();
            primaryStage.setTitle("RaceVision");
            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setResizable(true);
            primaryStage.setMinHeight(700);
            primaryStage.setMinWidth(1000);
            primaryStage.setScene(scene);
            primaryStage.show();
            mainController.setUp(race);
            interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceClockInterpreter(mainController.getRaceClock()));
            currentView = ViewType.MAIN;
        }
    }


    /**
     * Display the window with information about a race which hasn't started yet.
     *
     * @throws IOException IO Exception thrown when loading FXML loader
     */
    public void showPreRace() throws IOException {
        if (currentView.differentView(ViewType.PRE_RACE)) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(preRacePath));
            Parent root = loader.load(); // throws IOException
            preRaceController = loader.getController();
            primaryStage.setTitle("RaceVision");
            Scene scene = new Scene(root, 777, 578);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            preRaceController.setUp(race);
            currentView = ViewType.PRE_RACE;
        }
    }


    /**
     * Sets the socket to receive from
     * @param socketMessageReceiver socket to receive from
     */
    public void setReceiver(SocketMessageReceiver socketMessageReceiver) {
        receiver = socketMessageReceiver;
    }
}
