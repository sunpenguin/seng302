package seng302.team18.visualiser.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import seng302.team18.model.Coordinate;
import seng302.team18.model.GeographicLocation;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.XYPair;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Used to map coordinates to pixel values on a pane.
 */
public class PixelMapper {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private Coordinate center;
    private Coordinate northWest;
    private Coordinate southEast;
    private GeographicLocation object;
    private final ObservableDoubleValue paneHeightProp;
    private final ObservableDoubleValue paneWidthProp;
    private Coordinate viewPortCenter;
    private final DoubleProperty zoomLevel = new SimpleDoubleProperty(0);
    private double maxZoom = Double.POSITIVE_INFINITY;
    private double minZoom = Double.NEGATIVE_INFINITY;
    private double mappingScale = 1;

    private final static GPSCalculator GPS_CALCULATOR = new GPSCalculator();
    private boolean isTracking = false;

    private final double ZOOM_CORRECTION = 4; // corrects zoom level to sensible values ie setZoom 4 magnifies by 4


    /**
     * Constructor for PixelMapper.
     *
     * @param northWest      a geographic coordinate expressing the northern and western limits of the mapped area
     * @param southEast      a geographic coordinate expressing the southern and eastern limits of the mapped area
     * @param center         a geographic coordinate expressing centre point of the mapped area
     * @param paneHeightProp the height of the pane to map to
     * @param paneWidthProp  the width of the pane to map to
     */
    public PixelMapper(Coordinate northWest, Coordinate southEast, Coordinate center,
                       ObservableDoubleValue paneHeightProp, ObservableDoubleValue paneWidthProp) {
        this.northWest = northWest;
        this.southEast = southEast;
        this.center = center;
        this.paneHeightProp = paneHeightProp;
        this.paneWidthProp = paneWidthProp;
        viewPortCenter = center;
        calculateMappingScale();
    }


    public void addViewCenterListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener("viewPortCenter", listener);
    }


    @SuppressWarnings("unused")
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
        return Math.pow(2, zoomLevel.doubleValue());
    }


    /**
     * Calculates the mapping scale to be used when mapping geographic coordinates to the display pane.
     * This must be called before any calls to {@link PixelMapper#mapToPane(Coordinate)} if the tracking state,
     * zoom level, pane size or the course width/height have changed since the last call to this method.
     * <p>
     * It is recommended for this method to be called at the start of each rendering loop.
     */
    public void calculateMappingScale() {
        if (isTracking && object.getCoordinate() != null) {
            setViewPortCenter(object.getCoordinate());
        }

        double courseWidth = calcCourseWidth();
        double courseHeight = calcCourseHeight();
        double paneWidth = paneWidthProp.get();
        double paneHeight = paneHeightProp.get();

        if (courseWidth / courseHeight > paneWidth / paneHeight) {
            mappingScale = paneWidth / courseWidth;
        } else {
            mappingScale = paneHeight / courseHeight;
        }

        final double MAP_SCALE_CORRECTION = 0.95;
        mappingScale *= MAP_SCALE_CORRECTION * getZoomFactor();
    }


    /**
     * Maps a coordinate to a pixel value relative to the current resolution and zoom of the race view pane
     * NOTE: Origin is at the top left corner (X and Y increase to the right and downwards respectively)
     * <p>
     * Precondition: Neither the tracking state, zoom level, pane size nor the course width/height
     * have changed since the last call to {@link PixelMapper#calculateMappingScale()} was made.
     * It is recommended that that call is made at the start of each rendering loop.
     *
     * @param coordinate Coordinate to map
     * @return XYPair containing the x and y pixel values
     */
    public XYPair mapToPane(Coordinate coordinate) {
        double dX = distanceAlongCircleOfLatitude(viewPortCenter, coordinate.getLongitude());
        double dY = distanceAlongMeridian(viewPortCenter, coordinate.getLatitude());

        double x = (dX * mappingScale) + (paneWidthProp.get() / 2);
        double y = (dY * mappingScale) + (paneHeightProp.get() / 2);

        return new XYPair(x, y);
    }


    /**
     * Along the the meridian that passes through the reference point, calculates the length of the arc from the
     * reference point to the given latitude. If the given latitude is north of the reference point the returned value
     * is positive, and if it is south it is negative.
     *
     * @param reference specifies the longitude (meridian) and the starting latitude
     * @param latitude  specifies the other latitude value
     * @return the displacement of the given latitude from the reference latitude along the given meridian
     */
    private double distanceAlongMeridian(Coordinate reference, double latitude) {
        double distance = GPS_CALCULATOR.distance(reference, new Coordinate(latitude, reference.getLongitude()));
        boolean latitudeAboveReference = latitude > reference.getLatitude();
        return latitudeAboveReference ? -distance : distance;
    }


    /**
     * Along the circle of latitude that passes through the reference point, calculates the length of the arc from
     * the reference point to the given longitude. If the given longitude is to the east (within 180 degrees) of the
     * reference point, the returned value is positive, and if it is to the west it is negative.
     *
     * @param reference specifies the latitude (circle of latitude) and the starting longitude
     * @param longitude specifies the other longitude value
     * @return the displacement of the given longitude from the reference longitude along the given meridian (m)
     */
    private double distanceAlongCircleOfLatitude(Coordinate reference, double longitude) {
        double degreesEastFromLongToRef = reference.getLongitude() - longitude;

        if (degreesEastFromLongToRef < -180) {
            degreesEastFromLongToRef += 360;
        } else if (degreesEastFromLongToRef > 180) {
            degreesEastFromLongToRef -= 360;
        }

        Coordinate refPrime = new Coordinate(reference.getLatitude(), 0);
        Coordinate point = new Coordinate(reference.getLatitude(), degreesEastFromLongToRef);
        double distance = GPS_CALCULATOR.distance(refPrime, point);

        boolean longToEastOfRef = degreesEastFromLongToRef <= 0;
        return longToEastOfRef ? distance : -distance;
    }


    /**
     * Calculates the mapping ratio between the pixel and geographical coordinates
     *
     * @return double, The ratio value (number of pixels : meter)
     */
    public double mappingRatio() {
        GPSCalculator gpsCalculator = new GPSCalculator();
        Coordinate oneKNorthOfCentre = gpsCalculator.toCoordinate(center, 0, 1000);

        calculateMappingScale();
        XYPair XYCenter = mapToPane(center);
        XYPair XYKNorthOfCenter = mapToPane(oneKNorthOfCentre);

        double distanceBetweenK = XYCenter.calculateDistance(XYKNorthOfCenter);

        return distanceBetweenK / 1000;
    }


    /**
     * Calculates the width of the course
     *
     * @return the width of the course (m)
     */
    @SuppressWarnings("Duplicates")
    private double calcCourseWidth() {
        Coordinate west = new Coordinate(center.getLatitude(), northWest.getLongitude());
        Coordinate east = new Coordinate(center.getLatitude(), southEast.getLongitude());

        double dWest = GPS_CALCULATOR.distance(west, center);
        double dEast = GPS_CALCULATOR.distance(center, east);

        return Math.max(dEast, dWest) * 2;
    }


    /**
     * Calculates the height of the course
     *
     * @return the width of the course (m)
     */
    @SuppressWarnings("Duplicates")
    private double calcCourseHeight() {
        Coordinate north = new Coordinate(northWest.getLatitude(), center.getLongitude());
        Coordinate south = new Coordinate(southEast.getLatitude(), center.getLongitude());

        double dNorth = GPS_CALCULATOR.distance(north, center);
        double dSouth = GPS_CALCULATOR.distance(south, center);

        return Math.max(dNorth, dSouth) * 2;
    }


    /**
     * Returns the zoomLevelProperty value
     *
     * @return The zoom level property
     */
    public DoubleProperty zoomLevelProperty() {
        return zoomLevel;
    }


    /**
     * Sets the zoom level for the pixel mapper.
     * Does not zoom in beyond the max value or below min value/0 if no minimum level.
     *
     * @param level The value the zoom level will be set to
     */
    public void setZoomLevel(double level) {
        if (level < 0) {
            zoomLevel.set(0);
        } else if (level < maxZoom) {
            zoomLevel.set(level / ZOOM_CORRECTION);
        }else {
            zoomLevel.set(maxZoom / ZOOM_CORRECTION);
        }

        if (level < minZoom) {
            zoomLevel.set(minZoom / ZOOM_CORRECTION);
        }
    }


    public double getZoomLevel() {
        return zoomLevel.get() * ZOOM_CORRECTION;
    }


    /**
     * Sets the object to track.
     *
     * @param object the object to track
     */
    public void track(GeographicLocation object) {
        this.object = object;
    }


    /**
     * Sets the tracking boolean to whether to track or not.
     *
     * @param tracking whether to track or not
     */
    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }


    public void setMaxZoom(double maxZoom) {
        this.maxZoom = maxZoom;
    }


    public double getMinZoom() {
        return minZoom;
    }


    public void setMinZoom(double minZoom) {
        this.minZoom = minZoom;
    }


}