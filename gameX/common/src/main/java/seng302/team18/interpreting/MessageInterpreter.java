package seng302.team18.interpreting;

import seng302.team18.message.MessageBody;

import java.util.Observable;

/**
 * Abstract class for interpreting MessageBody.
 */
public abstract class MessageInterpreter extends Observable {


    /**
     * Abstract method for interpreting MessageBody.
     * @param message to be interpreted
     */
    public abstract void interpret(MessageBody message);


    /**
     * Adds a MessageInterpreter to the MessageInterpreter unless it is not a composite MessageInterpreter.
     * @param type of message the MessageInterpreter (parameter) will listen to.
     * @param interpreter the MessageInterpreter to be added with the type of message.
     */
    public void add(int type, MessageInterpreter interpreter) {}


    /**
     * Removes a MessageInterpreter from the MessageInterpreter unless it is not a composite MessageInterpreter.
     * @param type of message the MessageInterpreter (parameter) was listening to.
     * @param interpreter the MessageInterpreter to be removed from the type of message.
     */
    public void remove(int type, MessageInterpreter interpreter) {}

}
