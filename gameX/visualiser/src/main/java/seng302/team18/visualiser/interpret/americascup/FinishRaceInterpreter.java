package seng302.team18.visualiser.interpret.americascup;

import javafx.application.Platform;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.RaceStatus;
import seng302.team18.visualiser.controller.RaceController;


/**
 * Interpreter to check if a race has been completed.
 */
public class FinishRaceInterpreter extends MessageInterpreter {

    private RaceController controller;

    /**
     * Constructor for a FinishRaceInterpreter.
     *
     * @param controller the RaceController
     */
    public FinishRaceInterpreter(RaceController controller) {
        this.controller = controller;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            int statusCode = statusMessage.getRaceStatus();
            if (statusCode == RaceStatus.FINISHED.getCode()) {
                Platform.runLater(() -> controller.showFinishersList());
            }
        }
    }
}
