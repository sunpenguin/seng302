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
        double pixelWidth = pane.getWidth();
        double pixelHeight = pane.getHeight();
        if (pixelHeight <= 0 || pixelWidth <= 0) {
            pixelWidth = pane.getPrefWidth();
            pixelHeight = pane.getPrefHeight();
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

        XYPair result = new XYPair(pixelWidth * widthRatio, ((pixelHeight * heightRatio) * -1 + (pixelHeight)));
        return result;
    }

    /**
     * Converts the latitude / longitude coordinates to pixel coordinates.
     * @param coord Coordinates to be converted
     * @return x and y pixel coordinates of the given coordinates
     */
    public XYPair convertCoordPixel(Coordinate coord) {

        GPSCalculations calculator = new GPSCalculations(course);
        double pixelWidth = pane.getWidth();
        double pixelHeight = pane.getHeight();
        if (pixelHeight <= 0 || pixelWidth <= 0) {
            pixelWidth = pane.getPrefWidth();
            pixelHeight = pane.getPrefHeight();
        }

        /*
            so we map only to values in a square region
         */
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
        if (course.getZoomId() != 0) { /* If zoomId is 0, then no zoom should be applied */
            zoom = course.getZoomLevel();

            /*   Example: zoom = 8. Now effective pixel width and height are 4x as large.
                         Features will only be seen if the pixel value they receive falls
                         within the resolution of the race-view section of the window.
                         With the larger pixel height and width we will only see 1/8th of
                         the original view, and will give the effect of zooming.
            */
            zoomPixelWidth *= (zoom / 2);
            zoomPixelHeight *= (zoom / 2);

            /*
                We need to pan the viewable area of the scene to where feature the user
                chose is. This however will cause the feature they chose to be at the top
                left of the screen. It still has to be centered.
             */
            pan = convertCoordPixelNoZoom(viewCentre);
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

//        double centerXDifference = ((1 - aspectRatio) * pixelWidth) / 2;


        double widthRatio = (courseWidth - (gps.getMaxX() - planeCoordinates.getX())) / courseWidth;
        double heightRatio = (courseHeight - (gps.getMaxY() - planeCoordinates.getY())) / courseHeight;

        XYPair resultZoom = new XYPair((zoomPixelWidth * widthRatio - (pan.getX() * (zoom / 2))),
                                   ((zoomPixelHeight * heightRatio) * -1 + (zoomPixelHeight)) - (pan.getY() * (zoom / 2)));

        /*
            Now we center the zoomed view by shifting it by the necessary amount so that
            what was the top left corner is now the center of the zoomed view.
            Example: zoomPixelWidth = 2000, zoom = 8. Then to center the view we have to shift by
                     2000 / 8 pixels to the left and up.
         */
        if (course.getZoomId() != 0) {
            resultZoom.shiftX((zoomPixelWidth / zoom));
            resultZoom.shiftY((zoomPixelHeight / zoom));
        }

        return resultZoom;
    }
}
