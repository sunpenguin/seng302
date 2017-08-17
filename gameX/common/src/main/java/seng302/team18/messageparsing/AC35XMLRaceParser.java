package seng302.team18.messageparsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.team18.message.AC35RaceXMLComponents;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.model.MarkRounding;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A parser which reads information from an XML stream and creates messages representing information about the race.
 */
public class AC35XMLRaceParser implements MessageBodyParser {


    /**
     * Select the relevant race elements from the input XML and creating a race message object to store this information.
     *
     * @param stream The input stream (XML).
     * @return A race message holding the information read from the stream.
     */
    @Override
    public AC35XMLRaceMessage parse(InputStream stream) {


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder(); // parser configuration exception
            doc = builder.parse(stream); // io exception
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
        doc.getDocumentElement().normalize();
        Element raceElement = (Element) doc.getElementsByTagName(AC35RaceXMLComponents.ROOT_RACE.toString()).item(0);

        Node idNode = raceElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_RACE_ID.toString()).item(0); // race id
        int id = parseRaceId(idNode);

        Node startTimeNode = raceElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_START_DATE_TIME.toString()).item(0); // start time
        String startTimeString = parseRaceTime(startTimeNode);

        Node participantsNode = raceElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_PARTICIPANTS.toString()).item(0); // participants
        Map<Integer, AC35XMLRaceMessage.EntryDirection> participants = parseParticipants(participantsNode);

        Node courseNode = raceElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COURSE.toString()).item(0); // compound marks
        Map<Integer, CompoundMark> compoundMarks = parseCompoundMarks(courseNode);

        Node markSequenceNode = raceElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COMPOUND_MARK_SEQUENCE.toString()).item(0); // mark roundings
        List<MarkRounding> markRoundings = parseMarkRoundings(markSequenceNode, compoundMarks);

        Node boundariesNode = raceElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COURSE_BOUNDARIES.toString()).item(0); // boundaries
        List<Coordinate> boundaries = parseBoundaries(boundariesNode);

        AC35XMLRaceMessage message = new AC35XMLRaceMessage();
        message.setRaceID(id);
        message.setStartTime(startTimeString);
        message.setBoundaryMarks(boundaries);
        message.setCompoundMarks(new ArrayList<>(compoundMarks.values()));
        message.setParticipants(participants);
        message.setMarkRoundings(markRoundings);
        return message;
    }

    /**
     * Converts the byte array an input stream of standard characters to be passed to the other parser so that a race
     * message can be generated and returned.
     *
     * @param bytes The input byte stream.
     * @return A race message holding the information read from the stream.
     */
    @Override
    public AC35XMLRaceMessage parse(byte[] bytes) {
        InputStream stream = new ByteArrayInputStream(new String(bytes, StandardCharsets.UTF_8).trim().getBytes());
        return parse(stream);
    }

    private int parseRaceId(Node raceIDNode) {
        return Integer.parseInt(raceIDNode.getTextContent());
    }

    /**
     * Reads the race time from the data stream.
     *
     * @param startTimeNode The element from the XML with information about the race time
     * @return A string representation of the time of the race.
     */
    private String parseRaceTime(Node startTimeNode) {

        if (startTimeNode.getNodeType() == Node.ELEMENT_NODE) {
            String time = ((Element) startTimeNode).getAttribute(AC35RaceXMLComponents.ATTRIBUTE_TIME.toString());
            if ("".equals(time)) {
                return ((Element) startTimeNode).getAttribute(AC35RaceXMLComponents.ATTRIBUTE_START.toString());
            } else {
                return time;
            }
        }
        return "";
    }

    /**
     * Reads the participants ID's from the XML.
     *
     * @param participantsNode The element from the XML with information about the participants.
     * @return A list of integers (boat IDs)
     */
    private Map<Integer, AC35XMLRaceMessage.EntryDirection> parseParticipants(Node participantsNode) {

        Map<Integer, AC35XMLRaceMessage.EntryDirection> participantIDs = new HashMap<>();

        if (participantsNode.getNodeType() == Node.ELEMENT_NODE) {
            Element participantsElement = (Element) participantsNode;
            NodeList participantNodeList = participantsElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_YACHT.toString());

            for (int i = 0; i < participantNodeList.getLength(); i++) {
                Node participantNode = participantNodeList.item(i);

                if (participantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element participantElement = (Element) participantNode;

                    int id = Integer.parseInt(participantElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SOURCE_ID.toString()));
                    AC35XMLRaceMessage.EntryDirection direction = AC35XMLRaceMessage.EntryDirection.fromValue(participantElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_ENTRY.toString()));
                    participantIDs.put(id, direction);
                }
            }
        }
        return participantIDs;
    }

    /**
     * Reads the compound marks from the XML.
     *
     * @param courseNode The element from the XML with information about the marks.
     * @return A list mapping the integer ID and coordinate of the mark.
     */
    private Map<Integer, CompoundMark> parseCompoundMarks(Node courseNode) {

        Map<Integer, CompoundMark> compoundMarks = new HashMap<>();
        if (courseNode.getNodeType() == Node.ELEMENT_NODE) {
            Element courseElement = (Element) courseNode;
            NodeList compoundMarkNodeList = courseElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COMPOUND_MARK.toString());
            for (int i = 0; i < compoundMarkNodeList.getLength(); i++) {
                Node compoundMarkNode = compoundMarkNodeList.item(i);
                if (compoundMarkNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element compoundMarkElement = (Element) compoundMarkNode;
                    List<Mark> marks = new ArrayList<>();
                    NodeList markNodeList = compoundMarkElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_MARK.toString());
                    for (int j = 0; j < markNodeList.getLength(); j++) {
                        Node markNode = markNodeList.item(j);
                        if (markNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element markElement = (Element) markNode;
                            int id = Integer.parseInt(markElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SOURCE_ID.toString()));
                            double lat = Double.parseDouble(markElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_TARGET_LATITUDE.toString()));
                            double lng = Double.parseDouble(markElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_TARGET_LONGITUDE.toString()));
                            marks.add(new Mark(id, new Coordinate(lat, lng)));
                        }
                    }
                    String compoundMarkName = compoundMarkElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_NAME.toString());
                    int compoundMarkID = Integer.parseInt(compoundMarkElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_COMPOUND_MARK_ID.toString()));
                    compoundMarks.put(compoundMarkID, new CompoundMark(compoundMarkName, marks, compoundMarkID));
                }
            }
        }
        return compoundMarks;
    }

    /**
     * Reads the mark roundings from the XML.
     *
     * @param markSequenceNode The element from the XML with information about the mark sequence.
     * @param compoundMarks    A list mapping the integer ID and coordinate of the mark.
     * @return A list of mark roundings as read bu the parser.
     */
    private List<MarkRounding> parseMarkRoundings(Node markSequenceNode, Map<Integer, CompoundMark> compoundMarks) {

        List<MarkRounding> markRoundings = new ArrayList<>();

        if (markSequenceNode.getNodeType() == Node.ELEMENT_NODE) {
            Element markSequenceElement = (Element) markSequenceNode;
            NodeList markSequenceNodeList = markSequenceElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_CORNER.toString());

            for (int i = 0; i < markSequenceNodeList.getLength(); i++) {
                Node markNode = markSequenceNodeList.item(i);

                if (markNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element markElement = (Element) markNode;

                    int seqNum = Integer.parseInt(markElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SEQUENCE_ID.toString()));
                    int compoundMarkNum = Integer.parseInt(markElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_COMPOUND_MARK_ID.toString()));
                    MarkRounding.Direction direction = MarkRounding.Direction.fromValue(markElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_ROUNDING.toString()));
                    int zoneSize = Integer.parseInt(markElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_ZONE_SIZE.toString()));
                    markRoundings.add(new MarkRounding(seqNum, compoundMarks.get(compoundMarkNum), direction, zoneSize));
                }
            }
        }
        return markRoundings;
    }

    /**
     * Reads the boundary marks representing the outer edge of the course from the XML.
     *
     * @param boundariesNode The element from the XML with information about the mark sequence.
     * @return A list of boundary marks mapping out the outer edge of the course.
     */
    private List<Coordinate> parseBoundaries(Node boundariesNode) {

        class BoundaryMark {
            private final int sequenceID;
            private final Coordinate coordinate;

            public BoundaryMark(int sequenceID, Coordinate coordinate) {
                this.sequenceID = sequenceID;
                this.coordinate = coordinate;
            }

            private int getSequenceID() {
                return sequenceID;
            }

            private Coordinate getCoordinate() {
                return coordinate;
            }
        }

        List<BoundaryMark> boundaries = new ArrayList<>();

        if (boundariesNode.getNodeType() == Node.ELEMENT_NODE) {
            Element boundariesElement = (Element) boundariesNode;
            NodeList boundaryList = boundariesElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_LIMIT.toString());
            for (int i = 0; i < boundaryList.getLength(); i++) {
                Node boundaryNode = boundaryList.item(i);
                if (boundariesNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element boundaryElement = (Element) boundaryNode;
                    int seqId = Integer.parseInt(boundaryElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SEQUENCE_ID.toString()));
                    double lat = Double.parseDouble(boundaryElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_LATITUDE.toString()));
                    double lon = Double.parseDouble(boundaryElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_LONGITUDE.toString()));
                    boundaries.add(new BoundaryMark(seqId, new Coordinate(lat, lon)));
                }
            }
        }
        return boundaries.stream()
                .sorted(Comparator.comparingInt(BoundaryMark::getSequenceID))
                .map(BoundaryMark::getCoordinate)
                .collect(Collectors.toList());
    }
}
