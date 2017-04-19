package seng302.team18.visualiser.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng302.team18.data.*;
import seng302.team18.model.Race;
import seng302.team18.visualiser.RaceLoop;
import seng302.team18.visualiser.display.RaceRenderer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

/**
 * Created by dhl25 on 17/04/17.
 */
public class ControllerManager {
    private MainWindowController mainController;
    private String mainControllerPath;
    private PreRaceController preRaceController;
    private String preRacePath;
    private Stage primaryStage;

    private SocketMessageReceiver receiver;
    private RaceMessageInterpreter interpreter;
    private Race race;



    public ControllerManager(Stage primaryStage, String mainControllerPath, String preRacePath) {
        this.mainControllerPath = mainControllerPath;
        this.preRacePath = preRacePath;
        this.primaryStage = primaryStage;
    }


    public void start() throws Exception {

        receiver = getPort();
        race = new Race();
        interpreter = new RaceMessageInterpreter(race);
        MessageBody message = receiver.nextMessage();
        while(message == null || AC35MessageType.from(message.getType()) != AC35MessageType.RACE_STATUS) {
            interpreter.interpretMessage(message);
            message = receiver.nextMessage();
        }
        AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
        Instant startIn = Instant.ofEpochMilli(statusMessage.getStartTime());
        Instant currentIn = Instant.ofEpochMilli(statusMessage.getCurrentTime());
        ZonedDateTime startTime = ZonedDateTime.ofInstant(startIn, race.getCourse().getTimeZone());
        ZonedDateTime currentTime = ZonedDateTime.ofInstant(currentIn, race.getCourse().getTimeZone());
        if (currentTime.isBefore(startTime.plusSeconds((long) Race.PREP_TIME_SECONDS))) {
            showPreRace(currentTime, ChronoUnit.SECONDS.between(currentTime, startTime) - (long) Race.PREP_TIME_SECONDS);
        } else {
            showMainView();
        }



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
    }


    private void showPreRace(ZonedDateTime currentTime, long duration) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(preRacePath));
        Parent root = loader.load(); // throws IOException
        preRaceController = loader.getController();
        primaryStage.setTitle("RaceVision");
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        preRaceController.setUp(this, currentTime, duration, race.getStartingList());
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
                SocketMessageReceiver s = new SocketMessageReceiver(4941, new AC35MessageParserFactory());
                return s;

            } catch (IOException e) {
                System.out.println("try again");
            }
        }
        return getPort();
    }
}
