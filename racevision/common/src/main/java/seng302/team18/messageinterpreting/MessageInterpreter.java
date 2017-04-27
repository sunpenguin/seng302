package seng302.team18.messageinterpreting;

import seng302.team18.data.MessageBody;

/**
 * Created by dhl25 on 17/04/17.
 */
public abstract class MessageInterpreter {

    public abstract void interpret(MessageBody message);

    public void add(int type, MessageInterpreter interpreter) {}

    public void remove(int type, MessageInterpreter interpreter) {}

}
