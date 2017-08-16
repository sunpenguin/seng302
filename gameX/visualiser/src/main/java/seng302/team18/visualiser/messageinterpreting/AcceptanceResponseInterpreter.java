package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.send.Sender;

import java.util.List;

/**
 * Created by dhl25 on 16/08/17.
 */
public class AcceptanceResponseInterpreter extends MessageInterpreter {


    private List<MessageBody> messages;
    private Sender sender;


    public AcceptanceResponseInterpreter(List<MessageBody> messages, Sender sender) {
        this.messages = messages;
        this.sender = sender;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AcceptanceMessage) {
            AcceptanceMessage acceptanceMessage = (AcceptanceMessage) message;
            if (!acceptanceMessage.getRequestType().isError()) {
                for (MessageBody messageBody : messages) {
                    sender.send(messageBody);
                }
            }
        }
    }


}
