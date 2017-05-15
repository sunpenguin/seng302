package seng302.team18.visualiser.util;

import javafx.scene.layout.Pane;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.GeographicLocation;
import seng302.team18.util.GPSCalculations;
import seng302.team18.util.XYPair;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for mapping coordinates on to a pane.
 */
public class PixelMapper {

    private final Course course;
    private final Pane pane;
    private Coordinate NW;
    private Coordinate SE;
    private int zoomLevel = 0;
    private Coordinate viewPortCenter;

    public PixelMapper(Course course, Pane pane) {
        this.course = course;
        this.pane = pane;

        viewPortCenter = course.getCentralCoordinate();
        NW = new Coordinate(viewPortCenter.getLatitude(), viewPortCenter.getLongitude());
        SE = new Coordinate(viewPortCenter.getLatitude(), viewPortCenter.getLongitude());
    }

    public XYPair coordToPixel(Coordinate coord) {
        findMinMaxPoints();
        double courseWidth = calcCourseWidth();
        double courseHeight = calcCourseHeight();

//        System.out.println(String.format("course width=%.5f height=%.5f", courseWidth, courseHeight));


        double paneWidth = pane.getWidth();
        double paneHeight = pane.getHeight();
        if (paneHeight <= 0 || paneWidth <= 0) {
            paneWidth = pane.getPrefWidth();
            paneHeight = pane.getPrefHeight();
        }

        double mappingScale = 1;
        if (courseWidth / courseHeight > paneWidth / paneHeight) {
            mappingScale = paneWidth / courseWidth;
        } else {
            mappingScale = paneHeight / courseHeight;
        }

        XYPair worldCoordinates = coordinateToPlane(coord);
        XYPair viewCenter = coordinateToPlane(viewPortCenter);

        double dX = worldCoordinates.getX() - viewCenter.getX();
        double dY = worldCoordinates.getY() - viewCenter.getY();

        double x = dX * mappingScale + paneWidth / 2;
        double y = dY * mappingScale + paneHeight / 2;

        if (x < 0 || y < 0) {
            System.out.println(coord.toString() + " " + x + " " + y);
        }

        return new XYPair(x, y);
    }

    /**
     * Converts the given longitude to a value in [0, 256 * 2 ^ zoomLevel]
     *
     * @param longitude
     * @return
     */
    private double longitudeToPixels(double longitude) {
        double x = 128 * Math.pow(2, zoomLevel) * (longitude + Math.PI) / Math.PI;
        return x;
    }

    /**
     * Converts the given latitude to a value in [0, 256 * 2 ^ zoomLevel]
     *
     * @param latitude
     * @return
     */
    private double latitudeToPixels(double latitude) {
        double y = 128 * Math.pow(2, zoomLevel) * (Math.PI - Math.log(Math.tan((Math.PI / 4) + (latitude / 2)))) / Math.PI;
        return y;
    }

    private XYPair coordinateToPlane(Coordinate point) {
        return new XYPair(longitudeToPixels(point.getLongitude()), latitudeToPixels(point.getLatitude()));
    }

    /**
     * Finds the min and max points of a course using boundaries.
     */
    private void findMinMaxPoints() {
        // TODO write test
        double minLong = 180;
        double maxLong = -180;
        double minLat = 90;
        double maxLat = -90;

        List<GeographicLocation> points = new ArrayList<>(course.getBoundaries());
        for (CompoundMark compoundMark : course.getCompoundMarks()) {
            points.addAll(compoundMark.getMarks());
        }

        for (GeographicLocation point : points) {
            if (point.getCoordinate().getLongitude() < minLong) {
                minLong = point.getCoordinate().getLongitude();
            }

            if (point.getCoordinate().getLongitude() > maxLong) {
                maxLong = point.getCoordinate().getLongitude();
            }

            if (point.getCoordinate().getLatitude() < minLat) {
                minLat = point.getCoordinate().getLatitude();
            }

            if (point.getCoordinate().getLatitude() > maxLat) {
                maxLat = point.getCoordinate().getLatitude();
            }
        }

        NW = new Coordinate(maxLat, minLong);
        SE = new Coordinate(minLat, maxLong);

//        System.out.println(NW);
//        System.out.println(SE);
    }

    private double calcCourseWidth() {
        Coordinate west = new Coordinate(course.getCentralCoordinate().getLatitude(), NW.getLongitude());
        Coordinate east = new Coordinate(course.getCentralCoordinate().getLatitude(), SE.getLongitude());

        double dWest = GPSCalculations.distance(west, course.getCentralCoordinate());
        double dEast = GPSCalculations.distance(course.getCentralCoordinate(), east);

        Coordinate furthest = (dWest > dEast) ? west : east;
        return Math.abs(coordinateToPlane(course.getCentralCoordinate()).getX() - coordinateToPlane(furthest).getX()) * 2;
    }

    private double calcCourseHeight() {
        Coordinate north = new Coordinate(NW.getLatitude(), course.getCentralCoordinate().getLongitude());
        Coordinate south = new Coordinate(SE.getLatitude(), course.getCentralCoordinate().getLongitude());

        double dNorth = GPSCalculations.distance(north, course.getCentralCoordinate());
        double dSouth = GPSCalculations.distance(south, course.getCentralCoordinate());

        Coordinate furthest = (dNorth > dSouth) ? north : south;
        return Math.abs(coordinateToPlane(course.getCentralCoordinate()).getY() - coordinateToPlane(furthest).getY()) * 2;
    }
}


