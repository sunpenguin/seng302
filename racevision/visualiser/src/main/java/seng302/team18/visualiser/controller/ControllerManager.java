package seng302.team18.visualiser.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.*;
import seng302.team18.visualiser.messageinterpreting.*;
import seng302.team18.model.Race;
import seng302.team18.visualiser.messageinterpreting.RaceClockInterpreter;

import java.io.IOException;
import java.util.*;
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

        private ViewType(int code) {
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
     * @throws Exception
     */
    public void start() throws Exception {
        receiver = getSocket();
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
     * Set up and initialise interpreter variables, adding interpreters of each relevant type to the glabal interpreter.
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

        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceStatusInterpreter(this));
    }



    /**
     * Sets up the main GUI window, controlled by the main window controller.
     * @throws IOException IO Exception thrown when loading FXML loader
     */
    public void showMainView() throws IOException {
        if (currentView.differentView(ViewType.MAIN)) {
            currentView = ViewType.MAIN;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(mainControllerPath));
            Parent root = loader.load(); // throws IOException
            mainController = loader.getController();
            primaryStage.setTitle("RaceVision");
            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();

            mainController.setUp(race);

            interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceClockInterpreter(mainController.getRaceClock()));
        }
    }

    /**
     * Display the window with information about a race which hasn't started yet.
     * @throws IOException IO Exception thrown when loading FXML loader
     */
    public void showPreRace() throws IOException {
        if (currentView.differentView(ViewType.PRE_RACE)) {
            currentView = ViewType.PRE_RACE;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(preRacePath));
            Parent root = loader.load(); // throws IOException
            preRaceController = loader.getController();
            primaryStage.setTitle("RaceVision");
            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();

            preRaceController.setUp(race.getCurrentTime(), race.getStartTime(), race.getStartingList());
        }
    }


    /**
     * Asks the user where they would like their data streamed from. They can use of four options: Test mock, test stream,
     * live stream or they can specify their own custom  port number and address to stream from.
     * @return A port and host combination
     */
    private SocketMessageReceiver getSocket() {
        Scanner scanner = new Scanner(System.in);
        String decision = "";
        List<String> validChoices = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
        while (!validChoices.contains(decision)) {
            System.out.println("What race type do you want?");
            System.out.println("1: TestMock, 2: TestStream, 3: LiveStream, 4: Custom Host");
            if (scanner.hasNext()) {
                decision = scanner.next();
            }
        }

        if (decision.equals("1")){
            try {
                return new SocketMessageReceiver("127.0.0.1", 5005, new AC35MessageParserFactory());
            } catch (IOException e) {
                System.out.println("Could not establish connection to mock Host/Port");
            }
        } else if (decision.equals("2")) {
            try {
                return new SocketMessageReceiver("livedata.americascup.com", 4941, new AC35MessageParserFactory());
            } catch (IOException e) {
                System.out.println("Could not establish connection to test Host/Port");
            }
        } else if (decision.equals("3")) {
            try {
                return new SocketMessageReceiver("livedata.americascup.com", 4940, new AC35MessageParserFactory());
            } catch (IOException e) {
                System.out.println("Could not establish connection to stream Host/Port");
            }
        }else if (decision.equals("4")) {
            try {
                List<String> portAndHost = getCustomConnection();
                return new SocketMessageReceiver(portAndHost.get(1), Integer.parseInt(portAndHost.get(0)), new AC35MessageParserFactory());
            }catch (Exception e) {
                System.out.println("Could not establish connection to stream Host/Port");
            }
        }
        return getSocket();
    }

    /**
     * A companion method for getSocket.
     * Asks the user for the custom port and address.
     * @return A port and host combination
     */
    private List<String> getCustomConnection() {
        List<String> portAndHost = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a port number: ");
        while (portAndHost.isEmpty()) {
            if (scanner.hasNext()) {
               portAndHost.add(scanner.next());
            }
        }
        System.out.println("Please enter a host name: ");
        while (portAndHost.size() == 1) {
            if (scanner.hasNext()) {
                portAndHost.add(scanner.next());
            }
        }
        return portAndHost;
    }
}
