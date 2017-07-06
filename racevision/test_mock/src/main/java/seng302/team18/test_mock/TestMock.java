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

    private Race race;
    private final static int SERVER_PORT = 5005;
    private Server server;


    public TestMock(Race race) {
        this.race = race;
        this.server = new Server(SERVER_PORT, this);
    }


    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessageGenerator> scheduledMessages = new ArrayList<>();


    public void run() {
        server.openServer();
        runSimulation();
        server.closeServer();
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
}
