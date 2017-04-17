package seng302.team18.visualiser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng302.team18.visualiser.controller.ControllerManager;
import seng302.team18.visualiser.controller.MainWindowController;
import seng302.team18.visualiser.controller.PreRaceController;

import java.io.IOException;
import java.time.ZoneId;

/**
 * Created by afj19 on 11/04/17.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        Parent root = loader.load(); // throws IOException
        MainWindowController mainWindowController = loader.getController();
//        PreRaceController preRaceController = loader.getController();
        primaryStage.setTitle("RaceVision");
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
//        ControllerManager manager = new ControllerManager("MainWindow.fxml");
        launch(args);
    }
}
