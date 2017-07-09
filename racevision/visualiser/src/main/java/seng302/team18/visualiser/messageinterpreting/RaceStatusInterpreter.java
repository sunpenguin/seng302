package seng302.team18.visualiser.messageinterpreting;

import javafx.application.Platform;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.RaceStatus;
import seng302.team18.visualiser.controller.PreRaceController;

import java.io.IOException;
import java.util.List;

/**
 * Class for swapping views based on AC35RaceStatusMessages.
 */
public class RaceStatusInterpreter extends MessageInterpreter {


    private PreRaceController controller;

    /**
     * Constructor for RaceStatusInterpreter.
     * @param controller used for swapping views
     */
    public RaceStatusInterpreter(PreRaceController controller) {
        this.controller = controller;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            List<Integer> raceCodes = RaceStatus.nonPreRaceCodes();
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            int statusCode = statusMessage.getRaceStatus();
            if (raceCodes.contains(statusCode)) {
                Platform.runLater(() -> {
                    try {
                        controller.showRace();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                });
            }
        }
    }
}
