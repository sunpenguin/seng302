package seng302.team18.visualiser.util;

import javafx.scene.layout.Pane;
import seng302.team18.model.*;
import seng302.team18.util.XYPair;

/**
 * Class for mapping coordinates on to a pane.
 */
public class PixelMapper {

    private Course course;
    private Pane pane;
    private Double padding;
    private double minX = Double.MAX_VALUE;
    private double maxX = -(Double.MAX_VALUE);
    private double minY = Double.MAX_VALUE;
    private double maxY = -(Double.MAX_VALUE);

    public PixelMapper(Course course, Pane pane, Double padding) {
        this.course = course;
        this.pane = pane;
        this.padding = padding;
    }

    /**
     * Converts the latitude / longitude coordinates to pixel coordinates.
     * @param coord Coordinates to be converted
     * @return x and y pixel coordinates of the given coordinates
     */
    public XYPair coordToPixel(Coordinate coord) {

        findMinMaxPoints();
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

        double courseWidth = maxX - minX;
        double courseHeight = maxY - minY;
        XYPair planeCoordinates = coordinateToPlane(coord);
        double aspectRatio = courseWidth / courseHeight;

        if (courseHeight > courseWidth) {
            pixelWidth *= aspectRatio;
        } else {
            pixelHeight *= aspectRatio;
        }

        double widthRatio = (courseWidth - (maxX - planeCoordinates.getX())) / courseWidth;
        double heightRatio = (courseHeight - (maxY - planeCoordinates.getY())) / courseHeight;

        return new XYPair(pixelWidth * widthRatio + padding, (pixelHeight * heightRatio + padding) * - 1);
    }


    /**
     * Method to convert a given Coordinate from longitude and latitude to x, y values.
     * Source: http://stackoverflow.com/questions/16266809/convert-from-latitude-longitude-to-x-y
     *
     * @param point The coordinates to convert.
     * @return the coordinate mapped to a plane.
     */
    private XYPair coordinateToPlane(Coordinate point) {
        double earthRadius = 6371e3; // meters
        double aspectLat = Math.cos(course.getCentralCoordinate().getLatitude());
        double x = earthRadius * Math.toRadians(point.getLongitude()) * aspectLat;
        double y = earthRadius * Math.toRadians(point.getLatitude());
        return new XYPair(x, y);
    }

//    public Coordinate pixelToCoordinate(XYPair point) {
//        double earthRadius = 6371e3; // meters
//        double aspectLat = Math.cos(course.getCentralCoordinate().getLatitude());
//        double latitude = Math.toDegrees(point.getY() / earthRadius);
//        double longitude = Math.toDegrees((point.getX() / earthRadius) / aspectLat);
//        return new Coordinate(latitude, longitude);
//    }


    /**
     * Finds the min and max points of a course using boundaries.
     */
    private void findMinMaxPoints() {
        minX = Double.MAX_VALUE;
        maxX = -(Double.MAX_VALUE);
        minY = Double.MAX_VALUE;
        maxY = -(Double.MAX_VALUE);
        for (BoundaryMark boundary : course.getBoundaries()) {
            XYPair boundaryXYValues = coordinateToPlane(boundary.getCoordinate());
            double xValue = boundaryXYValues.getX();
            double yValue = boundaryXYValues.getY();
            if (xValue < minX) {
                minX = xValue;
            }
            if (xValue > maxX) {
                maxX = xValue;
            }
            if (yValue < minY) {
                minY = yValue;
            }
            if (yValue > maxY) {
                maxY = yValue;
            }
        }
//        for (CompoundMark compoundMark : course.getCompoundMarks()) {
//            for (Mark mark : compoundMark.getMarks()) {
//                XYPair markXYValues = coordinateToPlane(mark.getCoordinate());
//                double xValue = markXYValues.getX();
//                double yValue = markXYValues.getY();
//                if (xValue < minX) {
//                    minX = xValue;
//                }
//                if (xValue > maxX) {
//                    maxX = xValue;
//                }
//                if (yValue < minY) {
//                    minY = yValue;
//                }
//                if (yValue > maxY) {
//                    maxY = yValue;
//                }
//            }
    }
}


