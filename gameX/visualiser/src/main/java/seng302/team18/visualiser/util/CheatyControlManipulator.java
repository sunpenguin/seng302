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


    public List<MessageBody> generateUpwind(double windDirection, double boatHeading) {
        double heading = (boatHeading - windDirection + 360) % 360;
        fromLeft = !(heading > 180 && heading < 360);
        List<MessageBody> actions = new ArrayList<>();
        actions.add(makeUpwindMessage());
        return actions;
    }


    public List<MessageBody> generateDownwind(double windDirection, double boatHeading) {
        double heading = (boatHeading - windDirection + 360) % 360;
        fromLeft = !(heading > 0 && heading < 180);
        List<MessageBody> actions = new ArrayList<>();
        actions.add(makeDownwindMessage());
        return actions;
    }


    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


    private MessageBody makeUpwindMessage() {
        BoatActionMessage actionMessage = new BoatActionMessage(playerId);
        actionMessage.setUpwind();
        return actionMessage;
    }

    private MessageBody makeDownwindMessage() {
        BoatActionMessage actionMessage = new BoatActionMessage(playerId);
        actionMessage.setDownwind();
        return actionMessage;
    }

}
