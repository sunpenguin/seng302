package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.visualiser.controller.PreRaceController;

/**
 * The BoatListInterpreter that updates the boat list in PreRaceController.
 */
public class BoatListInterpreter extends MessageInterpreter {

    private PreRaceController controller;

    /**
     * Constructor for BoatListInterpreter
     *
     * @param controller to update when new AC35XMLBoatMessage are received
     */
    public BoatListInterpreter(PreRaceController controller) {
        this.controller = controller;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35XMLBoatMessage) {
            controller.updateBoatList();
        }
    }

}
