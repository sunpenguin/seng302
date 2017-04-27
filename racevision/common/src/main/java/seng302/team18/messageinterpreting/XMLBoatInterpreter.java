package seng302.team18.messageinterpreting;

import seng302.team18.data.AC35XMLBoatMessage;
import seng302.team18.data.MessageBody;
import seng302.team18.model.Race;

/**
 * Created by dhl25 on 27/04/17.
 */
public class XMLBoatInterpreter extends MessageInterpreter {

    private Race race;

    public XMLBoatInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35XMLBoatMessage) {
            AC35XMLBoatMessage boatMessage = (AC35XMLBoatMessage) message;
            race.setStartingList(boatMessage.getBoats());
        }
    }
}
