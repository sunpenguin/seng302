package seng302.team18.test_mock;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

/**
 * Created by jth102 on 11/04/17.
 */
public class CoordinateContainer {

    private Map<Integer, List> ACLocationsMap;

    public CoordinateContainer() {
        ACLocationsMap = new HashMap();
        setupLocationMap();
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

//        System.out.println("center"+center);
        isleOfWhiteLocations.add(center);

        ACLocationsMap.put(1, isleOfWhiteLocations);
    }

    public Map<Integer, List> getLocationMap() {return ACLocationsMap;}
}