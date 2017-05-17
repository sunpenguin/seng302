package seng302.team18.visualiser;

import javafx.application.Application;
import javafx.stage.Stage;
import seng302.team18.visualiser.controller.ControllerManager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ControllerManager manager = new ControllerManager(primaryStage, "MainWindow.fxml", "PreRace.fxml");
        manager.start();
    }

    public static void main(String[] args) {
        launch(args);
//        List<String> list = Arrays.asList("1", "2", "3");
//        list.sort(Comparator.naturalOrder());
//        System.out.println(list);
    }
}
