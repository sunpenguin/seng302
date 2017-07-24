package seng302.team18.visualiser.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
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

    private GPSCalculations gpsCalculations;

    public static final int ZOOM_LEVEL_4X = 1;
    private final int NW_BOUND_INDEX = 0; // Used in bounds
    private final int SE_BOUND_INDEX = 1; // Used in bounds
    private final double MAP_SCALE_CORRECTION = 0.95;

    public PixelMapper(Course course, Pane pane) {
        this.course = course;
        this.pane = pane;
        gpsCalculations = new GPSCalculations();
        bounds = gpsCalculations.findMinMaxPoints(course);
        viewPortCenter = course.getCentralCoordinate();
    }


    public void addViewCenterListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener("viewPortCenter", listener);
    }

    public void removeViewCenterListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener("viewPortCenter", listener);
    }

    /**
     * Update the view port center to a certain GPS coordinate.
     *
     * @param center The target location to center the view on. (Example: A boat or mark location)
     */
    public void setViewPortCenter(Coordinate center) {
        Coordinate old = viewPortCenter;
        viewPortCenter = center;
        propertyChangeSupport.firePropertyChange("viewPortCenter", old, center);
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
        bounds = gpsCalculations.findMinMaxPoints(course);

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
        mappingScale *= MAP_SCALE_CORRECTION * Math.pow(2, zoomLevel.intValue());

        XYPair worldCoordinates = coordinateToPlane(coord);
        XYPair viewCenter = coordinateToPlane(viewPortCenter);

        double dX = worldCoordinates.getX() - viewCenter.getX();
        double dY = worldCoordinates.getY() - viewCenter.getY();

        double x = dX * mappingScale + paneWidth / 2;
        double y = dY * mappingScale + paneHeight / 2;

        return new XYPair(x, y);
    }

    /**
     *  Calculates the mapping ratio between the pixel and geographical coordinates
     *
     * @return double, The ratio value (number of pixels : meter)
     */
    public double mappingRatio(){
        GPSCalculations gpsCalculator = new GPSCalculations();
        Coordinate oneKNorthOfCentre = gpsCalculator.toCoordinate(course.getCentralCoordinate(), 0, 1000);

        XYPair XYCenter = coordToPixel(course.getCentralCoordinate());
        XYPair XYKNorthOfCenter = coordToPixel(oneKNorthOfCentre);

        double distanceBetweenK = XYCenter.calculateDistance(XYKNorthOfCenter);

        return distanceBetweenK/1000;
    }


    /**
     * Calculates the width of the course
     *
     * @return the width of the course using Web Mercator cartesian coordinates
     */
    private double calcCourseWidth() {
        Coordinate west = new Coordinate(course.getCentralCoordinate().getLongitude(), bounds.get(NW_BOUND_INDEX).getLongitude());
        Coordinate east = new Coordinate(course.getCentralCoordinate().getLongitude(), bounds.get(SE_BOUND_INDEX).getLongitude());

        double dWest = gpsCalculations.distance(west, course.getCentralCoordinate());
        double dEast = gpsCalculations.distance(course.getCentralCoordinate(), east);

        Coordinate furthest = (dWest > dEast) ? west : east;
        return Math.abs(coordinateToPlane(course.getCentralCoordinate()).getX() - coordinateToPlane(furthest).getX()) * 2;
    }

    /**
     * Calculates the height of the course
     *
     * @return the width of the course using Web Mercator cartesian coordinates
     */
    private double calcCourseHeight() {
        Coordinate north = new Coordinate(bounds.get(NW_BOUND_INDEX).getLatitude(), course.getCentralCoordinate().getLatitude());
        Coordinate south = new Coordinate(bounds.get(SE_BOUND_INDEX).getLatitude(), course.getCentralCoordinate().getLatitude());

        double dNorth = gpsCalculations.distance(north, course.getCentralCoordinate());
        double dSouth = gpsCalculations.distance(south, course.getCentralCoordinate());

        Coordinate furthest = (dNorth > dSouth) ? north : south;
        return Math.abs(coordinateToPlane(course.getCentralCoordinate()).getY() - coordinateToPlane(furthest).getY()) * 2;
    }

    /**
     * Converts the given longitude to a value in [0, 256 * 2 ^ zoomLevel]
     *
     * @param longitude longitude to convert
     * @return longitude in Web Mercator scale
     */
    private double webMercatorLongitude(double longitude) {
        return 128 * (longitude + Math.PI) / Math.PI;
    }

    /**
     * Converts the given latitude to a value in [0, 256 * 2 ^ zoomLevel]
     *
     * @param latitude Latitude to convert
     * @return latitude in Web Mercator scale
     */
    private double webMercatorLatitude(double latitude) {
        return 128 * (Math.PI - Math.log(Math.tan((Math.PI / 4) + (latitude / 2)))) / Math.PI;
    }

    /**
     * Converts a coordinate from GPS coordinates to cartesian coordinates in the range [0, 256 * 2 ^ zoomLevel] for
     * both x and y
     *
     * @param point Coordinate to convert
     * @return converted coordinate
     */
    private XYPair coordinateToPlane(Coordinate point) {
        return new XYPair(webMercatorLongitude(point.getLongitude()), webMercatorLatitude(point.getLatitude()));
    }

    public IntegerProperty zoomLevelProperty() {
        return zoomLevel;
    }

    public void setZoomLevel(int level) {
        zoomLevel.set(level);
    }
}