package seng302.team18.visualiser.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng302.team18.messageparsing.*;
import seng302.team18.visualiser.messageinterpreting.*;
import seng302.team18.model.Race;
import seng302.team18.visualiser.messageinterpreting.RaceClockInterpreter;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Main Controller that manages the other controller classes.
 */
public class ControllerManager {
    private MainWindowController mainController;
    private String mainControllerPath;
    private PreRaceController preRaceController;
    private String preRacePath;
    private Stage primaryStage;

    private SocketMessageReceiver receiver;
    private MessageInterpreter interpreter;
    private Race race;


    public ControllerManager(Stage primaryStage, String mainControllerPath, String preRacePath) {
        this.mainControllerPath = mainControllerPath;
        this.preRacePath = preRacePath;
        this.primaryStage = primaryStage;
    }


    /**
     * initialises the reciever, race and interpreter. Loops through messages recieved via the receiver and interprets
     * them.
     * @throws Exception
     */
    public void start() throws Exception {
        receiver = getPort();
        race = new Race();
        interpreter = new CompositeMessageInterpreter();
        initialiseInterpreter();
        MessageBody message = receiver.nextMessage();
        while(message == null || AC35MessageType.from(message.getType()) != AC35MessageType.RACE_STATUS) {
            interpreter.interpret(message);
            message = receiver.nextMessage();
        }

        AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
        Instant startIn = Instant.ofEpochMilli(statusMessage.getStartTime());
        Instant currentIn = Instant.ofEpochMilli(statusMessage.getCurrentTime());
        ZonedDateTime startTime = ZonedDateTime.ofInstant(startIn, race.getCourse().getTimeZone());
        ZonedDateTime currentTime = ZonedDateTime.ofInstant(currentIn, race.getCourse().getTimeZone());

        if (currentTime.isBefore(startTime.plusSeconds((long) Race.PREP_TIME_SECONDS))) {
            showPreRace(currentTime, startTime, ChronoUnit.SECONDS.between(currentTime, startTime) - (long) Race.PREP_TIME_SECONDS);
        } else {
            showMainView();
        }

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

    }


    /**
     * Set up and intialise interpreter variables, adding interpreters of each relevant type to the glabal interpreter.
     */
    private void initialiseInterpreter() {
        interpreter.add(AC35MessageType.XML_RACE.getCode(), new XMLRaceInterpreter(race));
        interpreter.add(AC35MessageType.XML_BOATS.getCode(), new XMLBoatInterpreter(race));
        interpreter.add(AC35MessageType.XML_REGATTA.getCode(), new XMLRegattaInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceTimeInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new WindDirectionInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new EstimatedTimeInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new FinishersListInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new LegNumberInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatLocationInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new MarkLocationInterpreter(race));
        interpreter.add(AC35MessageType.MARK_ROUNDING.getCode(), new MarkRoundingInterpreter(race));
    }

    /**
     * Sets up the main GUI window, controlled by the main window controller.
     * @throws IOException IO Exception thrown when loading FXML loader
     */
    public void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(mainControllerPath));
        Parent root = loader.load(); // throws IOException
        mainController = loader.getController();
        primaryStage.setTitle("RaceVision");
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        mainController.setUp(race, interpreter, receiver);

        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceClockInterpreter(mainController.getRaceClock()));
    }

    /**
     * Display the window with information about a race which hasn't started yet.
     * @param currentTime The current time at the race location.
     * @param startTime The start time of the race which is being streamed.
     * @param duration The length of time until startTime
     * @throws IOException IO Exception thrown when loading FXML loader
     */
    private void showPreRace(ZonedDateTime currentTime, ZonedDateTime startTime, long duration) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(preRacePath));
        Parent root = loader.load(); // throws IOException
        preRaceController = loader.getController();
        primaryStage.setTitle("RaceVision");
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        preRaceController.setUp(this, currentTime, startTime, duration, race.getStartingList());
    }


    /**
     * Asks the user where they would like their data streamed from. They can use of four options: Test mock, test stream,
     * live stream or they can specify their own custom  port number and address to stream from.
     * @return A port and host combination
     */
    private SocketMessageReceiver getPort() {
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
        return getPort();
    }

    /**
     * A companion method for getPort.
     * Asks the user for thir custom port and address.
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
