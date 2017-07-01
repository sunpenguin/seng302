package seng302.team18.test_mock.ac35_xml_encoding;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.model.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by afj19 on 29/06/17.
 */
public class RaceXmlEncoderTest {

    private RaceXmlEncoder raceXmlEncoder;
    private String raceXML;
    private DOMSource domSource;
    private AC35XMLRaceMessage raceMessage;

    private void setUpRaceMessage() {
         String startTime;
         List<Integer> participantIDs;
         List<CompoundMark> compoundMarks;
         List<MarkRounding> markRoundings;
         List<BoundaryMark> boundaryMarks;
         int raceID;

        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss-Z");
        startTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        participantIDs = new ArrayList<>();

        for (Integer i = 107; i < 109; i ++) {
            participantIDs.add(i);
        }

        Mark mark1 = new Mark(1, new Coordinate(22,21));
        Mark mark2 = new Mark(2, new Coordinate(32,31));
        Mark mark3 = new Mark(3, new Coordinate(12,11));
        Mark mark4 = new Mark(4, new Coordinate(52,51));
        Mark mark5 = new Mark(5, new Coordinate(42,41));
        Mark mark6 = new Mark(6, new Coordinate(52,51));

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

        MarkRounding markRounding1 = new MarkRounding(1, compoundMark1);
        MarkRounding markRounding2 = new MarkRounding(2, compoundMark2);
        MarkRounding markRounding3 = new MarkRounding(3, compoundMark3);
        MarkRounding markRounding4 = new MarkRounding(4, compoundMark4);
        markRoundings = new ArrayList<>();
        markRoundings.add(markRounding1);
        markRoundings.add(markRounding2);
        markRoundings.add(markRounding3);
        markRoundings.add(markRounding4);

        BoundaryMark b1 = new BoundaryMark(1, new Coordinate(33,34));
        BoundaryMark b2 = new BoundaryMark(2, new Coordinate(13,14));
        BoundaryMark b3 = new BoundaryMark(3, new Coordinate(53,54));
        BoundaryMark b4 = new BoundaryMark(4, new Coordinate(43,44));

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
        raceMessage.setParticipantIDs(participantIDs);
        raceMessage.setStartTime(startTime);
    }

    @Before
    public void setUp() throws Exception {
        setUpRaceMessage();
        domSource = raceXmlEncoder.getDomSource(raceMessage);
    }

    @Test
    public void RaceXmlEncoderTest() throws TransformerException, ParserConfigurationException{
        raceXML = raceXmlEncoder.encode(raceMessage);
    }

    @Test
    public void encodeParticipantTest() {

    }

    @Test
    public void encodeCourseTest() {}

    @Test
    public void encodeCompoundMarkTest() {}

    @Test
    public void encodeMarksTest() {}

    @Test
    public void encodeCompoundMarkSequenceTest() {}

    @Test
    public  void encodeCourseLimitsTest() {}
}
