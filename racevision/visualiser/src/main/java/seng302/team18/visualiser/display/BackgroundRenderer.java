package seng302.team18.visualiser.display;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Group;
import javafx.scene.web.WebEngine;
import seng302.team18.model.*;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.ArrayList;
import java.util.Arrays;
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
    private PixelMapper pixelMapper;
    private Coordinate NW = new Coordinate(0, 0);
    private Coordinate SE = new Coordinate(0, 0);

    /*
        These properties hold the current northern and southern latitudes
        as well as eastern and western longitudes of what the map displays
        at any given time
    */
    private DoubleProperty n = new SimpleDoubleProperty(0);
    private DoubleProperty s = new SimpleDoubleProperty(0);
    private DoubleProperty e = new SimpleDoubleProperty(0);
    private DoubleProperty w = new SimpleDoubleProperty(0);

    /**
     * Constructor for the BackgroundRenderer
     * @param race The race
     * @param webEngine The WebEngine
     */
    public BackgroundRenderer(PixelMapper pixelMapper, Race race, WebEngine webEngine) {
        this.pixelMapper = pixelMapper;
        this.race = race;
        center = new Coordinate(0, 0);
        this.webEngine = webEngine;
        webEngine.load(mapURL);
        webEngine.setJavaScriptEnabled(true);
    }

    /**
     * Updates the map shown in the background of the race view, based on the current course.
     * Also sets the values for the coordinates which are currently the north west and south
     * east bounds of what the map displays at any given time
     *
     * @return true if the map was updated false otherwise.
     */
    public boolean renderBackground() {
        Coordinate newCenter = race.getCourse().getCentralCoordinate();

        try {
            n.set((double) webEngine.executeScript("getN();"));
            s.set((double) webEngine.executeScript("getS();"));
            e.set((double) webEngine.executeScript("getE();"));
            w.set((double) webEngine.executeScript("getW();"));
            NW.setLatitude(n.getValue());
            NW.setLongitude(w.getValue());
            SE.setLatitude(s.getValue());
            SE.setLongitude(e.getValue());
            pixelMapper.setBounds(new ArrayList<>(Arrays.asList(NW, SE)));
        } catch (Exception ex) {

        }
        if (!center.equals(newCenter)) {
            try {
                final String centerCommand =
                        "setCenter(" + newCenter.getLatitude() + ", " + newCenter.getLongitude() + ")";
                webEngine.executeScript(centerCommand);
                center = newCenter;

                webEngine.executeScript("resetMarkers();");
                for(BoundaryMark mark : race.getCourse().getBoundaries()) {
                    webEngine.executeScript("addMarker(" + mark.getCoordinate().getLatitude() + ", " + mark.getCoordinate().getLongitude() + ");");
                }

                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }


    public DoubleProperty nProperty() {
        return n;
    }

    public DoubleProperty sProperty() {
        return s;
    }
}
