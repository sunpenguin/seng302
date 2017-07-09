package seng302.team18.message;

import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Boat;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MessageBody that contains the boat information from the XML file sent by the AC35 streaming protocol.
 */
public class AC35XMLBoatMessage implements XmlMessage {

    private List<AbstractBoat> boats;

    /**
     * Constructor for AC35XMLBoatMessage.
     *
     * @param boats the boats participating in the race.
     */
    public AC35XMLBoatMessage(List<AbstractBoat> boats) {
        this.boats = boats;
    }

    @Override
    public int getType() {
        return AC35MessageType.XML_BOATS.getCode();
    }

    public List<AbstractBoat> getBoats() {
        return boats;
    }

    public List<Boat> getYachts() {
        return boats.stream()
                .filter(abstractBoat -> abstractBoat instanceof Boat)
                .map(abstractBoat -> (Boat) abstractBoat)
                .collect(Collectors.toList());
    }

}
