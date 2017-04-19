package seng302.team18.test_mock;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;
import seng302.team18.test_mock.mock_generators.CourseGenerator;
import seng302.team18.test_mock.mock_generators.RaceGenerator;
import seng302.team18.test_mock.mock_generators.RegattaGenerator;

import java.util.List;

import static java.lang.Thread.sleep;


/**
 * Created by Justin on 18/04/2017.
 */
public class TestMock {

    private Regatta regatta;
    private Course course;
    private Race race;

    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessage> messages;

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

        // wait for connection
        // TODO add list of connections

        initMessageSenders();
        runSimulation();
    }


    /**
     * Initialise the generators for scheduled messages
     */
    private void initMessageSenders() {
        messages.add(new BoatMessageGenerator(race.getStartingList()));
    }


    /**
     * Simulate the race will sending the scheduled messages
     */
    private void runSimulation() {
        final int LOOP_FREQUENCY = 10;
        boolean simulate = true;

        long timeCurr = System.currentTimeMillis();
        long timeLast;

        do {
            timeLast = timeCurr;
            timeCurr = System.currentTimeMillis();

            race.updateBoats((timeCurr - timeLast) * 1e3);

            for (ScheduledMessage sendable : messages) {
                sendable.send(timeCurr);
            }

            try {
                sleep(1000 / LOOP_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (race.isFinished()) {
                simulate = false;
            }
        } while (simulate);
    }
}
