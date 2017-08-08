package seng302.team18.racemodel;

import seng302.team18.model.*;
import seng302.team18.racemodel.connection.*;
import seng302.team18.racemodel.ac35_xml_encoding.XmlMessageBuilder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;


/**
 * Class to handle a mock race
 */
public class TestMock implements Observer {

    private Race race;
    private final Server server;
    private final XmlMessageBuilder xmlMessageBuilder;
    private final List<Boat> boats;
    private boolean open = true;

    private MessageGenerator generatorXmlRegatta;
    private MessageGenerator generatorXmlBoats;
    private MessageGenerator generatorXmlRace;


    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessageGenerator> scheduledMessages = new ArrayList<>();


    public TestMock(Server server, XmlMessageBuilder messageBuilder, Race race, List<Boat> boats) {
        this.server = server;
        this.xmlMessageBuilder = messageBuilder;
        this.race = race;
        this.boats = boats;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ClientConnection) {
            ClientConnection client = (ClientConnection) arg;
            race.addParticipant(boats.get(race.getStartingList().size())); // Maybe a bug
            generateXMLs();
            sendXmlRegatta(client);
            sendXmlBoatRace();
        } else if (arg instanceof ServerState) {
            open = !((ServerState) arg).equals(ServerState.CLOSED);
        } else if (arg instanceof ConnectionListener) {
            generateXMLs();
            sendXmlBoatRace();
        }
    }


    /**
     * Generate the XML files to be sent to clients.
     */
    private void generateXMLs() {
        generatorXmlRegatta = new XmlMessageGeneratorRegatta(xmlMessageBuilder.buildRegattaMessage(race));
        generatorXmlRace = new XmlMessageGeneratorRace(xmlMessageBuilder.buildRaceXmlMessage(race));
        generatorXmlBoats = new XmlMessageGeneratorBoats(xmlMessageBuilder.buildBoatsXmlMessage(race));
    }


    /**
     * Broadcast the race and boat XML files to all clients.
     */
    private void sendXmlBoatRace() {
        server.broadcast(generatorXmlRace.getMessage());
        server.broadcast(generatorXmlBoats.getMessage());
    }


    /**
     * Send the regatta XML file to a new client.
     *
     * @param newPlayer ClientConnection used to send message through.
     */
    private void sendXmlRegatta(ClientConnection newPlayer) {
        newPlayer.sendMessage(generatorXmlRegatta.getMessage());
    }


    /**
     * Simulate the race while sending the scheduled messages
     *
     * @param startWaitTime Number of seconds between the preparation phase and the start time
     * @param warningWaitTime Number of seconds between the time the method is executed and warning phase
     * @param prepWaitTime Number of seconds between the warning phase and the preparation phase
     * @param cutoffDifference Number of seconds before entering the warning phase for not allowing new connections
     */
    public void runSimulation(int startWaitTime, int warningWaitTime, int prepWaitTime, int cutoffDifference) {
        final int LOOP_FREQUENCY = 60;

        long timeCurr = System.currentTimeMillis();
        long timeLast;

        scheduledMessages.add(new RaceMessageGenerator(race));
        scheduledMessages.add(new HeartBeatMessageGenerator());

        // Set race time
        ZonedDateTime initialTime = ZonedDateTime.now();
        ZonedDateTime warningTime = initialTime.plusSeconds(warningWaitTime);
        ZonedDateTime prepTime = warningTime.plusSeconds(prepWaitTime);
        ZonedDateTime connectionCutOff = warningTime.minusSeconds(cutoffDifference);
        race.setStartTime(prepTime.plusSeconds(startWaitTime));

        race.setStatus(RaceStatus.PRESTART);

        do {
            timeLast = timeCurr;
            timeCurr = System.currentTimeMillis();

            if (ZonedDateTime.now().isAfter(connectionCutOff)) {
                server.stopAcceptingConnections();
            }

            if ((race.getStatus() == RaceStatus.PRESTART) && ZonedDateTime.now().isAfter(warningTime)) {
                race.setStatus(RaceStatus.WARNING);

            } else if ((race.getStatus() == RaceStatus.WARNING) && ZonedDateTime.now().isAfter(prepTime)) {

                race.setStatus(RaceStatus.PREPARATORY);
                server.stopAcceptingConnections();

                for (Boat b : race.getStartingList()) {
                    scheduledMessages.add(new BoatMessageGenerator(b));
                }

            } else {
                race.updateBoats((timeCurr - timeLast));
                // Send mark rounding messages for all mark roundings that occurred
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

            // Sleep
            try {
                sleep(1000 / LOOP_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!race.isFinished() && open);

        // Sends final message
        ScheduledMessageGenerator raceMessageGenerator = new RaceMessageGenerator(race);
        server.broadcast(raceMessageGenerator.getMessage());
    }
}