package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.visualiser.ClientRace;

/**
 * A MessageInterpreter that takes a AC35XMLBoatMessage and updates the boats of a Race.
 */
public class XMLBoatInterpreter extends MessageInterpreter {

    private ClientRace race;


    /**
     * Constructor for XMLBoatInterpreter. Takes a Race as a parameter which it updates every time a
     * AC35XMLBoatMessage is interpreted.
     *
     * @param race to be updated.
     */
    public XMLBoatInterpreter(ClientRace race) {
        this.race = race;
    }


    /**
     * Interprets the message of the XMLBoatInterpreter
     *
     * @param message to be interpreted
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35XMLBoatMessage) {
            AC35XMLBoatMessage boatMessage = (AC35XMLBoatMessage) message;
            race.setStartingList(boatMessage.getYachts());
        }
    }
}
