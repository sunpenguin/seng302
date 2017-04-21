package seng302.team18.test_mock;

import seng302.team18.model.Boat;
import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;
import seng302.team18.test_mock.XMLparsers.*;
import seng302.team18.test_mock.connection.BoatMessageGenerator;
import seng302.team18.test_mock.connection.ScheduledMessage;
import seng302.team18.test_mock.connection.Server;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * Class to handle a mock race
 */
public class TestMock {

    private Regatta regatta;
    private Course course;
    private Race race;

    private AC35RegattaContainer ac35RegattaContainer;
    private AC35BoatsContainer ac35BoatsContainer;
    private AC35RaceContainer ac35RaceContainer;

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
    }

    /**
     * Read each test xml file and fill the containers so classes can be made
     */
    private void readFiles() {
        AC35RegattaParser ac35RegattaParser = new AC35RegattaParser();
        ac35RegattaContainer = ac35RegattaParser.parse(this.getClass().getResourceAsStream("/AC35regatta.xml"));

        AC35BoatsParser ac35BoatsParser = new AC35BoatsParser();
        ac35BoatsContainer = ac35BoatsParser.parse(this.getClass().getResourceAsStream("/AC35boats.xml"));

        AC35RaceParser ac35RaceParser = new AC35RaceParser();
        ac35RaceContainer = ac35RaceParser.parse(this.getClass().getResourceAsStream("/AC35race.xml"));
    }

    public void run() {
        readFiles();

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
