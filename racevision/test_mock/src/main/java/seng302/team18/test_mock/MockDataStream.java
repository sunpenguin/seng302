package seng302.team18.test_mock;

import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.test_mock.connection.Server;

import java.util.ArrayList;
import java.util.Arrays;

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
        final long TIME_OUT = 5 * 3 * 1000;

//        RaceCourseGenerator generator = new RaceCourseGenerator();
//        generator.generateXmlMessages();
//        Course course = generator.generateCourse();
//        Race race = new Race(new ArrayList<>(), course, 1337);
//        Race race = generator.generateRace(course);

        Server server = new Server(SERVER_PORT);
        ConnectionListener listener = new ConnectionListener(race, Arrays.asList(121, 122, 123, 124, 125, 126), new AC35MessageParserFactory());
        TestMock testMock = new TestMock(race, server);

        server.addObserver(listener);
        server.addObserver(testMock);
        server.openServer();
        listener.setTimeout(System.currentTimeMillis() + TIME_OUT);
        testMock.runSimulation();
        server.closeServer();
    }


    public static void main (String[] args) {
        runMock();
    }
}
