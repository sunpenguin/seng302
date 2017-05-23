package seng302.team18.visualiser.display;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.web.WebEngine;
import seng302.team18.model.*;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.ArrayList;
import java.util.Arrays;


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
    private DoubleProperty north = new SimpleDoubleProperty(0);
    private DoubleProperty south = new SimpleDoubleProperty(0);
    private DoubleProperty east = new SimpleDoubleProperty(0);
    private DoubleProperty west = new SimpleDoubleProperty(0);


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
     * sets the BackgroundRenderers variables using data from the web engine
     */
    private void setDirections(){
        try {
            north.set((double) webEngine.executeScript("getN();"));
            south.set((double) webEngine.executeScript("getS();"));
            east.set((double) webEngine.executeScript("getE();"));
            west.set((double) webEngine.executeScript("getW();"));
            NW.setLatitude(north.getValue());
            NW.setLongitude(west.getValue());
            SE.setLatitude(south.getValue());
            SE.setLongitude(east.getValue());
            pixelMapper.setBounds(new ArrayList<>(Arrays.asList(NW, SE)));
        } catch (Exception ex) {

        }
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
        setDirections();
        if (!center.equals(newCenter)) {
            try {
                final String centerCommand = "setCenter(" + newCenter.getLatitude() + ", " + newCenter.getLongitude() + ")";
                webEngine.executeScript(centerCommand);
                center = newCenter;

                webEngine.executeScript("resetMarkers();");
                for(BoundaryMark mark : race.getCourse().getBoundaries()) {
                    addMark(mark);
                }

                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }


    /**
     * Adds a mark to be plotted on the map
     * @param mark the mark to be plotted on the map
     */
    public void addMark(BoundaryMark mark) {
        webEngine.executeScript("addMarker(" + mark.getCoordinate().getLatitude() + ", " + mark.getCoordinate().getLongitude() + ");");
    }


    /**
     * Returns the DoubleProperty for north bound of the map.
     * @return north bound
     */
    public DoubleProperty northProperty() {
        return north;
    }


    /**
     * Returns the DoubleProperty for south bound of the map.
     * @return south bound
     */
    public DoubleProperty southProperty() {
        return south;
    }
}
