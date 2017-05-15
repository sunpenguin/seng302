package seng302.team18.visualiser.display;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Group;
import javafx.scene.web.WebEngine;
import seng302.team18.model.BoundaryMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.Race;

import java.util.stream.Collectors;

/**
 * Using the geographic course information, requests a map image from the google map API and renders it in the
 * background of the course area.
 */
public class BackgroundRenderer {

    private final WebEngine webEngine;
    private String mapURL = getClass().getResource("/googlemaps.html").toExternalForm();

    private final Race race;
    private Coordinate center;


    /**
     * Constructor for the BackgroundRenderer
     * @param race yes
     * @param webEngine also yes
     */
    public BackgroundRenderer(Race race, WebEngine webEngine) {
        this.race = race;
        center = new Coordinate(0, 0);
        this.webEngine = webEngine;
        webEngine.load(mapURL);
        webEngine.setJavaScriptEnabled(true);
    }

    /**
     * Updates the map shown in the background of the race view, based on the current course.
     *
     * @return true if the map was updated false otherwise.
     */
    public boolean renderBackground() {
        Coordinate newCenter = race.getCourse().getCentralCoordinate();
        if (!center.equals(newCenter)) {
            try {
                final String centerCommand =
                        "setCenter(" + newCenter.getLatitude() + ", " + newCenter.getLongitude() + ")";
                webEngine.executeScript(centerCommand);
                center = newCenter;
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

}
