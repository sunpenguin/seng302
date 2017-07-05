package seng302.team18.test_mock;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.messageparsing.*;
import seng302.team18.model.*;
import seng302.team18.test_mock.ac35_xml_encoding.BoatsXmlEncoder;
import seng302.team18.test_mock.ac35_xml_encoding.RaceXmlEncoder;
import seng302.team18.test_mock.connection.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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
//    private final String boatsXML = "/boats_test2.xml";
//    private final String raceXML = "/race_test2.xml";
    private String boatXML;
    private String raceXML;

    private AC35XMLRegattaMessage regattaMessage;
    private AC35XMLBoatMessage initialBoatMessage;
    private AC35XMLRaceMessage initialRaceMessage;
    private AC35XMLBoatMessage boatMessage;
    private AC35XMLRaceMessage raceMessage;

    private final static int SERVER_PORT = 5005;

    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessageGenerator> scheduledMessages = new ArrayList<>();

    //TODO give real port
    private Server server;

    public void run() throws TransformerException, ParserConfigurationException{
        setUpFiles(); // Also set up the server
        readFiles();
        generateClasses();

        server.openServer();
        initialiseScheduledMessages();
        runSimulation();
        server.closeServer();
    }


    /**
     * Generate race.xml and boat.xml files from the default initial AC35 XML message.
     */
    private void setUpFiles() throws TransformerException, ParserConfigurationException{
        // race.xml
        RaceXmlEncoder raceEncoder = new RaceXmlEncoder();
        String initialRaceXML = "/race_test2.xml";
        AC35XMLRaceParser raceParser = new AC35XMLRaceParser();
        initialRaceMessage = raceParser.parse(this.getClass().getResourceAsStream(initialRaceXML));
        raceXML = raceEncoder.encode(initialRaceMessage);

        // boat.xml
        BoatsXmlEncoder boatEncoder = new BoatsXmlEncoder();
        String initialBoatXML = "/boats_test2.xml";
        AC35XMLBoatParser boatParser = new AC35XMLBoatParser();
        initialBoatMessage = boatParser.parse(this.getClass().getResourceAsStream(initialBoatXML));
        boatXML = boatEncoder.encode(initialBoatMessage);

        // create a new server
        server = new Server(SERVER_PORT, initialRaceMessage);
    }


    /**
     * Read each test xml file and generate AC35 XML messages so classes can be made
     */
    private void readFiles() {
        AC35XMLRegattaParser regattaParser = new AC35XMLRegattaParser();
        regattaMessage = regattaParser.parse(this.getClass().getResourceAsStream(regattaXML));

        AC35XMLBoatParser boatsParser = new AC35XMLBoatParser();
        boatMessage = boatsParser.parse(boatXML.getBytes());

        AC35XMLRaceParser raceParser = new AC35XMLRaceParser();
        System.out.println(raceXML);
        raceMessage = raceParser.parse(raceXML.getBytes());
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

            accelerateBoat(race, race.getStartingList().get(2), 0.1);
            accelerateBoat(race, race.getStartingList().get(3), 0.05);

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


    /**
     * Used for testing to avoid having to
     * run test mock to test that messages
     * encode correctly.
     * @return returns generated race
     */
    public Race testRun() throws TransformerException, ParserConfigurationException{
//        setUpFiles();
        readFiles();
        generateClasses();
        return race;
    }


    private void accelerateBoat(Race race, Boat boat, double acceleration) {
        if (!race.getFinishedList().contains(boat)) {
            boat.setSpeed(boat.getSpeed() + acceleration);
        }
    }


    public String getRegattaXML() {
        return regattaXML;
    }


    public String getBoatsXML() {
        return boatXML;
    }


    public String getRaceXML() {
        return raceXML;
    }
}
