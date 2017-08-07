package seng302.team18.visualiser.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import org.junit.Ignore;
import org.junit.Test;
import seng302.team18.model.*;
import seng302.team18.util.GPSCalculations;
import seng302.team18.util.XYPair;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by David-chan on 13/05/17.
 */
public class PixelMapperTest {

    private PixelMapper mapper;
    private Course course;
    private Pane pane;


    @Ignore
    @Test
    public void mappingRatioOnNormalCourseTest() {
        //Make course
        BoundaryMark boundary1 = new BoundaryMark(0, new Coordinate(-43.639198, 172.675748));
        BoundaryMark boundary2 = new BoundaryMark(1, new Coordinate(-43.623970, 172.677429));
        BoundaryMark boundary3 = new BoundaryMark(2, new Coordinate(-43.628784, 172.668017));
        BoundaryMark boundary4 = new BoundaryMark(3, new Coordinate(-43.631407, 172.682455));
        List<BoundaryMark> boundaries = new ArrayList<>();

        boundaries.add(boundary1);
        boundaries.add(boundary2);
        boundaries.add(boundary3);
        boundaries.add(boundary4);

        List<CompoundMark> marks = new ArrayList<>();
        List<MarkRounding> markRoundings = new ArrayList<>();

        course = new Course(marks, boundaries, 0.0, 0.0, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-3)), markRoundings);

        course.setCentralCoordinate(new Coordinate(-43.630318, 172.675192));

        //Make pane
        pane = new Pane();
        pane.setMaxSize(500, 500);
        pane.setPrefSize(500, 500);
        pane.setMinSize(500, 500);

        //Make mapper
        mapper = new PixelMapper(course, pane);

        //get mapping ratio
        double mappingRatio = mapper.mappingRatio();

        GPSCalculations geoCalculator = new GPSCalculations();

        for (int i = 0; i < boundaries.size(); i++) {
            for (int j = 0; j < boundaries.size(); j++) {

                BoundaryMark boundaryMark1 = boundaries.get(i);
                XYPair xy1 = mapper.coordToPixel(boundaryMark1.getCoordinate());
                BoundaryMark boundaryMark2 = boundaries.get(j);
                XYPair xy2 = mapper.coordToPixel(boundaryMark2.getCoordinate());

                double geoDistance = geoCalculator.distance(boundaryMark1.getCoordinate(), boundaryMark2.getCoordinate());
                double xyDistance = xy1.calculateDistance(xy2);

                assertEquals(geoDistance, xyDistance / mappingRatio, 200);
            }
        }
    }


    @Ignore
    @Test
    public void mappingRatioOnLargeCourseTest() {
        //Make course
        BoundaryMark boundary1 = new BoundaryMark(0, new Coordinate(-43.546552, 173.297102));
        BoundaryMark boundary2 = new BoundaryMark(1, new Coordinate(-43.228048, 173.190999));
        BoundaryMark boundary3 = new BoundaryMark(2, new Coordinate(-42.956155, 173.454153));
        BoundaryMark boundary4 = new BoundaryMark(3, new Coordinate(-43.352464, 173.463651));
        List<BoundaryMark> boundaries = new ArrayList<>();

        boundaries.add(boundary1);
        boundaries.add(boundary2);
        boundaries.add(boundary3);
        boundaries.add(boundary4);

        List<CompoundMark> marks = new ArrayList<>();
        List<MarkRounding> markRoundings = new ArrayList<>();

        course = new Course(marks, boundaries, 0.0, 0.0, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-3)), markRoundings);

        course.setCentralCoordinate(new Coordinate(-43.273253, 173.337622));

        //Make pane
        pane = new Pane();
        pane.setMaxSize(500, 500);
        pane.setPrefSize(500, 500);
        pane.setMinSize(500, 500);

        //Make mapper
        mapper = new PixelMapper(course, pane);

        //get mapping ratio
        double mappingRatio = mapper.mappingRatio();

        GPSCalculations geoCalculator = new GPSCalculations();

        for (int i = 0; i < boundaries.size(); i++) {
            for (int j = 0; j < boundaries.size(); j++) {

                BoundaryMark boundaryMark1 = boundaries.get(i);
                XYPair xy1 = mapper.coordToPixel(boundaryMark1.getCoordinate());
                BoundaryMark boundaryMark2 = boundaries.get(j);
                XYPair xy2 = mapper.coordToPixel(boundaryMark2.getCoordinate());

                double geoDistance = geoCalculator.distance(boundaryMark1.getCoordinate(), boundaryMark2.getCoordinate());
                double xyDistance = xy1.calculateDistance(xy2);

                assertEquals(geoDistance, xyDistance / mappingRatio, 200);
            }
        }
    }


    @Ignore
    @Test
    public void mappingRatioOnTinyCourseTest() {
        //Test for when 1 k above centre is outside of course
        //Make course
        BoundaryMark boundary1 = new BoundaryMark(0, new Coordinate(-43.556333, 172.709354));
        BoundaryMark boundary2 = new BoundaryMark(1, new Coordinate(-43.549351, 172.709367));
        BoundaryMark boundary3 = new BoundaryMark(2, new Coordinate(-43.551943, 172.705137));
        BoundaryMark boundary4 = new BoundaryMark(3, new Coordinate(-43.552264, 172.712611));
        List<BoundaryMark> boundaries = new ArrayList<>();

        boundaries.add(boundary1);
        boundaries.add(boundary2);
        boundaries.add(boundary3);
        boundaries.add(boundary4);

        List<CompoundMark> marks = new ArrayList<>();
        List<MarkRounding> markRoundings = new ArrayList<>();

        course = new Course(marks, boundaries, 0.0, 0.0, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-3)), markRoundings);

        course.setCentralCoordinate(new Coordinate(-43.552622, 172.708628));

        //Make pane
        pane = new Pane();
        pane.setMaxSize(500, 500);
        pane.setPrefSize(500, 500);
        pane.setMinSize(500, 500);

        //Make mapper
        mapper = new PixelMapper(course, pane);

        //get mapping ratio
        double mappingRatio = mapper.mappingRatio();

        GPSCalculations geoCalculator = new GPSCalculations();

        for (int i = 0; i < boundaries.size(); i++) {
            for (int j = 0; j < boundaries.size(); j++) {

                BoundaryMark boundaryMark1 = boundaries.get(i);
                XYPair xy1 = mapper.coordToPixel(boundaryMark1.getCoordinate());
                BoundaryMark boundaryMark2 = boundaries.get(j);
                XYPair xy2 = mapper.coordToPixel(boundaryMark2.getCoordinate());

                double geoDistance = geoCalculator.distance(boundaryMark1.getCoordinate(), boundaryMark2.getCoordinate());
                double xyDistance = xy1.calculateDistance(xy2);

                assertEquals(geoDistance, xyDistance / mappingRatio, 200);
            }
        }
    }


    /**
     * Test method for manual testing and investigation of pixel mapper behaviour
     */
    @Ignore
    @Test
    public void testDummy() {

        // Copy of pixel mapper for manipulation in manual testing
        // Has private methods exposed
        class TestPixelMapper {
            private final Course course;
            private final Pane pane;
            private Coordinate viewPortCenter;
            private final IntegerProperty zoomLevel = new SimpleIntegerProperty(0);
            public List<Coordinate> bounds; // 2 coordinates: NW bound, SE bound
            private GPSCalculations gpsCalculations;
            public final int NW_BOUND_INDEX = 0; // Used in bounds
            public final int SE_BOUND_INDEX = 1; // Used in bounds
            private final double MAP_SCALE_CORRECTION = 1;

            public TestPixelMapper(Course course, Pane pane) {
                this.course = course;
                this.pane = pane;
                gpsCalculations = new GPSCalculations();
                bounds = gpsCalculations.findMinMaxPoints(course);
                viewPortCenter = course.getCentralCoordinate();
            }

            public double getZoomFactor() {
                return Math.pow(2, zoomLevel.intValue());
            }

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
                mappingScale *= MAP_SCALE_CORRECTION * getZoomFactor();

                XYPair worldCoordinates = coordinateToPlane(coord);
                XYPair viewCenter = coordinateToPlane(viewPortCenter);

                double dX = worldCoordinates.getX() - viewCenter.getX();
                double dY = worldCoordinates.getY() - viewCenter.getY();

                double x = (dX * mappingScale) + (paneWidth / 2);
                double y = (dY * mappingScale) + (paneHeight / 2);

                return new XYPair(x, y);
            }

            private double calcCourseWidth() {
                Coordinate west = new Coordinate(course.getCentralCoordinate().getLongitude(), bounds.get(NW_BOUND_INDEX).getLongitude());
                Coordinate east = new Coordinate(course.getCentralCoordinate().getLongitude(), bounds.get(SE_BOUND_INDEX).getLongitude());

                double dWest = gpsCalculations.distance(west, course.getCentralCoordinate());
                double dEast = gpsCalculations.distance(course.getCentralCoordinate(), east);

                Coordinate furthest = (dWest > dEast) ? west : east;
                return Math.abs(coordinateToPlane(course.getCentralCoordinate()).getX() - coordinateToPlane(furthest).getX()) * 2;
            }

            private double calcCourseHeight() {
                Coordinate north = new Coordinate(bounds.get(NW_BOUND_INDEX).getLatitude(), course.getCentralCoordinate().getLatitude());
                Coordinate south = new Coordinate(bounds.get(SE_BOUND_INDEX).getLatitude(), course.getCentralCoordinate().getLatitude());

                double dNorth = gpsCalculations.distance(north, course.getCentralCoordinate());
                double dSouth = gpsCalculations.distance(south, course.getCentralCoordinate());

                Coordinate furthest = (dNorth > dSouth) ? north : south;
                return Math.abs(coordinateToPlane(course.getCentralCoordinate()).getY() - coordinateToPlane(furthest).getY()) * 2;
            }

            private double webMercatorLongitude(double longitude) {
                return 128 * (longitude + Math.PI) / Math.PI;
            }

            private double webMercatorLatitude(double latitude) {
                double correctiveFactor = 1 / Math.cos(Math.toRadians(latitude));
//            double correctiveFactor = 1;
                return 128 * correctiveFactor * (Math.PI - Math.log(Math.tan((Math.PI / 4) + (latitude / 2)))) / Math.PI;
            }

            private XYPair coordinateToPlane(Coordinate point) {
                return new XYPair(webMercatorLongitude(point.getLongitude()), webMercatorLatitude(point.getLatitude()));
            }
        }


        GPSCalculations gps = new GPSCalculations();
        double[] distances = {10, 100, 500, 1000, 2500, 5000, 10000, 20000};
        for (double distance : distances) {
            Coordinate centre = new Coordinate(51.58072, -3.98796);
            BoundaryMark n = new BoundaryMark(1, gps.toCoordinate(centre, 0, distance));
            BoundaryMark e = new BoundaryMark(1, gps.toCoordinate(centre, 90, distance));
            BoundaryMark s = new BoundaryMark(1, gps.toCoordinate(centre, 180, distance));
            BoundaryMark w = new BoundaryMark(1, gps.toCoordinate(centre, 270, distance));

            Course course = new Course(
                    Collections.emptyList(),
                    Arrays.asList(n, e, s, w),
                    0.0,
                    0.0,
                    ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-4)),
                    Collections.emptyList()
            );

            course.setCentralCoordinate(centre);

            Pane pane = new Pane();
            pane.setMinSize(400, 400);
            pane.setPrefSize(400, 400);
            pane.setMaxSize(400, 400);

            TestPixelMapper pixelMapper = new TestPixelMapper(course, pane);

            XYPair middle = pixelMapper.coordToPixel(centre);
            XYPair t = pixelMapper.coordToPixel(n.getCoordinate());
            XYPair r = pixelMapper.coordToPixel(e.getCoordinate());
            XYPair b = pixelMapper.coordToPixel(s.getCoordinate());
            XYPair l = pixelMapper.coordToPixel(w.getCoordinate());

//            System.out.println("up   : " + (middle.getY() - t.getY()));
//            System.out.println("right: " + (r.getX() - middle.getX()));
//            System.out.println("down : " + (b.getY() - middle.getY()));
//            System.out.println("left : " + (middle.getX() - l.getX()));
            System.out.println("" + distance + ":");
            //           System.out.println("" + centre.getLatitude() + ", " + centre.getLongitude());
            //           course.getBoundaries().stream().map(BoundaryMark::getCoordinate).forEach(coordinate -> System.out.println("" + coordinate.getLatitude() + ", " + coordinate.getLongitude()));
            //           System.out.println();
            System.out.println("    height: " + pixelMapper.calcCourseHeight());
            System.out.println("    width : " + pixelMapper.calcCourseWidth());
            System.out.println("    NW   : " + pixelMapper.coordToPixel(pixelMapper.bounds.get(pixelMapper.NW_BOUND_INDEX)));
            System.out.println("    SE   : " + pixelMapper.coordToPixel(pixelMapper.bounds.get(pixelMapper.SE_BOUND_INDEX)));
            System.out.println("    mid  : " + middle + "    " + pixelMapper.coordinateToPlane(centre));
            System.out.println("    up   : " + t + "    " + pixelMapper.coordinateToPlane(n.getCoordinate()));
            System.out.println("    right: " + r + "    " + pixelMapper.coordinateToPlane(e.getCoordinate()));
            System.out.println("    down : " + b + "    " + pixelMapper.coordinateToPlane(s.getCoordinate()));
            System.out.println("    left : " + l + "    " + pixelMapper.coordinateToPlane(w.getCoordinate()));
            System.out.println("    dN: " + (pixelMapper.coordinateToPlane(centre).getY() - pixelMapper.coordinateToPlane(n.getCoordinate()).getY()));
            System.out.println("    dE: " + (pixelMapper.coordinateToPlane(e.getCoordinate()).getX() - pixelMapper.coordinateToPlane(centre).getX()));
            System.out.println("    dS: " + (pixelMapper.coordinateToPlane(s.getCoordinate()).getY() - pixelMapper.coordinateToPlane(centre).getY()));
            System.out.println("    dW: " + (pixelMapper.coordinateToPlane(centre).getX() - pixelMapper.coordinateToPlane(w.getCoordinate()).getX()));
        }
    }
}
