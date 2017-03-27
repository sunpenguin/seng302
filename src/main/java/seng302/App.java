package seng302;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import seng302.controller.MainWindowController;
import java.io.IOException;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        Parent root = loader.load(); // throws IOException
        MainWindowController mainWindowController = loader.getController();

        primaryStage.setTitle("RaceVision");
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}





