package seng302.team18.visualiser.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xml.sax.SAXException;
import seng302.team18.data.XMLBoatParser;
import seng302.team18.data.XMLCourseParser;
import seng302.team18.model.Boat;
import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.visualiser.display.ZoneTimeClock;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by dhl25 on 27/03/17.
 */
public class PreRaceController {
    @FXML
    private Label timeLabel;
    @FXML
    private ListView<Boat> listView;
    @FXML
    private Label timeZoneLabel;

    private ControllerManager manager;
    private ZoneTimeClock preRaceClock;

    @FXML
    public void initialize() {

//        Timeline showLive = new Timeline(new KeyFrame(
////                Duration.seconds(Race.WARNING_TIME_SECONDS / timeScaleFactor),
//                Duration.seconds(Race.WARNING_TIME_SECONDS),
//                event -> showLiveRaceView()));
//        showLive.setCycleCount(1);
//        showLive.play();
//
//        setUpList();
//        startClock();
//        displayTimeZone();
    }



    public void setUp(ControllerManager manager, ZonedDateTime currentTime, long duration, List<Boat> boats) {
        this.manager = manager;
        startClock(currentTime, currentTime.getZone());
        displayTimeZone(currentTime);
        Timeline showLive = new Timeline(new KeyFrame(
                Duration.seconds(duration),
                event -> {
                    try {
                        manager.showMainView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
        showLive.setCycleCount(1);
        showLive.play();
        setUpLists(boats);
    }

    public void showLiveRaceView() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        try {
            Parent root = loader.load(); // throws IOException
            Stage stage = (Stage) listView.getScene().getWindow();
            MainWindowController mainWindowController = loader.getController();
//            mainWindowController.setRace(race);
            mainWindowController.startRace((long) Race.PREP_TIME_SECONDS);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            preRaceClock.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startClock(ZonedDateTime currentTime, ZoneId timeZone) {
        final double KMPH_TO_MPS = 1000.0 / 3600.0;
//        double timeScaleFactor = race.getCourse().getCourseDistance()
//                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
//        preRaceClock = new ZoneTimeClock(timeLabel, timeScaleFactor, race.getCourse().getTimeZone());
        preRaceClock = new ZoneTimeClock(timeLabel, timeZone, currentTime);
        preRaceClock.start();
    }

    private void displayTimeZone(ZonedDateTime zoneTime) {
        final int MILLI_TO_HOUR = 3600000;
        final int MILLI_TO_MINUTE = 60000;
        final int SCALER_FOR_MINUTE = 60;
//        TimeZone timeZone = TimeZone.getTimeZone(race.getCourse().getTimeZone());
//        Calendar cal = GregorianCalendar.getInstance(timeZone);
//        int offsetInMillis = timeZone.getOffset(cal.getTimeInMillis());
//        String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / MILLI_TO_HOUR), Math.abs((offsetInMillis / MILLI_TO_MINUTE) % SCALER_FOR_MINUTE));
//        offset = (offsetInMillis >= 0 ? "+" : "-") + offset;
        timeZoneLabel.setText("UTC " + zoneTime.getOffset().toString());
    }

    private void setUpLists(List<Boat> boats) {
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

}
