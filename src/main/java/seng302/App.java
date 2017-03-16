package seng302;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng302.controller.MainWindowController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        Parent root = loader.load(); // throws IOException
        MainWindowController mainWindowController = loader.getController();
        primaryStage.setTitle("Race Vision");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1280);
        primaryStage.show();

        mainWindowController.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}





