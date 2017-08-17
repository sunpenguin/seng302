package seng302.team18.racemodel.interpret;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;

import java.util.List;

/**
 * Created by dhl25 on 17/08/17.
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
            if (boat != null) {
                boat.setColour(colourMessage.getColour());
            }
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
