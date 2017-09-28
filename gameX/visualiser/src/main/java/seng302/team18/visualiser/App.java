package seng302.team18.visualiser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng302.team18.util.NautiLogger;
import seng302.team18.visualiser.controller.TitleScreenController;
import seng302.team18.visualiser.sound.AudioPlayer;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));
        Parent root = loader.load(); // throws IOException
        TitleScreenController controller = loader.getController();
        controller.setup(primaryStage, new AudioPlayer());
        controller.reDraw();
        primaryStage.setTitle("High Seas");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest((event) -> System.exit(0));
        primaryStage.show();

    }


    public static void main(String[] args) {
        NautiLogger.setDefaultOutput();
        launch(args);
    }

}
