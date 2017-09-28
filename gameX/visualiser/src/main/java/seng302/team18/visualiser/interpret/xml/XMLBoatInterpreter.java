package seng302.team18.visualiser.interpret.xml;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.ClientRace;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            List<Boat> newBoats = updatedBoats(race.getStartingList(), boatMessage.getYachts());
            race.setStartingList(newBoats);
        }
    }


    /**
     * Sets old boat info to be consistent with new boats, (references stay the same)
     *
     * @param oldBoats old
     * @param newBoats new
     * @return updated
     */
    private List<Boat> updatedBoats(List<Boat> oldBoats, List<Boat> newBoats) {
        List<Integer> oldIds = oldBoats
                .stream()
                .map(Boat::getId)
                .collect(Collectors.toList());
        List<Boat> updatedBoats = new ArrayList<>();
        for (Boat boat : newBoats) {
            if (oldIds.contains(boat.getId())) {
                Boat oldBoat = getBoat(boat.getId(), oldBoats);
                updateInfo(oldBoat, boat);
                updatedBoats.add(oldBoat);
            } else {
                updatedBoats.add(boat);
            }
        }
        return updatedBoats;
    }


    /**
     * Gets the boat with the particular id from the list
     *
     * @param id    of desired boat
     * @param boats to look from
     * @return the boat if it exists, null otherwise.
     */
    private Boat getBoat(int id, List<Boat> boats) {
        for (Boat boat : boats) {
            if (boat.getId().equals(id)) {
                return boat;
            }
        }
        return null;
    }


    /**
     * updates an old boats info to be consistent with the new boat
     *
     * @param oldBoat no sir
     * @param newBoat yes sir
     */
    private void updateInfo(Boat oldBoat, Boat newBoat) {
        oldBoat.setSpeed(newBoat.getSpeed());
        oldBoat.setHeading(newBoat.getHeading());
        oldBoat.setControlled(newBoat.isControlled());
        oldBoat.setLives(newBoat.getLives());
        oldBoat.setHasCollided(newBoat.getHasCollided());
        oldBoat.setColour(newBoat.getColour());
    }

}
