package seng302.team18.visualiser.messageinterpreting;

import javafx.application.Platform;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RaceStatus;
import seng302.team18.model.Race;
import seng302.team18.visualiser.controller.ControllerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dhl25 on 10/05/17.
 */
public class RaceStatusInterpreter extends MessageInterpreter {


    private ControllerManager controllerManager;

    public RaceStatusInterpreter(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            List<Integer> preRaceCodes = RaceStatus.preRaceCodes();
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            int statusCode = statusMessage.getRaceStatus();
            if (preRaceCodes.contains(statusCode)) {
                Platform.runLater(() -> {
                    try {
                        controllerManager.showPreRace();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                });
            } else {
                Platform.runLater(() -> {
                    try {
                        controllerManager.showMainView();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                });
            }
        }
    }
}
