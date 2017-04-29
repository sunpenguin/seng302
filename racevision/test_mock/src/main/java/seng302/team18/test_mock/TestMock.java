package seng302.team18.test_mock;

import seng302.team18.model.*;
import seng302.team18.test_mock.XMLparsers.*;
import seng302.team18.test_mock.connection.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;


/**
 * Class to handle a mock race
 */
public class TestMock {

    private Regatta regatta;
    private Course course;
    private ActiveRace race;

    private String regattaXML = "/AC35regatta.xml";
    private String boatsXML = "/AC35boats.xml";
    private String raceXML = "/AC35race.xml";

    private AC35RegattaContainer ac35RegattaContainer;
    private AC35BoatsContainer ac35BoatsContainer;
    private AC35RaceContainer ac35RaceContainer;

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
        AC35RegattaParser ac35RegattaParser = new AC35RegattaParser();
        ac35RegattaContainer = ac35RegattaParser.parse(this.getClass().getResourceAsStream(regattaXML));

        AC35BoatsParser ac35BoatsParser = new AC35BoatsParser();
        ac35BoatsContainer = ac35BoatsParser.parse(this.getClass().getResourceAsStream(boatsXML));

        AC35RaceParser ac35RaceParser = new AC35RaceParser();
        ac35RaceContainer = ac35RaceParser.parse(this.getClass().getResourceAsStream(raceXML));
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

    public ActiveRace testRun() {
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
            race.setRaceStatusNumber((byte) 3);
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
        Map<Integer, CompoundMark> compoundMarkMap = ac35RaceContainer.getCompoundMarks();
        List<CompoundMark> compoundMarks = new ArrayList<>();

        for (CompoundMark compoundMark : compoundMarkMap.values()) {
            compoundMarks.add(compoundMark);
        }

        List<BoundaryMark> boundaryMarks = ac35RaceContainer.getBoundaryMark();
        double windDirection = 0;

        ZoneId zoneId;
        String utcOffset = ac35RegattaContainer.getuTcOffset();
        if (utcOffset.startsWith("+") || utcOffset.startsWith("-")) {
            zoneId = ZoneId.of("UTC" + utcOffset);
        } else {
            zoneId = ZoneId.of("UTC+" + utcOffset);
        }

        Coordinate central = new Coordinate(ac35RegattaContainer.getCentralLatitude(), ac35RegattaContainer.getCentralLongtitude());

        course = new Course(compoundMarks, boundaryMarks, windDirection, zoneId);
        course.setCentralCoordinate(central);
    }

    private void generateRace() {
        List<Boat> startingList = ac35BoatsContainer.getBoats();
        int raceID = ac35RaceContainer.getRaceID();

        race = new ActiveRace(startingList, course, raceID);

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
