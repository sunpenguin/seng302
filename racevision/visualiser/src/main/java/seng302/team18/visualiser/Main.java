package seng302.team18.visualiser;

import javafx.application.Application;
import javafx.stage.Stage;
import seng302.team18.visualiser.controller.ControllerManager;


/**
 * Created by afj19 on 11/04/17.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ControllerManager manager = new ControllerManager(primaryStage, "MainWindow.fxml", "PreRace.fxml");
        manager.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
