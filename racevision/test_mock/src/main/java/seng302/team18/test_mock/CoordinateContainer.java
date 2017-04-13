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
        StreamResult result = new StreamResult(new File("racevision/test_mock/src/main/resources/race.xml"));
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
            startTime.setAttribute("Time","2011-08-06T13:30:00-0700");
            startTime.setAttribute("Postpone","false");

            Element participants = doc.createElement("Participants");
            race.appendChild(participants);

            Element yacht1 = doc.createElement("Yacht");
            Element yacht2 = doc.createElement("Yacht");
            participants.appendChild(yacht1);
            participants.appendChild(yacht2);

            yacht1.setAttribute("SourceID","107");
            yacht1.setAttribute("Entry","Port");
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
            s1.setAttribute("TargetLat", String.valueOf(CourseMap.get("StartLine1").getLatitude()));
            s1.setAttribute("Name","PRO");
            s1.setAttribute("SeqID","1");
            s2.setAttribute("SourceID","102");
            s2.setAttribute("TargetLng", String.valueOf(CourseMap.get("StartLine2").getLongitude()));
            s2.setAttribute("TargetLat", String.valueOf(CourseMap.get("StartLine2").getLatitude()));
            s2.setAttribute("Name","PIN");
            s2.setAttribute("SeqID","2");

            Element compoundMark2 = doc.createElement("CompoundMark");
            course.appendChild(compoundMark2);
            compoundMark2.setAttribute("Name","M1");
            compoundMark2.setAttribute("CompoundMarkID","2");
            Element m1 = doc.createElement("Mark");
            compoundMark2.appendChild(m1);
            m1.setAttribute("SourceID","103");
            m1.setAttribute("TargetLng", String.valueOf(CourseMap.get("Mark1").getLongitude()));
            m1.setAttribute("TargetLat", String.valueOf(CourseMap.get("Mark1").getLatitude()));
            m1.setAttribute("Name","M1");

            Element compoundMark3 = doc.createElement("CompoundMark");
            course.appendChild(compoundMark3);
            compoundMark3.setAttribute("Name","M2");
            compoundMark3.setAttribute("CompoundMarkID","3");
            Element m2 = doc.createElement("Mark");
            compoundMark3.appendChild(m2);
            m2.setAttribute("SourceID","104");
            m2.setAttribute("TargetLng", String.valueOf(CourseMap.get("Mark2").getLongitude()));
            m2.setAttribute("TargetLat", String.valueOf(CourseMap.get("Mark2").getLatitude()));
            m2.setAttribute("Name","M2");

            Element compoundMark4 = doc.createElement("CompoundMark");
            course.appendChild(compoundMark4);
            compoundMark4.setAttribute("Name","Gate");
            compoundMark4.setAttribute("CompoundMarkID","4");

            Element g1 = doc.createElement("Mark");
            Element g2 = doc.createElement("Mark");
            compoundMark4.appendChild(g1);
            compoundMark4.appendChild(g2);
            g1.setAttribute("SourceID","104");
            g1.setAttribute("TargetLng", String.valueOf(CourseMap.get("Gate1").getLongitude()));
            g1.setAttribute("TargetLat", String.valueOf(CourseMap.get("Gate1").getLatitude()));
            g1.setAttribute("Name","G1");
            g1.setAttribute("SeqID","1");
            g2.setAttribute("SourceID","105");
            g2.setAttribute("TargetLng", String.valueOf(CourseMap.get("Gate2").getLongitude()));
            g2.setAttribute("TargetLat", String.valueOf(CourseMap.get("Gate2").getLatitude()));
            g2.setAttribute("Name","G2");
            g2.setAttribute("SeqID","2");

            Element compoundMarkSeq = doc.createElement("CompoundMarkSequence");
            race.appendChild(compoundMarkSeq);
            Element compoundMarkSeq1 = doc.createElement("Corner");
            compoundMarkSeq.appendChild(compoundMarkSeq1);
            compoundMarkSeq1.setAttribute("SeqID","1");
            compoundMarkSeq1.setAttribute("CompoundMarkID","1");
            compoundMarkSeq1.setAttribute("Rounding","SP");
            compoundMarkSeq1.setAttribute("ZoneSize","3");

            Element compoundMarkSeq2 = doc.createElement("Corner");
            compoundMarkSeq.appendChild(compoundMarkSeq2);
            compoundMarkSeq2.setAttribute("SeqID","2");
            compoundMarkSeq2.setAttribute("CompoundMarkID","2");
            compoundMarkSeq2.setAttribute("Rounding","Port");
            compoundMarkSeq2.setAttribute("ZoneSize","3");

            Element compoundMarkSeq3 = doc.createElement("Corner");
            compoundMarkSeq.appendChild(compoundMarkSeq3);
            compoundMarkSeq3.setAttribute("SeqID","3");
            compoundMarkSeq3.setAttribute("CompoundMarkID","3");
            compoundMarkSeq3.setAttribute("Rounding","Stbd");
            compoundMarkSeq3.setAttribute("ZoneSize","6");

            Element compoundMarkSeq4 = doc.createElement("Corner");
            compoundMarkSeq.appendChild(compoundMarkSeq4);
            compoundMarkSeq4.setAttribute("SeqID","4");
            compoundMarkSeq4.setAttribute("CompoundMarkID","4");
            compoundMarkSeq4.setAttribute("Rounding","PS");
            compoundMarkSeq4.setAttribute("ZoneSize","6");

            Element compoundMarkSeq5 = doc.createElement("Corner");
            compoundMarkSeq.appendChild(compoundMarkSeq5);
            compoundMarkSeq5.setAttribute("SeqID","5");
            compoundMarkSeq5.setAttribute("CompoundMarkID","1");
            compoundMarkSeq5.setAttribute("Rounding","SP");
            compoundMarkSeq5.setAttribute("ZoneSize","3");

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            try {
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                DOMSource source = new DOMSource(doc);
//                StreamResult result =  new StreamResult(System.out);
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