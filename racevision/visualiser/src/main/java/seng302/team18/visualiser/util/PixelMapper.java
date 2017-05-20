package seng302.team18.visualiser.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.util.GPSCalculations;
import seng302.team18.util.XYPair;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Class for mapping coordinates on to a pane.
 * Conversions from GPS to cartesian coordinates done using the Web Mercator system.
 */
public class PixelMapper {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private final Course course;
    private final Pane pane;
    private Coordinate viewPortCenter;
    private final IntegerProperty zoomLevel = new SimpleIntegerProperty(0);
    private List<Coordinate> bounds; // 2 coordinates: NW bound, SE bound
    private WebEngine webEngine;
    private XYPair nwBound = new XYPair(0, 0); // Pixel coordinates for the NW bound of the map on screen
    private  XYPair seBound = new XYPair(0, 0); // Pixel coordinates for the SE bound of the map on screen
    private int previousZoomLevel = 0;
    private XYPair worldCoordinatesCartesian = new XYPair(0, 0);
    private XYPair viewCenterCartesian = new XYPair(0, 0);
    private Coordinate previousViewCenter = new Coordinate(0, 0);

    public static final int ZOOM_LEVEL_4X = 1;

    public PixelMapper(Course course, Pane pane, WebEngine webEngine) {
        this.course = course;
        this.pane = pane;
        this.webEngine = webEngine;
        bounds = GPSCalculations.findMinMaxPoints(course);

        viewPortCenter = course.getCentralCoordinate();
    }

    public void addViewCenterListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener("viewPortCenter", listener);
    }

    public void removeViewCenterListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener("viewPortCenter", listener);
    }

    public void setViewPortCenter(Coordinate center) {
        Coordinate old = viewPortCenter;
        viewPortCenter = center;
        propertyChangeSupport.firePropertyChange("viewPortCenter", old, center);
    }

    public IntegerProperty zoomLevelProperty() {
        return zoomLevel;
    }

    public void setZoomLevel(int level) {
        zoomLevel.set(level);
    }

    /**
     * Get the current linear zoom factor
     *
     * @return the zoom factor
     */
    public double getZoomFactor() {
        return Math.pow(2, zoomLevel.intValue());
    }

    /**
     * Maps a coordinate to a pixel value relative to the current resolution and zoom of the race view pane
     * NOTE: Origin is at the top left corner (X and Y increase to the right and downwards respectively)
     *
     * @param coord Coordinate to map
     * @return XYPair containing the x and y pixel values
     */
    public XYPair coordToPixel(Coordinate coord) {

        IntegerProperty mapZoom = new SimpleIntegerProperty(0);

        try {
            worldCoordinatesCartesian = getPixelsFromGoogle(coord);
            viewCenterCartesian = getPixelsFromGoogle(viewPortCenter);
            nwBound = getPixelsFromGoogle(bounds.get(0));
            seBound = getPixelsFromGoogle(bounds.get(1));

            if (previousViewCenter!= viewPortCenter || zoomLevel.intValue() != previousZoomLevel) {
                webEngine.executeScript("map.setCenter({lat: " + viewPortCenter.getLatitude() +
                        ", lng: " + viewPortCenter.getLongitude() + "});");
                if(zoomLevel.intValue() != previousZoomLevel){
                    webEngine.executeScript("toggleZoomed();");
                }
                previousViewCenter = viewPortCenter;
                previousZoomLevel = zoomLevel.intValue();
            }
        } catch (Exception e) {
            // The maps have not loaded yet
        }

        double courseWidth = calcCourseWidth();
        double courseHeight = calcCourseHeight();
        double paneWidth = pane.getWidth();
        double paneHeight = pane.getHeight();

        if (paneHeight <= 0 || paneWidth <= 0) {
            paneWidth = pane.getPrefWidth();
            paneHeight = pane.getPrefHeight();
        }

        double mappingScale;

        if (courseWidth / courseHeight > paneWidth / paneHeight) {
            mappingScale = paneWidth / courseWidth;
        } else {
            mappingScale = paneHeight / courseHeight;
        }

//        mappingScale *= Math.pow(2, zoomLevel.intValue());
//        if(mappingScale != 1){
//            mappingScale = mappingScale/2;
//        }

        double dX = worldCoordinatesCartesian.getX() - viewCenterCartesian.getX();
        double dY = worldCoordinatesCartesian.getY() - viewCenterCartesian.getY();

        double x = dX * mappingScale + paneWidth / 2;
        double y = dY * mappingScale + paneHeight / 2;

        return new XYPair(x, y);
    }

    /**
     * Calculates the width of the course
     *
     * @return the width of the course using Web Mercator cartesian coordinates
     */
    private double calcCourseWidth() {
        return Math.abs(nwBound.getX() - seBound.getX());
    }

    /**
     * Calculates the height of the course
     *
     * @return the width of the course using Web Mercator cartesian coordinates
     */
    private double calcCourseHeight() {
        return Math.abs(nwBound.getY() - nwBound.getY());
    }

    public void setBounds(List<Coordinate> bounds) {
        this.bounds = bounds;
    }

    private XYPair getPixelsFromGoogle(Coordinate coord) throws Exception {
        double x = ((double) webEngine.executeScript("convertCoordX(" + coord.getLatitude() + ","
                + coord.getLongitude() + ");"));
        double y = ((double) webEngine.executeScript("convertCoordY(" + coord.getLatitude() + ","
                + coord.getLongitude() + ");"));
        return new XYPair(x, y);
    }
}


