package seng302.team18.test_mock;

import seng302.team18.messageparsing.SocketMessageReceiver;
import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.test_mock.connection.PlayerControllerReader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        final String regattaXML = TestXMLFiles.REGATTA_XML_1.toString();
        final String boatsXML = TestXMLFiles.BOATS_XML_2.toString();
        final String raceXML = TestXMLFiles.RACE_XML_2.toString();

        RaceCourseGenerator generator = new RaceCourseGenerator();
        generator.readFiles(regattaXML, boatsXML, raceXML);
        Course course = generator.generateCourse();
        Race race = generator.generateRace(course);

        TestMock testMock = new TestMock(race);
        testMock.run();

//      No players connected yet, but this is the general idea for each player
//        PlayerControllerReader controllerReader = new PlayerControllerReader(new SocketMessageReceiver());
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.submit(controllerReader);
//        executor.shutdown();
    }


    public static void main (String[] args) {
        runMock();
    }
}
