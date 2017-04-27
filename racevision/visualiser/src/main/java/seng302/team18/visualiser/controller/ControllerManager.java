package seng302.team18.visualiser.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng302.team18.messageparsing.*;
//import seng302.team18.visualiser.display.RaceClockInterpreter;
import seng302.team18.messageinterpreting.*;
import seng302.team18.model.Race;
import seng302.team18.visualiser.display.RaceClockInterpreter;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Main Controller that manages the other controller classes.
 * Created by dhl25 on 17/04/17.
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


    public void start() throws Exception {
        receiver = getPort();
        race = new Race();
        interpreter = new RaceMessageInterpreter();
        setHashMap();
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


    private void setHashMap() {
        interpreter.add(AC35MessageType.XML_RACE.getCode(), new XMLRaceInterpreter(race));
        interpreter.add(AC35MessageType.XML_BOATS.getCode(), new XMLBoatInterpreter(race));
        interpreter.add(AC35MessageType.XML_REGATTA.getCode(), new XMLRegattaInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceTimeInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new WindDirectionInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new EstimatedTimeInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new FinishersListInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatLocationInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new MarkLocationInterpreter(race));
        interpreter.add(AC35MessageType.MARK_ROUNDING.getCode(), new MarkRoundingInterpreter(race));
    }


    public void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(mainControllerPath));
        Parent root = loader.load(); // throws IOException
        mainController = loader.getController();
        primaryStage.setTitle("RaceVision");
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        mainController.setUp(race, interpreter, receiver);

        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceClockInterpreter(mainController.getRaceClock(), race.getCourse().getTimeZone()));
    }


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


    private SocketMessageReceiver getPort() {
        Scanner scanner = new Scanner(System.in);
        String decision = "";
        while (!decision.toUpperCase().equals("Y") && !decision.toUpperCase().equals("N")) {
            System.out.println("Would you like a live race? (Y/N)");
            if (scanner.hasNext()) {
                decision = scanner.next().toUpperCase();
            } else {
                scanner.next();
            }
        }

        if (decision.equals("Y")){
            try {
                SocketMessageReceiver receiver = new SocketMessageReceiver(4941, new AC35MessageParserFactory());
                return receiver;
            } catch (IOException e) {
                System.out.println("try again");
            }
        }
        return getPort();
    }
}
