package seng302.team18.racemodel;

import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.RequestType;
import seng302.team18.model.*;
import seng302.team18.racemodel.ac35_xml_encoding.XmlMessageBuilder;
import seng302.team18.racemodel.connection.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



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

    /**
     * Called by server, race, and connection listener
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ClientConnection) {
            ClientConnection client = (ClientConnection) arg;
            race.addParticipant(boats.get(race.getStartingList().size())); // Maybe a bug
            client.setId(boats.get(race.getStartingList().size()).getId());
            generateXMLs();
            sendXmlRegatta(client);
            sendXmlBoatRace();
        } else if (arg instanceof ServerState) {
            open = !((ServerState) arg).equals(ServerState.CLOSED);
        } else if (arg instanceof Integer) {
            Integer id = (Integer) arg;
            race.setBoatStatus(id, BoatStatus.DNF);
        } else if (arg instanceof ConnectionListener) {
            generateXMLs();
            sendXmlBoatRace();
        } else if (arg instanceof AcceptanceMessage) {
            AcceptanceMessage message = (AcceptanceMessage) arg;
            if (message.getRequestType() == RequestType.FAILURE_CLIENT_TYPE) {
                System.out.println("Remove " + message.getSourceId() + " From the race");
                race.removeParticipant(message.getSourceId());
                System.out.println(race.getStartingList());
                generateXMLs();
                sendXmlBoatRace();
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
     * @param startWaitTime    Number of seconds between the preparation phase and the start time
     * @param warningWaitTime  Number of seconds between the time the method is executed and warning phase
     * @param prepWaitTime     Number of seconds between the warning phase and the preparation phase
     * @param cutOffDifference Number of seconds before entering the warning phase for not allowing new connections
     */
    public void runSimulation(int startWaitTime, int warningWaitTime, int prepWaitTime, int cutOffDifference) {
        // TODO David, Sunguin 01/09/2017 Check tutorial mode
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

        scheduledMessages.add(new RaceMessageGenerator(race));
        scheduledMessages.add(new HeartBeatMessageGenerator());
        race.setStartTime(ZonedDateTime.now().plusSeconds(warningWaitTime + prepWaitTime + startWaitTime));

        long cutOffDelay = warningWaitTime - cutOffDifference;
        executor.schedule(server::stopAcceptingConnections, cutOffDelay, TimeUnit.SECONDS);

        executor.schedule(() -> race.setStatus(RaceStatus.WARNING), warningWaitTime, TimeUnit.SECONDS);

        long prepDelay = warningWaitTime + prepWaitTime;
        executor.schedule(() -> {
            generateXMLs();
            sendXmlBoatRace();
            switchToPrep();
        }, prepDelay, TimeUnit.SECONDS);

        long startDelay = startWaitTime + warningWaitTime + prepWaitTime;
        executor.schedule(this::switchToStarted, startDelay, TimeUnit.SECONDS);

        executor.scheduleWithFixedDelay(new Runnable() {
            long timeCurr = System.currentTimeMillis();
            long timeLast;
            @Override
            public void run() {
                timeLast = timeCurr;
                timeCurr = System.currentTimeMillis();
                updateRace(timeCurr, timeLast);
            }
        }, 0, 16, TimeUnit.MILLISECONDS);

    }


    private void updateRace(long timeCurr, long timeLast) {
        if (race.getMode() == RaceMode.CONTROLS_TUTORIAL) {
            generateXMLs();
            sendXmlBoatRace();
            switchToPrep();
        }
        race.setCurrentTime(ZonedDateTime.now());
        race.update(timeCurr - timeLast);
        updateClients(timeCurr);
    }


    /**
     * Switch into the preparatory stage.
     * At this time, also stop accepting new connections and set up a BoatMessageGenerator for each boat.
     */
    private void switchToPrep() {
        race.setStatus(RaceStatus.PREPARATORY);
        for (Boat b : race.getStartingList()) {
            scheduledMessages.add(new BoatMessageGenerator(b));
        }
    }


    /**
     * Switch the RaceStatus to STARTED.
     */
    private void switchToStarted() {
        race.setStatus(RaceStatus.STARTED);
        race.getStartingList().stream()
                .filter(boat -> boat.getStatus().equals(BoatStatus.PRE_START))
                .forEach(boat -> boat.setStatus(BoatStatus.RACING));
    }



    /**
     * Update the clients by sending any necessary new race info to them.
     * Sends out updates for positions, mark roundings, etc.
     *
     * @param timeCurr The current time (milliseconds)
     */
    private void updateClients(long timeCurr) {
        for (ScheduledMessageGenerator sendable : scheduledMessages) {
            if (sendable.isTimeToSend(timeCurr)) {
                server.broadcast(sendable.getMessage());
            }
        }

        for (MarkRoundingEvent rounding : race.popMarkRoundingEvents()) {
            server.broadcast((new MarkRoundingMessageGenerator(rounding, race.getId())).getMessage());
        }

        for (YachtEvent event : race.popYachtEvents()) {
            server.broadcast((new YachtEventCodeMessageGenerator(event, race.getId())).getMessage());
        }
    }
}