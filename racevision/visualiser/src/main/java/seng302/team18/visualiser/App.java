package seng302.team18.visualiser;

import javafx.application.Application;
import javafx.stage.Stage;
import seng302.team18.visualiser.controller.ControllerManager;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ControllerManager manager = new ControllerManager(primaryStage, "MainWindow.fxml", "PreRace.fxml");
        manager.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
