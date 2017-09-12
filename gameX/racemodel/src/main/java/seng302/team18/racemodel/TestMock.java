package seng302.team18.racemodel;



import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.RequestType;
import seng302.team18.model.*;
import seng302.team18.racemodel.ac35_xml_encoding.XmlMessageBuilder;
import seng302.team18.racemodel.connection.ClientConnection;
import seng302.team18.racemodel.connection.ConnectionListener;
import seng302.team18.racemodel.connection.Server;
import seng302.team18.racemodel.connection.ServerState;
import seng302.team18.racemodel.message_generating.*;
import seng302.team18.racemodel.model.AbstractCourseBuilder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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


    private SimulationLoop simulationLoop = null;


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

    /**
     * Called by server, and connection listener
     *
     * @param o object that has updated
     * @param arg given by the object o
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ClientConnection) { // Server ?
            ClientConnection client = (ClientConnection) arg;
            Boat b = boats.get(race.getStartingList().size());
            race.addParticipant(b); // TODO: Justin 10/09 This should not be done here. We should wait until they register.
            scheduledMessages.add(new BoatMessageGenerator(b));
            client.setId(boats.get(race.getStartingList().size()).getId());

            generateXMLs();
            sendRegattaXml(client);
            sendRaceXml();
            sendBoatsXml();

        } else if (arg instanceof ServerState) {
            open = !ServerState.CLOSED.equals(arg);
        } else if (arg instanceof Integer) {
            Integer id = (Integer) arg;
            race.setBoatStatus(id, BoatStatus.DNF);
        } else if (arg instanceof ConnectionListener) { // ConnectionListener
            generateXMLs();
            sendRaceXml();
            sendBoatsXml();

            if (simulationLoop == null) {
                simulationLoop = new SimulationLoop();
                Thread t = new Thread(simulationLoop);
                t.start();
            }
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
     * Broadcast the race xml to all clients.
     */
    private void sendRaceXml() {
        server.broadcast(generatorXmlRace.getMessage());
    }


    /**
     * Broadcast the boats xml to all clients.
     */
    private void sendBoatsXml() {
        server.broadcast(generatorXmlBoats.getMessage());
    }


    /**
     * Send the regatta XML file to a new client.
     *
     * @param newPlayer ClientConnection used to send message through.
     */
    private void sendRegattaXml(ClientConnection newPlayer) {
        newPlayer.sendMessage(generatorXmlRegatta.getMessage());
    }


    /**
     * Run the race.
     * Updates the position of boats
     *
     * @param timeCurr The current time (milliseconds)
     * @param timeLast The time (milliseconds) from the previous loop in runSimulation.
     */
    private void runRace(long timeCurr, long timeLast) {
        race.update((timeCurr - timeLast));
    }


    /**
     * Update the clients by sending any necessary new race info to them.
     * Sends out updates for positions, mark roundings, etc.
     *
     * @param timeCurr The current time (milliseconds)
     */
    private void updateClients(long timeCurr) {
        for (ScheduledMessageGenerator generator : scheduledMessages) {
            if (generator.isTimeToSend(timeCurr)) {
                server.broadcast(generator.getMessage());
            }
        }

        for (MarkRoundingEvent rounding : race.popMarkRoundingEvents()) {
            server.broadcast((new MarkRoundingMessageGenerator(rounding, race.getId())).getMessage());
        }

        for (YachtEvent event : race.popYachtEvents()) {
            server.broadcast((new YachtEventCodeMessageGenerator(event, race.getId())).getMessage());
        }

        for (PickUp pickUp : race.getPickUps()) {
            server.broadcast(new PowerUpMessageGenerator(pickUp).getMessage());
        }

        for (PowerUpEvent event : race.popPowerUpEvents()) {
            server.broadcast((new PowerTakenGenerator(event.getBoatId(), event.getPowerId(), event.getPowerDuration()).getMessage()));
        }
    }


    /**
     * Send the final messages and set race status to Finished.
     */
    private void sendFinalMessages() {
        race.setStatus(RaceStatus.FINISHED);
        ScheduledMessageGenerator raceMessageGenerator = new RaceMessageGenerator(race);
        server.broadcast(raceMessageGenerator.getMessage());
        for (ScheduledMessageGenerator generator : scheduledMessages) {
            server.broadcast(generator.getMessage());
        }
    }


    private class SimulationLoop implements Runnable {

        private boolean firstTime = true;

        @Override
        public void run() {
            final int LOOP_FREQUENCY = 60;

            long timeCurr = System.currentTimeMillis();
            long timeLast;

            scheduledMessages.add(new RaceMessageGenerator(race));
            scheduledMessages.add(new HeartBeatMessageGenerator());

            do {
                if (race.getStatus().equals(RaceStatus.STARTED)) {
                    generatorXmlRace = new XmlMessageGeneratorRace(xmlMessageBuilder.buildRaceXmlMessage(race));
                    sendRaceXml();
                }

                timeLast = timeCurr;
                timeCurr = System.currentTimeMillis();

                race.setCurrentTime(ZonedDateTime.now());

                runRace(timeCurr, timeLast);

                if (firstTime && race.getStatus().equals(RaceStatus.PREPARATORY)) {
                    generateXMLs();
                    sendBoatsXml();
                    sendRaceXml();
                    firstTime = false;
                }

                updateClients(timeCurr);

                // Sleep
                try {
                    Thread.sleep(1000 / LOOP_FREQUENCY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!race.isFinished() && open);

            sendFinalMessages();
            server.close();
        }
    }
}