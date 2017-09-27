package seng302.team18.visualiser.interpret.unique;

import javafx.application.Platform;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestType;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.controller.GameConnection;

import java.io.IOException;

/**
 * The MarkLocationInterpreter that sets the client's player id
 *
 * @see MessageInterpreter
 */
public class AcceptanceInterpreter extends MessageInterpreter {


    private ClientRace race;
    private GameConnection gameConnection;


    public AcceptanceInterpreter(ClientRace race, GameConnection gameConnection) {
        this.race = race;
        this.gameConnection = gameConnection;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AcceptanceMessage) {
            int sourceId = ((AcceptanceMessage) message).getSourceId();
            RequestType requestType =  ((AcceptanceMessage) message).getRequestType();
            if (requestType.getCode() != race.getMode().getCode()) {
                Platform.runLater(() -> {
                    gameConnection.setFailedConnection();
                });
            } else {
                Platform.runLater(() -> {
                    try {
                        gameConnection.goToPreRace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
            race.setPlayerId(sourceId);
        }
    }
}