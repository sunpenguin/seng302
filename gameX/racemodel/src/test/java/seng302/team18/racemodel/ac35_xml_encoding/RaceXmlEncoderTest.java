package seng302.team18.racemodel.ac35_xml_encoding;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import seng302.team18.message.AC35RaceXMLComponents;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.XmlMessage;
import seng302.team18.model.*;

import javax.xml.transform.dom.DOMSource;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for RaceXmlEncoder class.
 */
public class RaceXmlEncoderTest {

    private RaceXmlEncoder raceXmlEncoder = new RaceXmlEncoder();
    private AC35XMLRaceMessage raceMessage;
    private Element root;
    private Map<Integer, CompoundMark> encodedCompoundMarks = new HashMap<>();
    private LocalTime beforeEncoding;
    private LocalTime afterEncoding;

    /**
     * Set up a AC35 standard race message.
     */
    private void setUpRaceMessage() {
        String startTime;
        Map<Integer, AC35XMLRaceMessage.EntryDirection> participants;
        List<CompoundMark> compoundMarks;
        List<MarkRounding> markRoundings;
        List<Coordinate> boundaryMarks;
        int raceID;

        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss-Z");
        startTime = ZonedDateTime.now().format(DATE_TIME_FORMATTER);

        participants = new HashMap<>();

        for (Integer i = 107; i < 109; i++) {
            participants.put(i, (i % 2 == 0) ? AC35XMLRaceMessage.EntryDirection.PORT : AC35XMLRaceMessage.EntryDirection.STARBOARD);
        }

        Mark mark1 = new Mark(1, new Coordinate(22, 21));
        Mark mark2 = new Mark(2, new Coordinate(32, 31));
        Mark mark3 = new Mark(3, new Coordinate(12, 11));
        Mark mark4 = new Mark(4, new Coordinate(52, 51));
        Mark mark5 = new Mark(5, new Coordinate(42, 41));
        Mark mark6 = new Mark(6, new Coordinate(52, 51));

        List<Mark> startMarks = new ArrayList<>();
        startMarks.add(mark1);
        startMarks.add(mark2);

        List<Mark> finishMarks = new ArrayList<>();
        finishMarks.add(mark5);
        finishMarks.add(mark6);

        List<Mark> marks1 = new ArrayList<>();
        marks1.add(mark3);

        List<Mark> marks2 = new ArrayList<>();
        marks2.add(mark4);

        CompoundMark compoundMark1 = new CompoundMark("StartLine", startMarks, 102);
        CompoundMark compoundMark2 = new CompoundMark("Mark1", marks1, 103);
        CompoundMark compoundMark3 = new CompoundMark("Mark2", marks2, 104);
        CompoundMark compoundMark4 = new CompoundMark("FinishLine", finishMarks, 105);

        compoundMarks = new ArrayList<>();
        compoundMarks.add(compoundMark1);
        compoundMarks.add(compoundMark2);
        compoundMarks.add(compoundMark3);
        compoundMarks.add(compoundMark4);

        MarkRounding markRounding1 = new MarkRounding(1, compoundMark1, MarkRounding.Direction.SP, 3);
        MarkRounding markRounding2 = new MarkRounding(2, compoundMark2, MarkRounding.Direction.SP, 3);
        MarkRounding markRounding3 = new MarkRounding(3, compoundMark3, MarkRounding.Direction.SP, 3);
        MarkRounding markRounding4 = new MarkRounding(4, compoundMark4, MarkRounding.Direction.SP, 3);

        markRoundings = new ArrayList<>();
        markRoundings.add(markRounding1);
        markRoundings.add(markRounding2);
        markRoundings.add(markRounding3);
        markRoundings.add(markRounding4);

        Coordinate b1 = new Coordinate(33, 34);
        Coordinate b2 = new Coordinate(13, 14);
        Coordinate b3 = new Coordinate(53, 54);
        Coordinate b4 = new Coordinate(43, 44);

        boundaryMarks = new ArrayList<>();
        boundaryMarks.add(b1);
        boundaryMarks.add(b2);
        boundaryMarks.add(b3);
        boundaryMarks.add(b4);

        raceID = 123456;

        raceMessage = new AC35XMLRaceMessage();
        raceMessage.setRaceID(raceID);
        raceMessage.setBoundaryMarks(boundaryMarks);
        raceMessage.setCompoundMarks(compoundMarks);
        raceMessage.setMarkRoundings(markRoundings);
        raceMessage.setParticipants(participants);
        raceMessage.setStartTime(startTime);
        raceMessage.setRaceType(RaceType.MATCH);
    }


    /**
     * Get DOMSource from the RaceXmlEncoder in order to use it to run tests below.
     *
     * @throws Exception parseConfigurationException
     */
    @Before
    public void setUp() throws Exception {
        setUpRaceMessage();

        beforeEncoding = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        DOMSource domSource = raceXmlEncoder.getDomSource(raceMessage);
        afterEncoding = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

        Document doc = (Document) domSource.getNode();
        doc.getDocumentElement().normalize();
        root = (Element) doc.getElementsByTagName(AC35RaceXMLComponents.ROOT_RACE.toString()).item(0);
    }


    @Test
    public void encodeRaceId() throws Exception {
        Element raceIdElement = (Element) root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_RACE_ID.toString()).item(0);
        int encodedId = Integer.parseInt(raceIdElement.getTextContent());

        assertEquals("Race id has not been encoded correctly", raceMessage.getRaceID(), encodedId);
    }


    @Test
    public void encodeRaceType() throws Exception {
        Element raceTypeElement = (Element) root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_RACE_TYPE.toString()).item(0);

        assertEquals("Race type has not been encoded correctly", raceMessage.getRaceType(), RaceType.fromValue(raceTypeElement.getTextContent()));
    }


    @Test
    public void encodeModifiedTimeTest() throws Exception {
        final Element elementModified = (Element) root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_CREATION_TIME_DATE.toString()).item(0);
        final LocalTime modifiedTime = LocalTime.parse(elementModified.getTextContent(), XmlMessage.DATE_TIME_FORMATTER);

        boolean isAfterBefore = modifiedTime.equals(beforeEncoding) || modifiedTime.isAfter(beforeEncoding);
        boolean isBeforeAfter = modifiedTime.equals(afterEncoding) || modifiedTime.isBefore(afterEncoding);

        assertTrue("xml creation time has not been correctly encoded", isAfterBefore && isBeforeAfter);
    }


    @Test
    public void encodeParticipantsTest() {
        Map<Integer, AC35XMLRaceMessage.EntryDirection> encodedParticipants = new HashMap<>();

        Node ids = root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_PARTICIPANTS.toString()).item(0);
        Element participants = (Element) ids;
        NodeList participantsNodeList = participants.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_YACHT.toString());

        for (int i = 0; i < participantsNodeList.getLength(); i++) {
            Node participant = participantsNodeList.item(i);
            Element participantElement = (Element) participant;
            int sourceId = Integer.parseInt(participantElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SOURCE_ID.toString()));
            AC35XMLRaceMessage.EntryDirection entry = AC35XMLRaceMessage.EntryDirection.fromValue(participantElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_ENTRY.toString()));
            encodedParticipants.put(sourceId, entry);
        }

        assertEquals("Incorrect number of participants were encoded", raceMessage.getParticipants().size(), encodedParticipants.size());

        for (Integer expectedId : raceMessage.getParticipants().keySet()) {
            assertEquals("Incorrect entry direction encoded for participant " + expectedId, raceMessage.getParticipants().get(expectedId), encodedParticipants.get(expectedId));
        }
    }


    @Test
    public void encodeCourseTest() {
        Node course = root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COURSE.toString()).item(0);
        Element courseElement = (Element) course;
        NodeList compoundMarkNodes = courseElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COMPOUND_MARK.toString());

        for (int i = 0; i < compoundMarkNodes.getLength(); i++) {
            Node compoundMarkNode = compoundMarkNodes.item(i);
            Element compoundMark = (Element) compoundMarkNode;

            List<Mark> encodedMarks = new ArrayList<>();
            NodeList markNodes = compoundMark.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_MARK.toString());

            for (int j = 0; j < markNodes.getLength(); j++) {
                Node markNode = markNodes.item(j);
                Element mark = (Element) markNode;
                int markId = Integer.parseInt(mark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SOURCE_ID.toString()));
                double markLat = Double.parseDouble(mark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_TARGET_LATITUDE.toString()));
                double markLng = Double.parseDouble(mark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_TARGET_LONGITUDE.toString()));
                encodedMarks.add(new Mark(markId, new Coordinate(markLat, markLng)));
            }

            String compoundMarkName = compoundMark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_NAME.toString());
            int compoundMarkID = Integer.parseInt(compoundMark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_COMPOUND_MARK_ID.toString()));
            encodedCompoundMarks.put(compoundMarkID, new CompoundMark(compoundMarkName, encodedMarks, compoundMarkID));
        }

        CompoundMark firstCompoundMark = raceMessage.getCompoundMarks().get(0);
        CompoundMark firstEncodedCompoundMark = encodedCompoundMarks.get(102);
        assertEquals(firstCompoundMark.getMarks().get(0).getCoordinate(), firstEncodedCompoundMark.getMarks().get(0).getCoordinate());
    }


    @Test
    public void encodeCompoundMarkSequenceTest() {
        List<MarkRounding> encodedMarkRoundings = new ArrayList<>();

        Node markRounding = root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COMPOUND_MARK_SEQUENCE.toString()).item(0);
        Element markRoundingElement = (Element) markRounding;
        NodeList markRoundings = markRoundingElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_CORNER.toString());

        for (int i = 0; i < markRoundings.getLength(); i++) {
            Node markRoundingNode = markRoundings.item(i);
            Element markRoundingE = (Element) markRoundingNode;
            int seqID = Integer.parseInt(markRoundingE.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SEQUENCE_ID.toString()));
            int compoundMarkNum = Integer.parseInt(markRoundingE.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_COMPOUND_MARK_ID.toString()));
            encodedMarkRoundings.add(new MarkRounding(seqID, encodedCompoundMarks.get(compoundMarkNum), MarkRounding.Direction.SP, 3));
        }

        MarkRounding firstMarkRounding = raceMessage.getMarkRoundings().get(0);
        MarkRounding firstEncodedMarkRounding = encodedMarkRoundings.get(0);
        assertEquals(firstMarkRounding.getSequenceNumber(), firstEncodedMarkRounding.getSequenceNumber());
    }


    @Test
    public void encodeCourseLimitsTest() {
        List<Coordinate> encodedBoundaryMarks = new ArrayList<>();

        Node boundaryNode = root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COURSE_BOUNDARIES.toString()).item(0);
        Element boundaryElement = (Element) boundaryNode;
        NodeList boundaryMarks = boundaryElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_LIMIT.toString());

        for (int i = 0; i < boundaryMarks.getLength(); i++) {
            Node boundaryMarkNode = boundaryMarks.item(i);
            Element boundaryMarkElement = (Element) boundaryMarkNode;
            int seqId = Integer.parseInt(boundaryMarkElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SEQUENCE_ID.toString()));
            double lat = Double.parseDouble(boundaryMarkElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_LATITUDE.toString()));
            double lon = Double.parseDouble(boundaryMarkElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_LONGITUDE.toString()));
            encodedBoundaryMarks.add(new Coordinate(lat, lon));
        }

        Coordinate firstBoundaryMark = raceMessage.getBoundaryMarks().get(0);
        Coordinate firstEncodedBoundaryMark = encodedBoundaryMarks.get(0);
        assertEquals(firstBoundaryMark, firstEncodedBoundaryMark);
    }
}