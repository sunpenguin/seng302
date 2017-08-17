package seng302.team18.visualiser.messageinterpreting;

import javafx.scene.paint.Color;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.send.Sender;

import java.io.IOException;
import java.util.List;

/**
 * Created by dhl25 on 16/08/17.
 */
public class ColourResponder extends MessageInterpreter {


    private Color color;
    private Sender sender;


    public ColourResponder(Color color, Sender sender) {
        this.color = color;
        this.sender = sender;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AcceptanceMessage) {
            AcceptanceMessage acceptanceMessage = (AcceptanceMessage) message;
            if (!acceptanceMessage.getRequestType().isError()) {
                try {
                    sender.send(new ColourMessage(color, acceptanceMessage.getSourceId()));
                } catch (IOException e) {
                }
            }
        }
    }


}
