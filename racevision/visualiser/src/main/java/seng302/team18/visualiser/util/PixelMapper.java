package seng302.team18.visualiser.util;

import javafx.scene.layout.Pane;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.util.GPSCalculations;
import seng302.team18.util.XYPair;

/**
 * Created by dhl25 on 30/03/17.
 */
public class PixelMapper {

    private Course course;
    private Pane pane;
    private Double padding;

    public PixelMapper(Course course, Pane pane, Double padding) {
        this.course = course;
        this.pane = pane;
        this.padding = padding;
    }

    public XYPair convertCoordPixelNoZoom(Coordinate coord) {

        GPSCalculations calculator = new GPSCalculations(course);
        double pixelWidth = pane.getWidth() - padding * 2;
        double pixelHeight = pane.getHeight() - padding * 2;
        if (pixelHeight <= 0 || pixelWidth <= 0) {
            pixelWidth = pane.getPrefWidth() - padding * 2;
            pixelHeight = pane.getPrefHeight() - padding * 2;
        }

        if (pixelHeight > pixelWidth) {
            pixelHeight = pixelWidth;
        } else if (pixelWidth > pixelHeight) {
            pixelWidth = pixelHeight;
        }

        GPSCalculations gps = new GPSCalculations(course);
        gps.findMinMaxPoints(course);
        double courseWidth = gps.getMaxX() - gps.getMinX();
        double courseHeight = gps.getMaxY() - gps.getMinY();

        XYPair planeCoordinates = calculator.coordinateToPixel(coord);
        double aspectRatio = courseWidth / courseHeight;

        if (courseHeight > courseWidth) {
            pixelWidth *= aspectRatio;
        } else {
            pixelHeight *= aspectRatio;
        }

        double widthRatio = (courseWidth - (gps.getMaxX() - planeCoordinates.getX())) / courseWidth;
        double heightRatio = (courseHeight - (gps.getMaxY() - planeCoordinates.getY())) / courseHeight;

        XYPair result = new XYPair(pixelWidth * widthRatio + padding, ((pixelHeight * heightRatio + padding) * -1 + (pixelHeight + (padding * 2))));
        return result;
    }

    /**
     * Converts the latitude / longitude coordinates to pixel coordinates.
     * @param coord Coordinates to be converted
     * @return x and y pixel coordinates of the given coordinates
     */
    public XYPair convertCoordPixel(Coordinate coord) {

        GPSCalculations calculator = new GPSCalculations(course);
        double pixelWidth = pane.getWidth() - padding * 2;
        double pixelHeight = pane.getHeight() - padding * 2;
        if (pixelHeight <= 0 || pixelWidth <= 0) {
            pixelWidth = pane.getPrefWidth() - padding * 2;
            pixelHeight = pane.getPrefHeight() - padding * 2;
        }

        if (pixelHeight > pixelWidth) {
            pixelHeight = pixelWidth;
        } else if (pixelWidth > pixelHeight) {
            pixelWidth = pixelHeight;
        }

        GPSCalculations gps = new GPSCalculations(course);
        gps.findMinMaxPoints(course);
        double courseWidth = gps.getMaxX() - gps.getMinX();
        double courseHeight = gps.getMaxY() - gps.getMinY();


        // Zooming
        int zoom = 0;
        double zoomPixelWidth = pixelWidth;
        double zoomPixelHeight = pixelHeight;
        Coordinate viewCentre = course.getViewCenter();
        XYPair pan = new XYPair(0, 0);
        if (viewCentre != null) {
            if (true) {
                // 4x zoom
                zoom = 4;
                zoomPixelWidth *= 2;
                zoomPixelHeight *= 2;
                pan = convertCoordPixelNoZoom(viewCentre);
            }
        }


        XYPair planeCoordinates = calculator.coordinateToPixel(coord);
        double aspectRatio = courseWidth / courseHeight;

        if (courseHeight > courseWidth) {
            pixelWidth *= aspectRatio;
            zoomPixelWidth *= aspectRatio;
        } else {
            pixelHeight *= aspectRatio;
            zoomPixelHeight *= aspectRatio;
        }

        double widthRatio = (courseWidth - (gps.getMaxX() - planeCoordinates.getX())) / courseWidth;
        double heightRatio = (courseHeight - (gps.getMaxY() - planeCoordinates.getY())) / courseHeight;

        XYPair resultZoom = new XYPair(zoomPixelWidth * widthRatio - (pan.getX() * 2),
                                   ((zoomPixelHeight* heightRatio) * -1 + (zoomPixelHeight)) - (pan.getY() * 2));

        if (viewCentre != null) {
            resultZoom.shiftX((zoomPixelWidth / zoom) + padding * 6);
            resultZoom.shiftY((zoomPixelHeight / zoom)  + padding * 2);
        }

        return resultZoom;
    }
}
