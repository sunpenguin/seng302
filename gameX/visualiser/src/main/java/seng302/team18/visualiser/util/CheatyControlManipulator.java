package seng302.team18.visualiser.util;

import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that generate upwind and downwind massages based on stuff
 */
public class CheatyControlManipulator {

    private int playerId;
    private boolean fromLeft = false;

    public CheatyControlManipulator() {}


    /**
     * Generates the sequence of messages required to turn clockwise.
     * 
     * @param windDirection direction of the wind.
     * @param boatHeading heading of the boat.
     * @return list of messages to send which will make the boat turn clock wise
     */
    public List<MessageBody> generateClockwise(double windDirection, double boatHeading) {
        double heading = (boatHeading - windDirection + 360) % 360;
        double flippedHeading = ((boatHeading - windDirection + 360 + 180) % 360);
        List<MessageBody> actions = new ArrayList<>();

        if (Math.abs(heading) < 0.01) {
            if (fromLeft) {
                actions.add(makeDownwindMessage());
            } else {
                actions.add(makeDownwindMessage());
                actions.add(makeUpwindMessage());
                actions.add(makeDownwindMessage());
            }
        } else if (Math.abs(flippedHeading) < 0.01) {
            if (fromLeft) {
                actions.add(makeUpwindMessage());
                actions.add(makeDownwindMessage());
                actions.add(makeUpwindMessage());
            } else {
                actions.add(makeUpwindMessage());
            }
        } else if (heading > 0 && heading < 180) {
            actions.add(makeDownwindMessage());
            fromLeft = false;
        } else {
            actions.add(makeUpwindMessage());
            fromLeft = true;
        }

        return actions;
    }


    /**
     * Generates the sequence of messages required to turn counter clock wise.
     *
     * @param windDirection direction of the wind.
     * @param boatHeading heading of the boat.
     * @return list of messages to send which will make the boat turn counter clock wise
     */
    public List<MessageBody> generateCounterClockwise(double windDirection, double boatHeading) {
        double heading = (boatHeading - windDirection + 360) % 360;
        double flippedHeading = ((boatHeading - windDirection + 360 + 180) % 360);
        List<MessageBody> actions = new ArrayList<>();

        if (Math.abs(heading) < 0.01) {
            if (fromLeft) {
                actions.add(makeDownwindMessage());
                actions.add(makeUpwindMessage());
                actions.add(makeDownwindMessage());
            } else {
                actions.add(makeDownwindMessage());
            }
        } else if (Math.abs(flippedHeading) < 0.01) {
            if (fromLeft) {
                actions.add(makeUpwindMessage());
            } else {
               actions.add(makeUpwindMessage());
               actions.add(makeDownwindMessage());
               actions.add(makeUpwindMessage());
            }
        } else if (heading > 0 && heading < 180) {
            actions.add(makeUpwindMessage());
            fromLeft = false;
        } else {
            actions.add(makeDownwindMessage());
            fromLeft = true;
        }

        return actions;
    }


    /**
     * Generates the sequence of messages required to turn upwind.
     *
     * @param windDirection direction of the wind.
     * @param boatHeading heading of the boat.
     * @return list of messages to send which will make the boat turn up wind
     */
    public List<MessageBody> generateUpwind(double windDirection, double boatHeading) {
        double heading = (boatHeading - windDirection + 360) % 360;
        fromLeft = !(heading > 180 && heading < 360);
        List<MessageBody> actions = new ArrayList<>();
        actions.add(makeUpwindMessage());
        return actions;
    }


    /**
     * Generates the sequence of messages required to turn downwind.
     *
     * @param windDirection direction of the wind.
     * @param boatHeading heading of the boat.
     * @return list of messages to send which will make the boat turn downwind
     */
    public List<MessageBody> generateDownwind(double windDirection, double boatHeading) {
        double heading = (boatHeading - windDirection + 360) % 360;
        fromLeft = !(heading > 0 && heading < 180);
        List<MessageBody> actions = new ArrayList<>();
        actions.add(makeDownwindMessage());
        return actions;
    }


    /**
     * Player id is used for generating messages with the correct id.
     * @param playerId
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


    /**
     * Creates an upwind message.
     * @return an upwind message.
     */
    private MessageBody makeUpwindMessage() {
        BoatActionMessage actionMessage = new BoatActionMessage(playerId);
        actionMessage.setUpwind();
        return actionMessage;
    }


    /**
     * Creates an downwind message.
     * @return an downwind message.
     */
    private MessageBody makeDownwindMessage() {
        BoatActionMessage actionMessage = new BoatActionMessage(playerId);
        actionMessage.setDownwind();
        return actionMessage;
    }

}
