package seng302.team18.racemodel.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;

import java.util.List;

/**
 * Class used to interpret a ColourMessage.
 */
public class ColourInterpreter extends MessageInterpreter {

    private List<Boat> boats;

    public ColourInterpreter(List<Boat> boats) {
        this.boats = boats;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof ColourMessage) {
            ColourMessage colourMessage = (ColourMessage) message;
            Boat boat = getBoatById(colourMessage.getSourceID());
            while (boat == null) {
                boat = getBoatById(colourMessage.getSourceID());
            }
            boat.setColour(colourMessage.getColour());
        }
    }

    private Boat getBoatById(int id) {
        for (Boat boat : boats) {
            if (boat.getId().equals(id)) {
                return boat;
            }
        }
        return null;
    }
}
