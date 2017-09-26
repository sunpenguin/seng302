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
                addDownwindMessage(actions);
            } else {
                addDownwindMessage(actions);
                addUpwindMessage(actions);
                addDownwindMessage(actions);
            }
        } else if (Math.abs(flippedHeading) < 0.01) {
            if (fromLeft) {
                addUpwindMessage(actions);
                addDownwindMessage(actions);
                addUpwindMessage(actions);
            } else {
                addUpwindMessage(actions);
            }
        } else if (heading > 0 && heading < 180) {
            addDownwindMessage(actions);
            fromLeft = false;
        } else {
            addUpwindMessage(actions);
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
                addDownwindMessage(actions);
                addUpwindMessage(actions);
                addDownwindMessage(actions);
            } else {
                addDownwindMessage(actions);
            }
        } else if (Math.abs(flippedHeading) < 0.01) {
            if (fromLeft) {
                addUpwindMessage(actions);
            } else {
               addUpwindMessage(actions);
               addDownwindMessage(actions);
               addUpwindMessage(actions);
            }
        } else if (heading > 0 && heading < 180) {
            addUpwindMessage(actions);
            fromLeft = false;
        } else {
            addDownwindMessage(actions);
            fromLeft = true;
        }

        return actions;
    }


    public List<MessageBody> generateUpwind(double windDirection, double boatHeading) {
        double heading = (boatHeading - windDirection + 360) % 360;
        fromLeft = !(heading > 180 && heading < 360);
        List<MessageBody> actions = new ArrayList<>();
        addUpwindMessage(actions);
        return actions;
    }


    public List<MessageBody> generateDownwind(double windDirection, double boatHeading) {
        double heading = (boatHeading - windDirection + 360) % 360;
        fromLeft = !(heading > 0 && heading < 180);
        List<MessageBody> actions = new ArrayList<>();
        addDownwindMessage(actions);
        return actions;
    }


    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


    private void addUpwindMessage(List<MessageBody> actions) {
        BoatActionMessage actionMessage = new BoatActionMessage(playerId);
        actionMessage.setUpwind();
        actions.add(actionMessage);
    }

    private void addDownwindMessage(List<MessageBody> actions) {
        BoatActionMessage actionMessage = new BoatActionMessage(playerId);
        actionMessage.setDownwind();
        actions.add(actionMessage);
    }

}
