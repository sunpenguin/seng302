package seng302.team18.visualiser.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.display.ZoneTimeClock;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by dhl25 on 27/03/17.
 */
public class PreRaceController {
    @FXML
    private Label timeLabel;
    @FXML
    private Label startTimeLabel;
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



    public void setUp(ControllerManager manager, ZonedDateTime currentTime, ZonedDateTime startTime, long duration, List<Boat> boats) {
        this.manager = manager;
        startClock(currentTime, currentTime.getZone());
        displayTimeZone(currentTime);
        startTimeLabel.setText(startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        if (duration > 0) {
            Timeline showLive = new Timeline(new KeyFrame(
                    Duration.seconds(duration),
                    event -> {
                        showLiveRaceView(boats);
                    }));

            showLive.setCycleCount(1);
            showLive.play();
        }else{
            showLiveRaceView(boats);
        }
    }


    public void showLiveRaceView(List<Boat> boats) {
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
//        try {
//            Parent root = loader.load(); // throws IOException
//            Stage stage = (Stage) listView.getScene().getWindow();
//            MainWindowController mainWindowController = loader.getController();
////            mainWindowController.setRace(race);
//            mainWindowController.startRace((long) Race.PREP_TIME_SECONDS);
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            preRaceClock.stop();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            manager.showMainView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setUpLists(boats);
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
