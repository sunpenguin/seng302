package seng302.team18.messageparsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.model.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        final String RACE_ELEMENT = "Race";
        final String RACE_ID = "RaceID";
        final String START_DATE_TIME_ELEMENT = "RaceStartTime";
        final String PARTICIPANTS_ELEMENT = "Participants";
        final String COURSE_ELEMENT = "Course";
        final String COMPOUND_MARK_SEQUENCE = "CompoundMarkSequence";
        final String COURSE_BOUNDARIES_ELEMENT = "CourseLimit";

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
        Element raceElement = (Element) doc.getElementsByTagName(RACE_ELEMENT).item(0);

        Node idNode = raceElement.getElementsByTagName(RACE_ID).item(0); // race id
        int id = parseRaceId(idNode);

        Node startTimeNode = raceElement.getElementsByTagName(START_DATE_TIME_ELEMENT).item(0); // start time
        String startTimeString = parseRaceTime(startTimeNode);

        Node participantsNode = raceElement.getElementsByTagName(PARTICIPANTS_ELEMENT).item(0); // participants
        List<Integer> participantIDs = parseParticipantIDs(participantsNode);

        Node courseNode = raceElement.getElementsByTagName(COURSE_ELEMENT).item(0); // compound marks
        Map<Integer, CompoundMark> compoundMarks = parseCompoundMarks(courseNode);

        Node markSequenceNode = raceElement.getElementsByTagName(COMPOUND_MARK_SEQUENCE).item(0); // mark roundings
        List<MarkRounding> markRoundings = parseMarkRoundings(markSequenceNode, compoundMarks);

        Node boundariesNode = raceElement.getElementsByTagName(COURSE_BOUNDARIES_ELEMENT).item(0); // boundaries
        List<BoundaryMark> boundaries = parseBoundaries(boundariesNode);

        AC35XMLRaceMessage message = new AC35XMLRaceMessage();
        message.setRaceID(id);
        message.setStartTime(startTimeString);
        message.setBoundaryMarks(boundaries);
        message.setCompoundMarks(new ArrayList<>(compoundMarks.values()));
        message.setParticipantIDs(participantIDs);
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
        final String START = "Start"; // if program breaks change this to "Time"
        final String TIME = "Time";
        if (startTimeNode.getNodeType() == Node.ELEMENT_NODE) {
            String time = ((Element) startTimeNode).getAttribute(TIME);
            if ("".equals(time)) {
                return ((Element) startTimeNode).getAttribute(START);
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
    private List<Integer> parseParticipantIDs(Node participantsNode) {
        final String PARTICIPANT_ELEMENT = "Yacht";
        final String PARTICIPANT_ID = "SourceID";
        List<Integer> participantIDs = new ArrayList<>();
        if (participantsNode.getNodeType() == Node.ELEMENT_NODE) {
            Element participantsElement = (Element) participantsNode;
            NodeList participantNodeList = participantsElement.getElementsByTagName(PARTICIPANT_ELEMENT);
            for (int i = 0; i < participantNodeList.getLength(); i++) {
                Node participantNode = participantNodeList.item(i);
                if (participantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element participantElement = (Element) participantNode;
                    participantIDs.add(Integer.parseInt(participantElement.getAttribute(PARTICIPANT_ID)));
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
        final String COMPOUND_MARK_ELEMENT = "CompoundMark";
        final String COMPOUND_MARK_ID = "CompoundMarkID";
        final String COMPOUND_MARK_NAME = "Name";
        final String MARK_ELEMENT = "Mark";
        final String MARK_ID = "SourceID";
        final String MARK_LAT = "TargetLat";
        final String MARK_LONG = "TargetLng";
        Map<Integer, CompoundMark> compoundMarks = new HashMap<>();
        if (courseNode.getNodeType() == Node.ELEMENT_NODE) {
            Element courseElement = (Element) courseNode;
            NodeList compoundMarkNodeList = courseElement.getElementsByTagName(COMPOUND_MARK_ELEMENT);
            for (int i = 0; i < compoundMarkNodeList.getLength(); i++) {
                Node compoundMarkNode = compoundMarkNodeList.item(i);
                if (compoundMarkNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element compoundMarkElement = (Element) compoundMarkNode;
                    List<Mark> marks = new ArrayList<>();
                    NodeList markNodeList = compoundMarkElement.getElementsByTagName(MARK_ELEMENT);
                    for (int j = 0; j < markNodeList.getLength(); j++) {
                        Node markNode = markNodeList.item(j);
                        if (markNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element markElement = (Element) markNode;
                            int id = Integer.parseInt(markElement.getAttribute(MARK_ID));
                            double lat = Double.parseDouble(markElement.getAttribute(MARK_LAT));
                            double lng = Double.parseDouble(markElement.getAttribute(MARK_LONG));
                            marks.add(new Mark(id, new Coordinate(lat, lng)));
                        }
                    }
                    String compoundMarkName = compoundMarkElement.getAttribute(COMPOUND_MARK_NAME);
                    int compoundMarkID = Integer.parseInt(compoundMarkElement.getAttribute(COMPOUND_MARK_ID));
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
        final String CORNER = "Corner";
        final String MARK_SEQUENCE_ID = "SeqID";
        final String COMPOUND_MARK_ID = "CompoundMarkID";
        final String ROUNDING = "Rounding";
        List<MarkRounding> markRoundings = new ArrayList<>();
        if (markSequenceNode.getNodeType() == Node.ELEMENT_NODE) {
            Element markSequenceElement = (Element) markSequenceNode;
            NodeList markSequenceNodeList = markSequenceElement.getElementsByTagName(CORNER);
            for (int i = 0; i < markSequenceNodeList.getLength(); i++) {
                Node markNode = markSequenceNodeList.item(i);
                if (markNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element markElement = (Element) markNode;
                    int seqNum = Integer.parseInt(markElement.getAttribute(MARK_SEQUENCE_ID));
                    int compoundMarkNum = Integer.parseInt(markElement.getAttribute(COMPOUND_MARK_ID));
                    String rounding = markElement.getAttribute(ROUNDING);
                    markRoundings.add(new MarkRounding(seqNum, compoundMarks.get(compoundMarkNum)));
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
    private List<BoundaryMark> parseBoundaries(Node boundariesNode) {
        final String COURSE_BOUNDARY_ELEMENT = "Limit";
        final String BOUNDARY_SEQUENCE_ID = "SeqID";
        final String BOUNDARY_LAT = "Lat";
        final String BOUNDARY_LONG = "Lon";
        List<BoundaryMark> boundaries = new ArrayList<>();

        if (boundariesNode.getNodeType() == Node.ELEMENT_NODE) {
            Element boundariesElement = (Element) boundariesNode;
            NodeList boundaryList = boundariesElement.getElementsByTagName(COURSE_BOUNDARY_ELEMENT);
            for (int i = 0; i < boundaryList.getLength(); i++) {
                Node boundaryNode = boundaryList.item(i);
                if (boundariesNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element boundaryElement = (Element) boundaryNode;
                    int seqId = Integer.parseInt(boundaryElement.getAttribute(BOUNDARY_SEQUENCE_ID));
                    double lat = Double.parseDouble(boundaryElement.getAttribute(BOUNDARY_LAT));
                    double lon = Double.parseDouble(boundaryElement.getAttribute(BOUNDARY_LONG));
                    boundaries.add(new BoundaryMark(seqId, new Coordinate(lat, lon)));
                }
            }
        }
        return boundaries;
    }
}
