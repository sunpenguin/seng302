package seng302.team18.test_mock;

import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.model.Race;
import seng302.team18.test_mock.connection.Server;
import seng302.team18.test_mock.model.*;

/**
 * Class to set up the mock stream
 */
public class MockDataStream {

    // Change concrete builders here to change the preset of race/regatta/course
    private static final BaseRaceBuilder RACE_BUILDER = new RaceBuilder1();
    private static final BaseCourseBuilder COURSE_BUILDER = new CourseBuilder1();
    private static final BaseRegattaBuilder REGATTA_BUILDER = new RegattaBuilder1();
    private static final BaseParticipantsBuilder PARTICIPANTS_BUILDER = new ParticipantsBuilder1();

    // Change the XmlDefault implementations to change the default values for the XML messages
    private static final XmlMessageBuilder XML_MESSAGE_BUILDER = new XmlMessageBuilder(new BoatXmlDefaults(), new RaceXmlDefaults());


    /**
     * Set up the mock by reading the XML files and creating Race, Course objects.
     * Run the test mock.
     * Set up the PlayerControllerReader in its own thread for listening to player actions.
     */
    private static void runMock() {
        final int SERVER_PORT = 5005;
        final long TIME_OUT = 5 * 3 * 1000;

        Race race = RACE_BUILDER.buildRace(REGATTA_BUILDER.buildRegatta(), COURSE_BUILDER.buildCourse());

        Server server = new Server(SERVER_PORT);
        ConnectionListener listener = new ConnectionListener(race, PARTICIPANTS_BUILDER.getIdPool(), new AC35MessageParserFactory());
        TestMock testMock = new TestMock(server, XML_MESSAGE_BUILDER, race, PARTICIPANTS_BUILDER.getParticipantPool());

        server.addObserver(listener);
        server.addObserver(testMock);
        server.openServer();
        listener.setTimeout(System.currentTimeMillis() + TIME_OUT);
        testMock.runSimulation();
        server.closeServer();
    }


    public static void main(String[] args) {
        runMock();
    }
}
