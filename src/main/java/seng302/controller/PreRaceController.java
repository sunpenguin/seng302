package seng302.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xml.sax.SAXException;
import seng302.Boat;
import seng302.XMLParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by dhl25 on 27/03/17.
 */
public class PreRaceController {

    @FXML
    private ListView<Boat> listView;
    private List<Boat> boats;
    private final int MINUTES_UNTIL_PREPARATORY_SIGNAL = 1;

    @FXML
    public void initialize() throws IOException, SAXException, ParserConfigurationException {
        new Timeline(new KeyFrame(Duration.seconds(5), event -> showLiveRaceView())).play();
        // TODO comment the line above and uncomment the line below for actual thing
//        new Timeline(new KeyFrame(
//                Duration.minutes(MINUTES_UNTIL_PREPARATORY_SIGNAL),
//                event -> showLiveRaceView()))
//                .play();
        boats = XMLParser.parseBoats(new File("src/main/resources/boats.xml")); // throws exceptions
        setUpList();
    }


    public void setUpList() {
        listView.setItems(FXCollections.observableList(boats));
        listView.setCellFactory(param -> new ListCell<Boat>() {
            @Override
            protected void updateItem(Boat boat, boolean empty) {
                super.updateItem(boat, empty);
                if (empty || boat == null) {
                    setText(null);
                } else {
                    setText(boat.getBoatName());
                }
            }
        });
    }

    public void showLiveRaceView() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        try {
            Parent root = loader.load(); // throws IOException
            Stage stage = (Stage) listView.getScene().getWindow();
//            MainWindowController mainWindowController = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}