package seng302.team18.racemodel;

import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.model.*;
import seng302.team18.racemodel.ac35_xml_encoding.BoatXmlDefaults;
import seng302.team18.racemodel.ac35_xml_encoding.RaceXmlDefaults;
import seng302.team18.racemodel.ac35_xml_encoding.XmlMessageBuilder;
import seng302.team18.racemodel.connection.ConnectionListener;
import seng302.team18.racemodel.connection.Server;
import seng302.team18.racemodel.model.*;

/**
 * Class to set up the mock stream
 */
public class MockDataStream {

    // Change concrete builders here to change the preset of race/regatta/course
    private static final AbstractRaceBuilder RACE_BUILDER = new ChallengeRaceBuilder();
    private static final AbstractCourseBuilder COURSE_BUILDER = new CourseBuilderChallenge();
    private static final AbstractRegattaBuilder REGATTA_BUILDER = new RegattaBuilderRealistic();
    private static final AbstractParticipantsBuilder PARTICIPANTS_BUILDER = new ParticipantsBuilderSize20();

    // Change the XmlDefault implementations to change the default values for the XML messages
    private static final XmlMessageBuilder XML_MESSAGE_BUILDER = new XmlMessageBuilder(new BoatXmlDefaults(), new RaceXmlDefaults());


    /**
     * Main setup method for the application.
     */
    private static void runMock() {
        final int SERVER_PORT = 5005;

        Race race = RACE_BUILDER.buildRace(new Race(), REGATTA_BUILDER.buildRegatta(), COURSE_BUILDER.buildCourse());
        Server server = new Server(SERVER_PORT);
        ConnectionListener listener = new ConnectionListener(race, PARTICIPANTS_BUILDER.getIdPool(), new AC35MessageParserFactory());
        TestMock testMock = new TestMock(server, XML_MESSAGE_BUILDER, race, PARTICIPANTS_BUILDER.getParticipantPool(), COURSE_BUILDER);
//        testMock.setSendRaceXML(true);

        server.setCloseOnEmpty(true);
        server.addObserver(listener);
        server.addObserver(testMock);
        listener.addObserver(testMock);
        server.openServer();
    }


    public static void main(String[] args) {
        runMock();
    }
}
