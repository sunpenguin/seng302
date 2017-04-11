package seng302.team18.test_mock;

import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;

import java.util.*;

/**
 * Created by jth102 on 11/04/17.
 */
public class CoordinateContainer {

    private Map<Integer, List> ACLocationsMap;
    private Map<String, Coordinate> CourseMap;

    public CoordinateContainer() {
        ACLocationsMap = new HashMap();
        setupLocationMap();
        setUpCourseMap();
    }

    private void setupLocationMap() {
        // Isle of Whight
        List<Coordinate> isleOfWhiteLocations = new ArrayList();

        Coordinate topRight = new Coordinate(50.7735, -1.3825);
        Coordinate bottomRight = new Coordinate(50.7503, -1.3609);
        Coordinate topLeft = new Coordinate(50.7459, -1.4897);
        Coordinate bottomLeft = new Coordinate(50.7225, -1.466);

        isleOfWhiteLocations.add(topLeft);
        isleOfWhiteLocations.add(topRight);
        isleOfWhiteLocations.add(bottomLeft);
        isleOfWhiteLocations.add(bottomRight);

        Coordinate center = GPSCalculations.getCentralCoordinate(isleOfWhiteLocations);

        isleOfWhiteLocations.add(center);

        ACLocationsMap.put(1, isleOfWhiteLocations);
    }

    private void setUpCourseMap() {
        List<Coordinate> referenceBoundary = ACLocationsMap.get(1);

        Random ran = new Random();

        //startline
        double rangeStartLat = referenceBoundary.get(4).getLatitude() - referenceBoundary.get(2).getLatitude();
        double startLine1Lat = ran.nextDouble() * rangeStartLat + referenceBoundary.get(2).getLatitude();
        double rangeStartLon = referenceBoundary.get(4).getLongitude() - referenceBoundary.get(2).getLongitude();
        double startLine1Lon = ran.nextDouble() * rangeStartLon + referenceBoundary.get(2).getLongitude();
        Coordinate startLine1 = new Coordinate(startLine1Lat, startLine1Lon);
        CourseMap.put("StartLine1", startLine1);
        Coordinate startLine2 = GPSCalculations.coordinateToCoordinate(startLine1,100,1212);
        CourseMap.put("StartLine2", startLine2);

        // mark
        double rangeMarkLat = referenceBoundary.get(4).getLatitude();
        double rangeMarkLon = referenceBoundary.get(4).getLongitude();
        CourseMap.put("Mark", new Coordinate(rangeMarkLat, rangeMarkLon));
    }
}
