package seng302.team18.visualiser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import seng302.team18.visualiser.controller.TitleScreenController;
import seng302.team18.visualiser.sound.SoundEffectPlayer;

import java.io.File;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));
        Parent root = loader.load(); // throws IOException
        TitleScreenController controller = loader.getController();
        controller.setStage(primaryStage);
        controller.setup();
        controller.setSoundPlayer(new SoundEffectPlayer());
        controller.reDraw();
        primaryStage.setTitle("High Seas");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
