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
    private String regattaXML;
    private String boatsXML;
    private String raceXML;
    private Race race;
    private final static int SERVER_PORT = 5005;
    private Server server;


    public TestMock(String regattaXML, String boatsXML, String raceXML, Race race) {
        this.regattaXML = regattaXML;
        this.boatsXML = boatsXML;
        this.raceXML = raceXML;
        this.race = race;
        this.server = new Server(SERVER_PORT, regattaXML, boatsXML, raceXML);
    }

    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessageGenerator> scheduledMessages = new ArrayList<>();

    
    public void run() {
        server.openServer();

        initialiseScheduledMessages();
        runSimulation();

        server.closeServer();
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


    private void accelerateBoat(Race race, Boat boat, double acceleration) {
        if (!race.getFinishedList().contains(boat)) {
            boat.setSpeed(boat.getSpeed() + acceleration);
        }
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
