package seng302.team18.data;

import seng302.team18.model.Race;

/**
 * Created by david on 4/13/17.
 */
public class RaceMessageInterpreter {

    public RaceMessageInterpreter(Race race) {

    }

    public void interpretMessage(MessageBody message) {
        switch (message.getType()) {
            case XML_RACE:
                break;
            case XML_BOATS:
                break;
            case XML_REGATTA:
                break;
            case BOAT_LOCATION:
                break;
            case YACHT_EVENT:
                break;
        }
    }

}
