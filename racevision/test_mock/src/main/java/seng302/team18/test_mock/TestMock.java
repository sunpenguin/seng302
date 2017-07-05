package seng302.team18.test_mock;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.messageparsing.AC35XMLBoatParser;
import seng302.team18.messageparsing.AC35XMLRaceParser;
import seng302.team18.messageparsing.AC35XMLRegattaParser;
import seng302.team18.model.*;
import seng302.team18.test_mock.connection.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * Class to handle a mock race
 */
public class TestMock implements ParticipantManager {

    private Course course;
    private Race race;

    private AC35XMLRegattaMessage regattaMessage;
    private AC35XMLBoatMessage boatMessage;
    private AC35XMLRaceMessage raceMessage;

    private final static int SERVER_PORT = 5005;

    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessageGenerator> scheduledMessages = new ArrayList<>();

    private Server server;

    public void run() throws TransformerException, ParserConfigurationException {
        setUpFiles();
        generateClasses();

        server = new Server(SERVER_PORT, this, raceMessage, boatMessage, regattaMessage);

        server.openServer();
        runSimulation();
        server.closeServer();
    }


    /**
     * Generate race.xml and boat.xml files from the default initial AC35 XML message.
     */
    private void setUpFiles() throws TransformerException, ParserConfigurationException {
        // race.xml
        String initialRaceXML = "/race_test2.xml";
        AC35XMLRaceParser raceParser = new AC35XMLRaceParser();
        raceMessage = raceParser.parse(this.getClass().getResourceAsStream(initialRaceXML));

        // boat.xml
        String initialBoatXML = "/boats_test2.xml";
        AC35XMLBoatParser boatParser = new AC35XMLBoatParser();
        boatMessage = boatParser.parse(this.getClass().getResourceAsStream(initialBoatXML));

        // regatta.xml
        String initialRegattaXML = "/regatta_test1.xml";
        AC35XMLRegattaParser regattaParser = new AC35XMLRegattaParser();
        regattaMessage = regattaParser.parse(this.getClass().getResourceAsStream(initialRegattaXML));
    }


    /**
     * Generate the classes necessary to visualise a mock race.
     * These are: Regatta, Race, Course
     */
    private void generateClasses() {
        generateCourse();
        generateRace();
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

    @Override
    public void addBoat(Boat boat) {
        race.addParticipant(boat);

        System.out.print("Boats: ");
        for (Boat b : race.getStartingList()) {
            System.out.print(b.getName() + " " + b.getId() + ", ");
        }
        System.out.println();
    }


    /**
     * Simulate the race will sending the scheduled messages
     */
    private void runSimulation() {
        final int LOOP_FREQUENCY = 60;
        final int TIME_START = -5;
        final int TIME_WARNING = -3;
        final int TIME_PREP = -2;

        long timeCurr = System.currentTimeMillis();
        long timeLast;

        scheduledMessages.add(new RaceMessageGenerator(race));
        scheduledMessages.add(new HeartBeatMessageGenerator());

        // Set race time
        race.setStartTime(ZonedDateTime.now().minusMinutes(TIME_START));
        race.setStatus(RaceStatus.PRESTART);

        do {
            timeLast = timeCurr;
            timeCurr = System.currentTimeMillis();

            System.out.println(race.getStartTime().toString());

            if ((race.getStatus() == RaceStatus.PRESTART) && ZonedDateTime.now().isAfter(race.getStartTime().plusMinutes(TIME_WARNING))) {
                race.setStatus(RaceStatus.WARNING);

            } else if ((race.getStatus() == RaceStatus.WARNING) && ZonedDateTime.now().isAfter((race.getStartTime().plusMinutes(TIME_PREP)))) {

                race.setStatus(RaceStatus.PREPARATORY);
                server.stopAcceptingConnections();

                for (Boat b : race.getStartingList()) {
                    scheduledMessages.add(new BoatMessageGenerator(b));
                }

            } else {
                race.updateBoats((timeCurr - timeLast));

                // Send mark rounding messages for all mark roundings that occured
                for (MarkRoundingEvent rounding : race.popMarkRoundingEvents()) {
                    server.broadcast((new MarkRoundingMessageGenerator(rounding, race.getId())).getMessage());
                }

                if ((race.getStatus() == RaceStatus.PREPARATORY) && ZonedDateTime.now().isAfter(race.getStartTime())) {
                    race.setStatus(RaceStatus.STARTED);
                }
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


    /**
     * Used for testing to avoid having to
     * run test mock to test that messages
     * encode correctly.
     *
     * @return returns generated race
     */
    public Race testRun() throws TransformerException, ParserConfigurationException {
        setUpFiles();
        generateClasses();
        return race;
    }
}
