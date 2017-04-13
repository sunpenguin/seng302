package seng302.team18.test_mock;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

/**
 * Created by jth102 on 11/04/17.
 */
public class CoordinateContainer {

    private Map<Integer, List> ACLocationsMap;
    private Map<String, Coordinate> CourseMap;

    public CoordinateContainer() {
        ACLocationsMap = new HashMap();
        CourseMap = new HashMap<>();
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

        System.out.println("center"+center);
        isleOfWhiteLocations.add(center);

        ACLocationsMap.put(1, isleOfWhiteLocations);
    }

    private void setUpCourseMap() {
        List<Coordinate> referenceBoundary = ACLocationsMap.get(1);

        Random ran = new Random();
        //  startline
        double rangeStartLat = referenceBoundary.get(4).getLatitude() - referenceBoundary.get(2).getLatitude();
        double startLine1Lat = ran.nextDouble() * rangeStartLat + referenceBoundary.get(2).getLatitude();

        double rangeStartLon = referenceBoundary.get(4).getLongitude() - referenceBoundary.get(2).getLongitude();
        double startLine1Lon = ran.nextDouble() * rangeStartLon + referenceBoundary.get(2).getLongitude();
        Coordinate startLine1 = new Coordinate(startLine1Lat, startLine1Lon);
        System.out.println("s1" + startLine1);
        CourseMap.put("StartLine1", startLine1);
        Coordinate startLine2 = GPSCalculations.coordinateToCoordinate(startLine1,100,270);
        CourseMap.put("StartLine2", startLine2);
        System.out.println("s2"+startLine2);

        // marks
        Coordinate mark1 = GPSCalculations.coordinateToCoordinate(startLine2,45,300);
        CourseMap.put("Mark1", mark1);
        System.out.println("m1"+mark1);

        double mark2Lat = referenceBoundary.get(4).getLatitude();
        double mark2Lon = referenceBoundary.get(4).getLongitude();
        Coordinate mark2 = GPSCalculations.coordinateToCoordinate(new Coordinate(mark2Lat, mark2Lon), 200,400);
        CourseMap.put("Mark2", mark2);
        System.out.println("m2"+mark2);

        // gate
        double rangeGateLat = referenceBoundary.get(1).getLatitude() - referenceBoundary.get(4).getLatitude();
        double gateLat = ran.nextDouble() * rangeStartLat + referenceBoundary.get(4).getLatitude();

        double rangeGateLon = referenceBoundary.get(1).getLongitude() - referenceBoundary.get(4).getLongitude();
        double gateLon = ran.nextDouble() * rangeStartLon + referenceBoundary.get(4).getLongitude();
        Coordinate gate1 = new Coordinate(gateLat, gateLon);
        CourseMap.put("Gate1", gate1);
        System.out.println("g1"+gate1);

        Coordinate gate2 = GPSCalculations.coordinateToCoordinate(gate1,100,300);
        CourseMap.put("Gate2", gate2);
        System.out.println("g2"+gate2);

        //now we generate the race.xml
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element race = doc.createElement("Race");
            doc.appendChild(race);

            Element raceId = doc.createElement("RaceID");
            raceId.appendChild(doc.createTextNode("11080703"));
            race.appendChild(raceId);

            Element raceType = doc.createElement("RaceType");
            raceType.appendChild(doc.createTextNode("Match"));
            race.appendChild(raceType);

            Element timeDate = doc.createElement("CreationTimeDate");
            timeDate.appendChild(doc.createTextNode("2011-08-06T13:25:00-0000"));
            race.appendChild(timeDate);

            Element startTime = doc.createElement("RaceStartTime");
            race.appendChild(startTime);
            startTime.setAttribute("Postpone","false");
            startTime.setAttribute("Time","2011-08-06T13:30:00-0700");

            Element participants = doc.createElement("Participants");
            race.appendChild(participants);

            Element yacht1 = doc.createElement("Yacht");
            Element yacht2 = doc.createElement("Yacht");
            participants.appendChild(yacht1);
            participants.appendChild(yacht2);

            yacht1.setAttribute("Entry","Port");
            yacht1.setAttribute("SourceID","107");
            yacht2.setAttribute("Entry","Stbd");
            yacht2.setAttribute("SourceID","108");

            Element course = doc.createElement("Course");
            race.appendChild(course);

            Element compoundMark1 = doc.createElement("CompoundMark");
            course.appendChild(compoundMark1);
            compoundMark1.setAttribute("Name","StartLine");
            compoundMark1.setAttribute("CompoundMarkID","1");
            Element s1 = doc.createElement("Mark");
            Element s2 = doc.createElement("Mark");
            compoundMark1.appendChild(s1);
            compoundMark1.appendChild(s2);
            s1.setAttribute("SourceID","101");
            s1.setAttribute("TargetLng", String.valueOf(CourseMap.get("StartLine1").getLongitude()));
            s1.setAttribute("TargetLat", String.valueOf(CourseMap.get("StartLine1").getLongitude()));
            s1.setAttribute("Name","PRO");
            s1.setAttribute("SeqID","1");
            s2.setAttribute("SourceID","102");
            s2.setAttribute("TargetLng", String.valueOf(CourseMap.get("StartLine2").getLongitude()));
            s2.setAttribute("TargetLat", String.valueOf(CourseMap.get("StartLine2").getLongitude()));
            s2.setAttribute("Name","PIN");
            s2.setAttribute("SeqID","2");

            Element compoundMark2 = doc.createElement("CompoundMark");
            course.appendChild(compoundMark2);
            compoundMark2.setAttribute("Name","M1");
            compoundMark2.setAttribute("CompoundMarkID","2");
            Element m1 = doc.createElement("Mark");

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            try {
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result =  new StreamResult(System.out);
//                StreamResult result = new StreamResult(new File("C:\\file.xml"));
                try {
                    transformer.transform(source, result);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}