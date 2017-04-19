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
    /**
     * Converts the latitude / longitude coordinates to pixel coordinates.
     * @param coord Coordinates to be converted
     * @return x and y pixel coordinates of the given coordinates
     */
    public static XYPair convertCoordPixel
            (Coordinate coord, double padding, Pane pane, Course course) {

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

        return new XYPair(pixelWidth * widthRatio + padding, (pixelHeight * heightRatio + padding) * -1);
    }
}
