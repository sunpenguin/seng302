package seng302.team18.visualiser.interpret.unique;

import javafx.scene.paint.Color;
import seng302.team18.encode.Sender;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;

import java.io.IOException;

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
                } catch (IOException e) {}
            }
        }
    }


}
