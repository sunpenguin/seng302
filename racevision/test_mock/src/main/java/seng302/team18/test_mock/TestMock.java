package seng302.team18.test_mock;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.messageparsing.*;
import seng302.team18.model.*;
import seng302.team18.test_mock.connection.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Thread.sleep;


/**
 * Class to handle a mock race
 */
public class TestMock {

    private Course course;
    private Race race;

    private final String regattaXML = "/regatta_test1.xml";
    private final String boatsXML = "/boats_test2.xml";
    private final String raceXML = "/race_test2.xml";

    private AC35XMLRegattaMessage regattaMessage;
    private AC35XMLBoatMessage boatMessage;
    private AC35XMLRaceMessage raceMessage;

    private final static int SERVER_PORT = 5005;

    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessageGenerator> scheduledMessages = new ArrayList<>();

    //TODO give real port
    private Server server = new Server(SERVER_PORT, regattaXML, boatsXML, raceXML);

    /**
     * Generate the classes necessary to visualise a mock race.
     * These are: Regatta, Race, Course
     */
    private void generateClasses() {
        generateCourse();
        generateRace();
    }

    /**
     * Read each test xml file and fill the containers so classes can be made
     */
    private void readFiles() {
        AC35XMLRegattaParser regattaParser = new AC35XMLRegattaParser();
        regattaMessage = regattaParser.parse(this.getClass().getResourceAsStream(regattaXML));

        AC35XMLBoatParser boatsParser = new AC35XMLBoatParser();
        boatMessage = boatsParser.parse(this.getClass().getResourceAsStream(boatsXML));

        AC35XMLRaceParser raceParser = new AC35XMLRaceParser();
        raceMessage = raceParser.parse(this.getClass().getResourceAsStream(raceXML));
    }
    
    public void run() {

        readFiles();
        generateClasses();

        server.openServer();

        initialiseScheduledMessages();
        runSimulation();

        server.closeServer();
    }

    /**
     * Used for testing to avoid having to
     * run test mock to test that messages
     * encode correctly.
     * @return
     */
    public Race testRun() {
        readFiles();
        generateClasses();
        return race;
    }

    /**
     * Initialise the generators for scheduled messages
     */
    private void initialiseScheduledMessages() {
        for(Boat b : race.getStartingList()){
            scheduledMessages.add(new BoatMessageGenerator(b));
        }
        scheduledMessages.add(new RaceMessageGenerator(race));
        scheduledMessages.add(new HeartBeatMessageGenerator());
    }


    /**
     * Simulate the race will sending the scheduled messages
     */
    private void runSimulation() {
        final int LOOP_FREQUENCY = 60;

        long timeCurr = System.currentTimeMillis();
        long timeLast;
        do {
            timeLast = timeCurr;
            timeCurr = System.currentTimeMillis();

            // Update simulation
            race.setStatus((byte) 3);
            race.updateBoats((timeCurr - timeLast));

//            accelerateBoat(race, race.getStartingList().get(2), 0.1);
//            accelerateBoat(race, race.getStartingList().get(3), 0.05);

            // Send mark rounding messages for all mark roundings that occured
            for (MarkRoundingEvent rounding : race.popMarkRoundingEvents()) {
                server.broadcast((new MarkRoundingMessageGenerator(rounding, race.getId())).getMessage());
            }

            // Send messages if needed
            for (ScheduledMessageGenerator sendable : scheduledMessages) {
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

        // Sends final message
        ScheduledMessageGenerator raceMessageGenerator = new RaceMessageGenerator(race);
        server.broadcast(raceMessageGenerator.getMessage());
    }

    private void generateCourse() {
        List<CompoundMark> compoundMarks = raceMessage.getCompoundMarks();

        List<BoundaryMark> boundaryMarks = raceMessage.getBoundaryMarks();
        double windDirection = 0;

        List<MarkRounding> markRoundings = raceMessage.getMarkRoundings();

        ZoneId zoneId;
        String utcOffset = regattaMessage.getUtcOffset();
        if (utcOffset.startsWith("+") || utcOffset.startsWith("-")) {
            zoneId = ZoneId.of("UTC" + utcOffset);
        } else {
            zoneId = ZoneId.of("UTC+" + utcOffset);
        }

        Coordinate central = new Coordinate(regattaMessage.getCentralLat(), regattaMessage.getCentralLong());

        course = new Course(compoundMarks, boundaryMarks, windDirection, zoneId, markRoundings);
        course.setCentralCoordinate(central);
    }

    private void generateRace() {
        List<Boat> startingList = boatMessage.getBoats();
        int raceID = raceMessage.getRaceID();

        race = new Race(startingList, course, raceID);
    }

    /**
     * Adds acceleration to the current speed of the boat if the boat is not finished
     *
     * @param race the boat is participating
     * @param boat
     * @param acceleration amount to increase speed by in knots.
     */
    private void accelerateBoat(Race race, Boat boat, double acceleration) {
        if (!race.getFinishedList().contains(boat)) {
            boat.setSpeed(boat.getSpeed() + acceleration);
        }
    }
}
