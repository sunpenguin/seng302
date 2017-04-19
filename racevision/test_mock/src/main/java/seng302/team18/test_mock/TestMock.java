package seng302.team18.test_mock;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;
import seng302.team18.test_mock.connection.BoatMessageGenerator;
import seng302.team18.test_mock.connection.ScheduledMessage;
import seng302.team18.test_mock.connection.Server;
import seng302.team18.test_mock.mock_generators.CourseGenerator;
import seng302.team18.test_mock.mock_generators.RaceGenerator;
import seng302.team18.test_mock.mock_generators.RegattaGenerator;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * A mock AC35 created from supplied XML files.
 */
public class TestMock {

    private Regatta regatta;
    private Course course;
    private Race race;

    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessage> messages = new ArrayList<>();

    //TODO give real port
    private Server server = new Server(1478);


    /**
     * Generate the classes necessary to visualise a mock race.
     * These are: Regatta, Race, Course
     */
    private void generateClasses() {
        RegattaGenerator regattaGenerator = new RegattaGenerator();
        regattaGenerator.generate();
        regatta = regattaGenerator.getRegatta();

        CourseGenerator courseGenerator = new CourseGenerator();
        courseGenerator.generate();
        course = courseGenerator.getCourse();

        RaceGenerator raceGenerator = new RaceGenerator();
        raceGenerator.generate();
        race = raceGenerator.getRace();
    }


    /**
     * Create a FileConstructor with the necessary objects as arguments (regatta, course, race)
     * so that the appropriate files can be constructed.
     */
    private void constructFiles() {
        FileConstructor fileConstructor = new FileConstructor(regatta, course, race);
        fileConstructor.constructFiles();
    }


    public void run() {
        generateClasses();
        constructFiles();

        server.openServer();

        initMessageGenerators();
        runSimulation();

        server.closeServer();
    }


    /**
     * Initialise the generators for scheduled messages
     */
    private void initMessageGenerators() {
        messages.add(new BoatMessageGenerator(race.getStartingList()));
    }


    /**
     * Simulate the race will sending the scheduled messages
     */
    private void runSimulation() {
        final int LOOP_FREQUENCY = 10;

        long timeCurr = System.currentTimeMillis();
        long timeLast;

        do {
            timeLast = timeCurr;
            timeCurr = System.currentTimeMillis();

            // Update simulation
            race.updateBoats((timeCurr - timeLast) * 1e3);

            // Send messages if needed
            for (ScheduledMessage sendable : messages) {
                if (sendable.isTimeToSend(timeCurr)) {
                    server.broadcast(sendable.getMessage());
                }
            }

            server.pruneConnections();

            // Sleep
            try {
                sleep(1000 / LOOP_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!race.isFinished());
    }
}
