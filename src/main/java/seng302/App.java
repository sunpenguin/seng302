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

//        mainWindowController.start();
    }

    public static void main(String[] args) {
        double start1Lat =  32.296577;
        double start1Long = -64.854304;

        double start2Lat = 32.293771;
        double start2Long = -64.855242;

        Coordinate start1 = new Coordinate(start1Lat, start1Long);
        Coordinate start2 = new Coordinate(start2Lat, start2Long);

        System.out.println(GPSCalculations.GPSDistance(start1, start2));

        XYPair start1XY = GPSCalculations.GPSxy(start1);
        XYPair start2XY = GPSCalculations.GPSxy(start2);

        double diffX = start1XY.getX() - start2XY.getX();
        double diffY = start1XY.getY() - start2XY.getY();

        System.out.printf("Start1| x:%.2f, y:%.2f\n", start1XY.getX(), start1XY.getY());
        System.out.printf("Start2| x:%.2f, y:%.2f\n", start2XY.getX(), start2XY.getY());
        System.out.printf("Differences| x:%.2f, y:%.2f\n", diffX, diffY);
        System.out.println("--------------------------------------------------------------------------------------'");

        System.out.println(Math.sqrt((diffX * diffX) + (diffY * diffY)));

        launch(args);
    }
}





