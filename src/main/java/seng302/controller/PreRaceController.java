package seng302.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xml.sax.SAXException;
import seng302.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dhl25 on 27/03/17.
 */
public class PreRaceController {
    @FXML
    private Label timeLabel;
    @FXML
    private ListView<Boat> listView;
//    private List<Boat> boats;
    private final int SECONDS_TIL_PREPARATORY_SIGNAL = 5; // TODO change this to 60 later
    private ZonedDateTime zonedDateTime;
    private Race race;


    @FXML
    public void initialize() {
        Timeline showLive = new Timeline(new KeyFrame(
                Duration.seconds(SECONDS_TIL_PREPARATORY_SIGNAL),
                event -> showLiveRaceView()));
        showLive.setCycleCount(1);
        showLive.play();
        try {
            List<Boat> boats = XMLParser.parseBoats(new File("src/main/resources/boats.xml")); // throws exceptions
            Course course = XMLParser.parseCourse(new File("src/main/resources/course.xml")); // throws exceptions
            race = new Race(boats, course);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Course / Boat XML file.");
            alert.setHeaderText("Invalid Course / Boat XML file.");
            alert.setContentText("Invalid Course / Boat XML file.");
            alert.showAndWait();
        }


        setUpList();
        startClock();
    }


    public void setUpList() {
        listView.setItems(FXCollections.observableList(race.getStartingList()));
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


    private void startClock() {
//        final long UPDATE_PERIOD_NANO = 100000000L;
//        final double NANO_TO_SECONDS = 1e-9;
//        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        ZoneId zoneId = ZoneId.of("Atlantic/Bermuda");
//        zonedDateTime = ZonedDateTime.now(zoneId);
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.millis(TimeUnit.MILLISECONDS.convert(UPDATE_PERIOD_NANO, TimeUnit.NANOSECONDS)),
//                        actionEvent -> {
//                            zonedDateTime = zonedDateTime.plusNanos(UPDATE_PERIOD_NANO);
//                            timeLabel.setText(zonedDateTime.format(timeFormatter));
//                        }
//                ));
//        final int cycles = (int) (SECONDS_TIL_PREPARATORY_SIGNAL / (UPDATE_PERIOD_NANO * NANO_TO_SECONDS));
//        timeline.setCycleCount(cycles);
//        timeline.play();
        final double KMPH_TO_MPS = 1000.0 / 3600.0;
        double timeScaleFactor = race.getCourse().getCourseDistance()
                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
        ZoneTimeClock preRaceClock = new ZoneTimeClock(timeLabel, timeScaleFactor, race.getCourse().getTimeZone());
        preRaceClock.start();
    }

}
