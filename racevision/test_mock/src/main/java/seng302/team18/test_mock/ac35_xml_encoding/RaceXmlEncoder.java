package seng302.team18.test_mock.ac35_xml_encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.AC35RaceXMLComponents;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.model.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by afj19 on 29/06/17.
 */
public class RaceXmlEncoder extends XmlEncoder<AC35XMLRaceMessage> {

    public DOMSource getDomSource(AC35XMLRaceMessage raceMessage) throws ParserConfigurationException {
        final String DEFAULT_RACE_TYPE = "Match";
        final String DEFAULT_START_POSTPONE_STATE = "false";

        //Root
        Document doc = createDocument();
        Element root = doc.createElement(AC35RaceXMLComponents.ROOT_RACE.toString());
        doc.appendChild(root);

        // Race ID
        Element raceId = doc.createElement(AC35RaceXMLComponents.ELEMENT_RACE_ID.toString());
        raceId.appendChild(doc.createTextNode(((Integer) raceMessage.getRaceID()).toString()));
        root.appendChild(raceId);

        // Race Type
        Element raceType = doc.createElement(AC35RaceXMLComponents.ELEMENT_RACE_TYPE.toString());
        raceType.appendChild(doc.createTextNode(DEFAULT_RACE_TYPE));
        root.appendChild(raceType);

        // Creation time
        Element creationTime = doc.createElement(AC35RaceXMLComponents.ELEMENT_CREATION_TIME_DATE.toString());
        final String time = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        creationTime.appendChild(doc.createTextNode(time));
        root.appendChild(creationTime);

        // Start time
        Element startTime = doc.createElement(AC35RaceXMLComponents.ELEMENT_START_DATE_TIME.toString());
        startTime.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_TIME.toString(), raceMessage.getStartTime());
        startTime.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_POSTPONE.toString(), DEFAULT_START_POSTPONE_STATE);
        root.appendChild(startTime);

        // Participants
        root.appendChild(encodeParticipants(doc, raceMessage.getParticipantIDs()));

        // Course
        root.appendChild(encodeCourse(doc, raceMessage.getCompoundMarks()));

        // Compound mark sequence / mark roundings
        root.appendChild(encodeCompoundMarkSequence(doc, raceMessage.getMarkRoundings()));

        // Course Limits
        root.appendChild(encodeCourseLimits(doc, raceMessage.getBoundaryMarks()));

        return new DOMSource(doc);
    }

    private Element encodeParticipants(Document doc, List<Integer> participantIds) {
        Element participants = doc.createElement(AC35RaceXMLComponents.ELEMENT_PARTICIPANTS.toString());

        for (Integer participantId : participantIds) {
            participants.appendChild(encodeParticipant(doc, participantId));
        }

        return participants;
    }

    private Element encodeParticipant(Document doc, Integer id) {
        final String DEFAULT_ATTRIBUTE_ENTRY = "Port";

        Element participant = doc.createElement(AC35RaceXMLComponents.ELEMENT_YACHT.toString());

        participant.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_SOURCE_ID.toString(), id.toString());
        participant.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_ENTRY.toString(), DEFAULT_ATTRIBUTE_ENTRY);

        return participant;
    }

    private Element encodeCourse(Document doc, List<CompoundMark> compoundMarks) {
        Element course = doc.createElement(AC35RaceXMLComponents.ELEMENT_COURSE.toString());

        for (CompoundMark compoundMark : compoundMarks) {
            course.appendChild(encodeCompoundMark(doc, compoundMark));
        }

        return course;
    }

    private Element encodeCompoundMark(Document doc, CompoundMark compoundMark) {
        Element elementCompound = doc.createElement(AC35RaceXMLComponents.ELEMENT_COMPOUND_MARK.toString());
        elementCompound.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_COMPOUND_MARK_ID.toString(), compoundMark.getId().toString());
        elementCompound.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_NAME.toString(), compoundMark.getName());

        encodeMarks(doc, elementCompound, compoundMark.getMarks());

        return elementCompound;
    }

    private void encodeMarks(Document doc, Element compoundMark, List<Mark> marks) {
        boolean isGate = marks.size() > 1;

        for (int i = 0; i < marks.size(); i++) {
            Mark mark = marks.get(i);

            Element elementMark = doc.createElement(AC35RaceXMLComponents.ELEMENT_MARK.toString());
            if (isGate) {
                elementMark.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_SEQUENCE_ID.toString(), ((Integer) (i + 1)).toString());
            }

            elementMark.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_NAME.toString(), mark.getName());

            Coordinate coordinate = mark.getCoordinate();
            elementMark.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_LATITUDE.toString(), coordinate.getLatitude().toString());
            elementMark.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_LONGITUDE.toString(), coordinate.getLongitude().toString());

            elementMark.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_SOURCE_ID.toString(), mark.getId().toString());

            compoundMark.appendChild(elementMark);
        }
    }

    private Element encodeCompoundMarkSequence(Document doc, List<MarkRounding> markRoundings) {
        final String DEFAULT_ROUNDING = "SP";
        final Integer DEFAULT_ZONE_SIZE = 3;

        Element sequence = doc.createElement(AC35RaceXMLComponents.ELEMENT_COMPOUND_MARK_SEQUENCE.toString());

        for (MarkRounding rounding : markRoundings) {
            Element corner = doc.createElement(AC35RaceXMLComponents.ELEMENT_CORNER.toString());
            corner.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_SEQUENCE_ID.toString(), rounding.getSequenceNumber().toString());
            corner.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_COMPOUND_MARK_ID.toString(), rounding.getCompoundMark().getId().toString());
            corner.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_ROUNDING.toString(), DEFAULT_ROUNDING);
            corner.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_ZONE_SIZE.toString(), DEFAULT_ZONE_SIZE.toString());
            sequence.appendChild(corner);
        }

        return sequence;
    }

    private Element encodeCourseLimits(Document doc, List<BoundaryMark> boundaryMarks) {
        Element courseLimits = doc.createElement(AC35RaceXMLComponents.ELEMENT_COURSE_BOUNDARIES.toString());

        for (BoundaryMark boundaryMark : boundaryMarks) {
            Element limit = doc.createElement(AC35RaceXMLComponents.ELEMENT_LIMIT.toString());
            limit.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_SEQUENCE_ID.toString(), boundaryMark.getSequenceID().toString());
            limit.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_LATITUDE.toString(), boundaryMark.getCoordinate().getLatitude().toString());
            limit.setAttribute(AC35RaceXMLComponents.ATTRIBUTE_LONGITUDE.toString(), boundaryMark.getCoordinate().getLongitude().toString());
            courseLimits.appendChild(limit);
        }

        return courseLimits;
    }
}
