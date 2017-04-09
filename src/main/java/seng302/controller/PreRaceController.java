package seng302.controller;

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
import seng302.data.LiveDataListener;
import seng302.display.ZoneTimeClock;
import seng302.model.Boat;
import seng302.model.Course;
import seng302.model.Race;
import seng302.data.XMLBoatParser;
import seng302.data.XMLCourseParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
    private List<Boat> boats;
    private ZonedDateTime zonedDateTime;
    @FXML
    private Label timeZoneLabel;
    private Course course;

    private Race race;
    private ZoneTimeClock preRaceClock;

    @FXML
    public void initialize() {
        try {
            List<Boat> boats = XMLBoatParser.parseBoats(getClass().getResourceAsStream("/boats.xml")); // throws exceptions
            Course course = XMLCourseParser.parseCourse(getClass().getResourceAsStream("/course.xml")); // throws exceptions


            race = new Race(boats, course);

            getDurationInput();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Course / Boat XML file.");
            alert.setHeaderText("Invalid Course / Boat XML file.");
            alert.setContentText("Invalid Course / Boat XML file.");
            alert.showAndWait();
        }

        final double KMPH_TO_MPS = 1000.0 / 3600.0;
        double timeScaleFactor = race.getCourse().getCourseDistance()
                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();

        Timeline showLive = new Timeline(new KeyFrame(
                Duration.seconds(Race.WARNING_TIME_SECONDS / timeScaleFactor),
                event -> showLiveRaceView()));
        showLive.setCycleCount(1);
        showLive.play();

        setUpList();
        startClock();
        displayTimeZone();
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
            MainWindowController mainWindowController = loader.getController();
            mainWindowController.setRace(race);
            mainWindowController.startRace((long) Race.PREP_TIME_SECONDS);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            preRaceClock.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startClock() {
        final double KMPH_TO_MPS = 1000.0 / 3600.0;
        double timeScaleFactor = race.getCourse().getCourseDistance()
                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
        preRaceClock = new ZoneTimeClock(timeLabel, timeScaleFactor, race.getCourse().getTimeZone());
        preRaceClock.start();
    }

    private void displayTimeZone() {
        final int MILLI_TO_HOUR = 3600000;
        final int MILLI_TO_MINUTE = 60000;
        final int SCALER_FOR_MINUTE = 60;
        TimeZone timeZone = TimeZone.getTimeZone(race.getCourse().getTimeZone());
        Calendar cal = GregorianCalendar.getInstance(timeZone);
        int offsetInMillis = timeZone.getOffset(cal.getTimeInMillis());
        String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / MILLI_TO_HOUR), Math.abs((offsetInMillis / MILLI_TO_MINUTE) % SCALER_FOR_MINUTE));
        offset = (offsetInMillis >= 0 ? "+" : "-") + offset;
        timeZoneLabel.setText("UTC: " + offset);
    }


    private void getDurationInput() {

        Scanner scanner = new Scanner(System.in);
        int duration = 0;
        while (duration != 1 && duration != 5) {
            System.out.println("Choose how long the race lasts (1 minute or 5 minutes)");
            if (scanner.hasNextInt()) {
                duration = scanner.nextInt();
                race.setDuration(duration * 60); // convert from minutes to seconds
            } else {
                scanner.next();
            }
        }

        String decision = "";
        while (!decision.toUpperCase().equals("Y") && !decision.toUpperCase().equals("N")) {
            System.out.println("Would you like a live race? (Y/N)");
            if (scanner.hasNext()) {
                decision = scanner.next().toUpperCase();
            } else {
                scanner.next();
            }
        }
        System.out.println(decision);

        // TODO move to a better place.

        if (decision.equals("Y")){
            LiveDataListener l = new LiveDataListener(4941);
        }
    }

}
