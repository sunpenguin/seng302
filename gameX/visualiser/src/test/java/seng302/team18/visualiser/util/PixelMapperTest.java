package seng302.team18.visualiser.util;

import javafx.scene.layout.Pane;
import org.junit.Ignore;
import org.junit.Test;
import seng302.team18.model.*;
import seng302.team18.util.GPSCalculations;
import seng302.team18.util.XYPair;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
        GPSCalculations gps = new GPSCalculations();
        List<Coordinate> bounds = gps.findMinMaxPoints(course);
        mapper = new PixelMapper(bounds.get(0), bounds.get(1), course.getCentralCoordinate(), pane);

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
        GPSCalculations gps = new GPSCalculations();
        List<Coordinate> bounds = gps.findMinMaxPoints(course);
        mapper = new PixelMapper(bounds.get(0), bounds.get(1), course.getCentralCoordinate(), pane);

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
        GPSCalculations gps = new GPSCalculations();
        List<Coordinate> bounds = gps.findMinMaxPoints(course);
        mapper = new PixelMapper(bounds.get(0), bounds.get(1), course.getCentralCoordinate(), pane);

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


}
