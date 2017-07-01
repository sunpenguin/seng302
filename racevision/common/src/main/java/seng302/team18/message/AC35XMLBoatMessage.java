package seng302.team18.message;

import seng302.team18.model.Boat;

import java.util.List;

/**
 * MessageBody that contains the boat information from the XML file sent by the AC35 streaming protocol.
 */
public class AC35XMLBoatMessage implements MessageBody, XmlMessage {

    private List<Boat> boats;

    /**
     * Constructor for AC35XMLBoatMessage.
     *
     * @param boats the boats participating in the race.
     */
    public AC35XMLBoatMessage(List<Boat> boats) {
        this.boats = boats;
    }

    @Override
    public int getType() {
        return AC35MessageType.XML_BOATS.getCode();
    }

    public List<Boat> getBoats() {
        return boats;
    }

}
