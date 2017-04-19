package seng302.team18.visualiser.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng302.team18.data.AC35MessageParserFactory;
import seng302.team18.data.RaceMessageInterpreter;
import seng302.team18.data.SocketMessageReceiver;
import seng302.team18.model.Race;
import seng302.team18.visualiser.RaceLoop;
import seng302.team18.visualiser.display.RaceRenderer;

import java.io.IOException;
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

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(mainControllerPath));
        Parent root = loader.load(); // throws IOException
        MainWindowController mainWindowController = loader.getController();
        primaryStage.setTitle("RaceVision");
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        mainWindowController.setUp(race, interpreter, receiver);
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
