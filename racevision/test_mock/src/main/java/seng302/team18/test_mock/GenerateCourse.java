package seng302.team18.test_mock;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;
import seng302.team18.util.XYPair;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by hqi19 on 14/04/17.
 */
public class GenerateCourse {

    private Map<String, Coordinate> CourseMap;
    private MockData data;
    private Coordinate center;
    private Polygon polygon;

    public GenerateCourse (MockData mockData) {
        data = mockData;
        center = data.centreCoordinate;
        polygon = new Polygon();
        CoordinateContainer coordinates = new CoordinateContainer();
        CourseMap = new HashMap<>();
        setUpCourseMap();
    }

    private void setUpCourseMap() {
        StreamResult result = new StreamResult(new File("racevision/test_mock/src/main/resources/race.xml"));
        List<Coordinate> referenceBoundary = data.boundaries;

        for (int i = 0; i < referenceBoundary.size(); i ++) {
            XYPair xy = GPSCalculations.GPSxy(referenceBoundary.get(i));
            polygon.addPoint((int)xy.getX(), (int)xy.getY());

            System.out.println(referenceBoundary.get(i).getLatitude() + ", " + referenceBoundary.get(i).getLongitude());
        }

        XYPair xy = GPSCalculations.GPSxy(referenceBoundary.get(0));
        polygon.addPoint((int) xy.getX(), (int) xy.getY());

        Random ran = new Random();
        //  startline
        double rangeStartLat = center.getLatitude() - referenceBoundary.get(3).getLatitude();
        double startLine1Lat = ran.nextDouble() * rangeStartLat + referenceBoundary.get(3).getLatitude();

        double rangeStartLon = center.getLongitude() - referenceBoundary.get(3).getLongitude();
        double startLine1Lon = ran.nextDouble() * rangeStartLon + referenceBoundary.get(3).getLongitude();
        Coordinate startLine1 = new Coordinate(startLine1Lat, startLine1Lon);

        XYPair xyStart1 = GPSCalculations.GPSxy(startLine1);
        System.out.println(polygon.contains(xyStart1.getX(), xyStart1.getY()));

        System.out.println(startLine1);
        CourseMap.put("StartLine1", startLine1);
        Coordinate startLine2 = GPSCalculations.coordinateToCoordinate(startLine1,100,270);

        XYPair xyStart2 = GPSCalculations.GPSxy(startLine2);
        System.out.println(polygon.contains(xyStart2.getX(), xyStart2.getY()));

        CourseMap.put("StartLine2", startLine2);
        System.out.println(startLine2);

        // marks
        Coordinate mark1 = GPSCalculations.coordinateToCoordinate(startLine2,45,300);
        CourseMap.put("Mark1", mark1);
        System.out.println(mark1);

        double mark2Lat = center.getLatitude();
        double mark2Lon = center.getLongitude();
        Coordinate mark2 = GPSCalculations.coordinateToCoordinate(new Coordinate(mark2Lat, mark2Lon), 200,400);
        CourseMap.put("Mark2", mark2);
        System.out.println(mark2);

        // gate
        double rangeGateLat = referenceBoundary.get(1).getLatitude() - center.getLatitude();
        double gateLat = ran.nextDouble() * rangeGateLat + center.getLatitude();

        double rangeGateLon = referenceBoundary.get(1).getLongitude() - center.getLongitude();
        double gateLon = ran.nextDouble() * rangeGateLon + center.getLongitude();
        Coordinate gate1 = new Coordinate(gateLat, gateLon);

        XYPair xyGate1 = GPSCalculations.GPSxy(gate1);
        System.out.println(polygon.contains(xyGate1.getX(), xyGate1.getY()));

        CourseMap.put("Gate1", gate1);
        System.out.println(gate1);

        Coordinate gate2 = GPSCalculations.coordinateToCoordinate(gate1,100,300);

        System.out.println("GATE2:");
        XYPair xyGate2 = GPSCalculations.GPSxy(gate2);
        System.out.println(polygon.contains(xyGate2.getX(), xyGate2.getY()));

        CourseMap.put("Gate2", gate2);
        System.out.println(gate2);

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

            Element courselimit = doc.createElement("CourseLimit");
            race.appendChild(courselimit);

            Element limit1 = doc.createElement("Limit");
            courselimit.appendChild(limit1);
            limit1.setAttribute("SeqID","1");
            limit1.setAttribute("Lat", String.valueOf(referenceBoundary.get(0).getLatitude()));
            limit1.setAttribute("Lon",String.valueOf(referenceBoundary.get(0).getLongitude()));

            Element limit2 = doc.createElement("Limit");
            courselimit.appendChild(limit2);
            limit2.setAttribute("SeqID","2");
            limit2.setAttribute("Lat", String.valueOf(referenceBoundary.get(1).getLatitude()));
            limit2.setAttribute("Lon",String.valueOf(referenceBoundary.get(1).getLongitude()));

            Element limit3 = doc.createElement("Limit");
            courselimit.appendChild(limit3);
            limit3.setAttribute("SeqID","3");
            limit3.setAttribute("Lat", String.valueOf(referenceBoundary.get(3).getLatitude()));
            limit3.setAttribute("Lon",String.valueOf(referenceBoundary.get(3).getLongitude()));

            Element limit4 = doc.createElement("Limit");
            courselimit.appendChild(limit4);
            limit4.setAttribute("SeqID","4");
            limit4.setAttribute("Lat", String.valueOf(referenceBoundary.get(2).getLatitude()));
            limit4.setAttribute("Lon",String.valueOf(referenceBoundary.get(2).getLongitude()));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            try {
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                DOMSource source = new DOMSource(doc);
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

    public Map<String, Coordinate> getCourseMap() {return CourseMap;}
}
