package seng302.team18.test_mock;

import seng302.team18.model.Boat;
import seng302.team18.model.MarkRoundingEvent;
import seng302.team18.model.Race;
import seng302.team18.model.RaceStatus;
import seng302.team18.test_mock.connection.*;

import java.time.ZonedDateTime;
import java.util.*;

import static java.lang.Thread.sleep;


/**
 * Class to handle a mock race
 */
public class TestMock implements Observer {

    private Race race;
    private final static int SERVER_PORT = 5005;
    private Server server;
    private List<Boat> boats = Arrays.asList(new Boat("Emirates Team New Zealand", "TEAM New Zealand", 121),
            new Boat("Oracle Team USA", "TEAM USA", 122),
            new Boat("Artemis Racing", "TEAM SWE", 123),
            new Boat("Groupama Team France", "TEAM France", 124),
            new Boat("Land Rover BAR", "TEAM Britain", 125),
            new Boat("Softbank Team Japan", "TEAM Japan", 126));



    public TestMock(Race race) {
        this.race = race;
        this.server = new Server(SERVER_PORT);
        server.addObserver(this);
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
    public void update(Observable o, Object arg) {
        if (arg instanceof ClientConnection) {
            ClientConnection client = (ClientConnection) arg;
            client.sendMessage(new byte[10]); // TODO send the real message
            race.addParticipant(boats.get(race.getStartingList().size())); // Maybe a bug
        }
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
