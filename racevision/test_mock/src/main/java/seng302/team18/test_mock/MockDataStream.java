package seng302.team18.test_mock;

import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.test_mock.connection.Server;

import java.util.ArrayList;

/**
 * Class to set up the mock stream
 */
public class MockDataStream {

    /**
     * Set up the mock by reading the XML files and creating Race, Course objects.
     * Run the test mock.
     * Set up the PlayerControllerReader in its own thread for listening to player actions.
     */
    private static void runMock() {
        final int SERVER_PORT = 5005;
        final String regattaXML = TestXMLFiles.REGATTA_XML_1.toString();
        final String boatsXML = TestXMLFiles.BOATS_XML_2.toString();
        final String raceXML = TestXMLFiles.RACE_XML_2.toString();
        final long ABOUT_THREE_MINUTES = 59 * 3 * 1000;

        RaceCourseGenerator generator = new RaceCourseGenerator();
        generator.readFiles(regattaXML, boatsXML, raceXML);
        Course course = generator.generateCourse();
        Race race = generator.generateRace(course);

        Server server = new Server(SERVER_PORT);
        server.openServer();

        System.out.println(1337);

        ConnectionListener connectionListener = new ConnectionListener(race, new ArrayList<>(), System.currentTimeMillis() + ABOUT_THREE_MINUTES, new AC35MessageParserFactory());
        server.addObserver(connectionListener);

        TestMock testMock = new TestMock(race, server);
        testMock.runSimulation();
        server.closeServer();
    }


    public static void main (String[] args) {
        runMock();
    }
}
