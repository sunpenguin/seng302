package seng302.team18.test_mock;

import seng302.team18.messageparsing.*;
import seng302.team18.model.*;
import seng302.team18.test_mock.connection.*;
import seng302.team18.test_mock.parsers.PolarParser;

import java.time.ZoneId;
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
    private Polar polarsMap;

    private String regattaXML = "/AC35regatta.xml";
    private String boatsXML = "/AC35boats.xml";
    private String raceXML = "/AC35race.xml";
    private String polars = "/polar.txt";

    private AC35XMLRegattaMessage regattaMessage;
    private AC35XMLBoatMessage boatMessage;
    private AC35XMLRaceMessage raceMessage;

    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessage> messages = new ArrayList<>();

    //TODO give real port
    private Server server = new Server(5005, regattaXML, boatsXML, raceXML);

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

        PolarParser polarParser = new PolarParser();
        polarsMap = polarParser.parse(this.getClass().getResourceAsStream(polars));
    }

    public void run() {
        // comment out to see checksum result.
//        byte[] message = new byte[4];
//        message[0] = 0;
//        message[1] = 1;
//        message[2] = 2;
//        message[3] = 3;
//
//        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
//
//        try {
//            outputSteam.write(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        byte[] check = outputSteam.toByteArray();
//
//        CRCGenerator crcGenerator = new CRCGenerator();
//        crcGenerator.generateCRC(check);

        readFiles();
        generateClasses();

//        try {
//            byte[] header = HeaderGenerator.generateHeader(26, (short) 8);
//            RaceMessageGenerator racemsg = new RaceMessageGenerator(race);
//            byte[] raceBody = racemsg.getPayload();
//
//            byte[] combined = new byte[header.length + raceBody.length];
//            System.arraycopy(header,0,combined,0         ,header.length);
//            System.arraycopy(raceBody,0,combined,header.length,raceBody.length);
//
//            CRCGenerator crcGenerator = new CRCGenerator();
//            crcGenerator.generateCRC(combined);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        server.openServer();

        initMessageGenerators();
        runSimulation();

        server.closeServer();
    }

    public Race testRun() {
        readFiles();
        generateClasses();
        return race;
    }

    /**
     * Initialise the generators for scheduled messages
     */
    private void initMessageGenerators() {
        for(Boat b : race.getStartingList()){
           messages.add(new BoatMessageGenerator(b));
        }
        messages.add(new RaceMessageGenerator(race));
        messages.add(new HeartBeatMessageGenerator());
    }


    /**
     * Simulate the race will sending the scheduled messages
     */
    private void runSimulation() {
        final int LOOP_FREQUENCY = 60;

        long timeCurr = System.currentTimeMillis();
        long timeLast;
        List<Boat> boats = race.getStartingList();
        do {
            timeLast = timeCurr;
            timeCurr = System.currentTimeMillis();

            // Update simulation
            race.setStatus((byte) 3);
            race.updateBoats((timeCurr - timeLast));

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

    private void generateCourse() {
        List<CompoundMark> compoundMarks = raceMessage.getCompoundMarks();

        List<BoundaryMark> boundaryMarks = raceMessage.getBoundaryMarks();
        double windDirection = 0;

        ZoneId zoneId;
        String utcOffset = regattaMessage.getUtcOffset();
        if (utcOffset.startsWith("+") || utcOffset.startsWith("-")) {
            zoneId = ZoneId.of("UTC" + utcOffset);
        } else {
            zoneId = ZoneId.of("UTC+" + utcOffset);
        }

        Coordinate central = new Coordinate(regattaMessage.getCentralLat(), regattaMessage.getCentralLong());

        course = new Course(compoundMarks, boundaryMarks, windDirection, zoneId);
        course.setCentralCoordinate(central);
    }

    private void generateRace() {
        List<Boat> startingList = boatMessage.getBoats();
        int raceID = raceMessage.getRaceID();

        race = new Race(startingList, course, raceID, polarsMap);

//        RaceMessageGenerator raceMessageGenerator = new RaceMessageGenerator(race);
//        raceMessageGenerator.getMessage();
    }

    public String getRegattaXML() {
        return regattaXML;
    }

    public String getBoatsXML() {
        return boatsXML;
    }

    public String getRaceXML() {
        return raceXML;
    }
}
