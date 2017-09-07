package seng302.team18.visualiser.interpret;

import javafx.application.Platform;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.RaceStatus;
import seng302.team18.visualiser.controller.PreRaceController;

import java.io.IOException;
import java.util.List;

/**
 * Class for swapping views based on AC35RaceStatusMessages.
 */
public class PreRaceToMainRaceInterpreter extends MessageInterpreter {


    private PreRaceController controller;

    /**
     * Constructor for PreRaceToMainRaceInterpreter.
     * @param controller used for swapping views
     */
    public PreRaceToMainRaceInterpreter(PreRaceController controller) {
        this.controller = controller;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            List<Integer> raceCodes = RaceStatus.nonPreRaceCodes();
            int statusCode = statusMessage.getRaceStatus();
            if (raceCodes.contains(statusCode)) {
                Platform.runLater(() -> {
                    try {
                        controller.showRace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
