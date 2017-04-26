package seng302.team18.test_mock.XMLparsers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.team18.data.AC35XMLRaceMessage;
import seng302.team18.data.AC35XMLRaceParser;
import seng302.team18.model.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jth102 on 19/04/17.
 */
public class AC35RaceParser {
    private AC35RaceContainer raceContainer = new AC35RaceContainer();

    public AC35RaceContainer parse(InputStream stream) {
        final String RACE_ELEMENT = "Race";
        final String START_DATE_TIME_ELEMENT = "RaceStartTime";
        final String PARTICIPANTS_ELEMENT = "Participants";
        final String COURSE_ELEMENT = "Course";
        final String COMPOUND_MARK_SEQUENCE = "CompoundMarkSequence";
        final String COURSE_BOUNDARIES_ELEMENT = "CourseLimit";
        final String RACE_ID_ELEMENT = "RaceID";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            AC35XMLRaceParser parser = new AC35XMLRaceParser();

            builder = factory.newDocumentBuilder(); // parser configuration exception
            doc = builder.parse(stream); // io exception

            doc.getDocumentElement().normalize();
            Element raceElement = (Element) doc.getElementsByTagName(RACE_ELEMENT).item(0);


            Node raceIDNode = raceElement.getElementsByTagName(RACE_ID_ELEMENT).item(0); // start time
            int raceID = parser.parseRaceID(raceIDNode);


            Node startTimeNode = raceElement.getElementsByTagName(START_DATE_TIME_ELEMENT).item(0); // start time
            String startTimeString = parser.parseRaceTime(startTimeNode);

            Node participantsNode = raceElement.getElementsByTagName(PARTICIPANTS_ELEMENT).item(0); // participants
            List<Integer> participantIDs = parser.parseParticipantIDs(participantsNode);

            Node courseNode = raceElement.getElementsByTagName(COURSE_ELEMENT).item(0); // compound marks
            Map<Integer, CompoundMark> compoundMarks = parser.parseCompoundMarks(courseNode);

            Node markSequenceNode = raceElement.getElementsByTagName(COMPOUND_MARK_SEQUENCE).item(0); // mark roundings
            List<MarkRounding> markRoundings = parser.parseMarkRoundings(markSequenceNode, compoundMarks);

            Node boundariesNode = raceElement.getElementsByTagName(COURSE_BOUNDARIES_ELEMENT).item(0); // boundaries
            List<BoundaryMark> boundaries = parser.parseBoundaries(boundariesNode);

            raceContainer.setBoundaryMark(boundaries);
            raceContainer.setCompoundMarks(compoundMarks);
            raceContainer.setMarkRoundings(markRoundings);
            raceContainer.setParticipantIDs(participantIDs);
            raceContainer.setStartTime(startTimeString);
            raceContainer.setRaceID(raceID);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return raceContainer;
    }
}
