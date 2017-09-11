package seng302.team18.racemodel;



import seng302.team18.message.AC35MessageType;
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
import java.util.*;

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

    private AbstractCourseBuilder courseBuilder;
    private boolean shouldSendXML = false;


    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessageGenerator> scheduledMessages = new ArrayList<>();


    public TestMock(Server server, XmlMessageBuilder messageBuilder, Race race, List<Boat> boats, AbstractCourseBuilder courseBuilder) {
        this.server = server;
        this.xmlMessageBuilder = messageBuilder;
        this.race = race;
        this.boats = boats;
        this.courseBuilder = courseBuilder;
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
            race.addParticipant(boats.get(race.getStartingList().size())); // Maybe a bug
            client.setId(boats.get(race.getStartingList().size()).getId());
            generateXMLs();
            sendXmlRegatta(client);
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
        } else if (arg instanceof AcceptanceMessage) {
            AcceptanceMessage message = (AcceptanceMessage) arg;
            if (message.getRequestType() == RequestType.FAILURE_CLIENT_TYPE) {
                System.out.println("Remove " + message.getSourceId() + " From the race");
                race.removeParticipant(message.getSourceId());
                System.out.println(race.getStartingList());
                generateXMLs();
                sendRaceXml();
                sendBoatsXml();
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
    private void sendRaceXml() {
        server.broadcast(generatorXmlRace.getMessage());
    }


    private void sendBoatsXml() {
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
        ZonedDateTime timeToUpdateChallengeCourse = ZonedDateTime.now().plusNanos(50*1000000);
        race.setStartTime(prepTime.plusSeconds(startWaitTime));

        race.setStatus(RaceStatus.PRESTART);


        do {
            if (shouldSendXML) {
                generatorXmlRace = new XmlMessageGeneratorRace(xmlMessageBuilder.buildRaceXmlMessage(race));
                sendRaceXml();
            }

            timeLast = timeCurr;
            timeCurr = System.currentTimeMillis();

            if (ZonedDateTime.now().isAfter(connectionCutOff)) {
                server.stopAcceptingConnections();
            }

            race.setCurrentTime(ZonedDateTime.now());

            if ((race.getStatus() == RaceStatus.PRESTART) && ZonedDateTime.now().isAfter(warningTime)) {
                race.setStatus(RaceStatus.WARNING);

            } else if ((race.getStatus() == RaceStatus.WARNING) && ZonedDateTime.now().isAfter(prepTime)) {
                generateXMLs();
                sendRaceXml();
                sendBoatsXml();
                switchToPrep();
            } else {
                switchToStarted();
            }

            runRace(timeCurr, timeLast);
            updateClients(timeCurr);

            // Sleep
            try {
                Thread.sleep(1000 / LOOP_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!race.isFinished() && open);

        // Sends final message
        race.setStatus(RaceStatus.FINISHED);
        ScheduledMessageGenerator raceMessageGenerator = new RaceMessageGenerator(race);
        server.broadcast(raceMessageGenerator.getMessage());
    }


    /**
     * Switch into the preparatory stage.
     * At this time, also stop accepting new connections and set up a BoatMessageGenerator for each boat.
     */
    private void switchToPrep() {
        race.setStatus(RaceStatus.PREPARATORY);
        server.stopAcceptingConnections();

        for (Boat b : race.getStartingList()) {
            scheduledMessages.add(new BoatMessageGenerator(b));
        }
    }


    /**
     * If at the necessary time, switch the RaceStatus to STARTED.
     */
    private void switchToStarted() {
        if ((race.getStatus() == RaceStatus.PREPARATORY) && ZonedDateTime.now().isAfter(race.getStartTime())) {
            race.setStatus(RaceStatus.STARTED);
            race.getStartingList().stream()
                    .filter(boat -> boat.getStatus().equals(BoatStatus.PRE_START))
                    .forEach(boat -> boat.setStatus(BoatStatus.RACING));

        }
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

        for (PickUp pickUp : race.getPickUps()) {
            server.broadcast(new PowerUpMessageGenerator(pickUp).getMessage());
        }

        for (PowerUpEvent event : race.popPowerUpEvents()) {
            server.broadcast((new PowerTakenGenerator(event.getBoatId(), event.getPowerId(), event.getPowerDuration()).getMessage()));
        }

        for (Projectile projectile : race.popNewProjectileIds()) {
            server.broadcast((new ProjectileCreationMessageGenerator(projectile.getId()).getMessage()));
            ScheduledMessageGenerator newProMessageGen = new ProjectileMessageGenerator(AC35MessageType.PROJECTILE_LOCATION.getCode(), projectile);
            scheduledMessages.add(newProMessageGen);
        }

        for (Projectile projectile : race.popRemovedProjectiles()) {
            for(Iterator<ScheduledMessageGenerator> it = scheduledMessages.iterator(); it.hasNext();) {
                ScheduledMessageGenerator sched = it.next();
                if(sched instanceof ProjectileMessageGenerator){
                    ProjectileMessageGenerator projectleMessageGenerator = (ProjectileMessageGenerator) sched;
                    if(projectleMessageGenerator.getProjectileId() == projectile.getId()){
                        server.broadcast((new ProjectileGoneGenerator(projectile.getId()).getMessage()));
                        it.remove();
                    }
                }
            }
        }


    }


    public void setSendRaceXML(boolean send) {
        shouldSendXML = send;
    }
}