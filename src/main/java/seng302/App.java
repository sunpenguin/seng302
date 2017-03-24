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
//        primaryStage.setTitle("Race Vision");
//        primaryStage.setScene(new Scene(root, 1280, 720));
//        primaryStage.setMinHeight(720);
//        primaryStage.setMinWidth(1280);
//        primaryStage.show();


        primaryStage.setTitle("RaceVision");
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
            }
        });

//        System.out.println(Screen.getPrimary().getVisualBounds().getHeight());
//        System.out.println(Screen.getPrimary().getVisualBounds().getWidth());
    }

    public static void main(String[] args) {
        launch(args);
    }
}





