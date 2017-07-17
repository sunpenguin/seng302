package seng302.team18.test_mock;

import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.model.Race;
import seng302.team18.test_mock.connection.Server;
import seng302.team18.test_mock.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    private static int START_WAIT_TIME;
    private static int WARNING_WAIT_TIME;
    private static int PREP_WAIT_TIME;
    private static int MAX_PLAYERS;

    /**
     * Load a properties file and read the desired configuration settings
     */
    private static void config() throws IOException {
        Properties prop = new Properties();

        InputStream input = MockDataStream.class.getResourceAsStream("/config.txt");
        prop.load(input);

        START_WAIT_TIME = Integer.parseInt(prop.getProperty("START_WAIT_TIME"));
        WARNING_WAIT_TIME = Integer.parseInt(prop.getProperty("WARNING_WAIT_TIME"));
        PREP_WAIT_TIME = Integer.parseInt(prop.getProperty("PREP_WAIT_TIME"));
        MAX_PLAYERS = Integer.parseInt(prop.getProperty("MAX_PLAYERS"));
    }


    /**
     * Set up the mock by reading the XML files and creating Race, Course objects.
     * Run the test mock.
     * Set up the PlayerControllerReader in its own thread for listening to player actions.
     */
    private static void runMock() {
        try {
            config();

            final int SERVER_PORT = 5005;
            final long TIME_OUT = WARNING_WAIT_TIME - 5; // Number of seconds we will allow for more connections to be made to the server

            Race race = RACE_BUILDER.buildRace(REGATTA_BUILDER.buildRegatta(), COURSE_BUILDER.buildCourse());
            Server server = new Server(SERVER_PORT);
            ConnectionListener listener = new ConnectionListener(race, PARTICIPANTS_BUILDER.getIdPool(), new AC35MessageParserFactory());
            TestMock testMock = new TestMock(server, XML_MESSAGE_BUILDER, race, PARTICIPANTS_BUILDER.getParticipantPool());

            server.addObserver(listener);
            server.addObserver(testMock);
            server.openServer();
            listener.setTimeout(System.currentTimeMillis() + TIME_OUT);
            testMock.runSimulation(START_WAIT_TIME, WARNING_WAIT_TIME, PREP_WAIT_TIME);
            server.close();
        } catch (IOException e) {
            System.out.println("Error occurred reading configuration file");
        }
    }


    public static void main(String[] args) {
        runMock();
    }
}
