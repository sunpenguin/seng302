package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.MessageBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CompositeMessageInterpreter that can add and remove other MessageInterpreter.
 *
 * @see MessageInterpreter
 */
public class CompositeMessageInterpreter extends MessageInterpreter {
    private Map<Integer, List<MessageInterpreter>> interpreterMap;

    /**
     * The constructor for CompositeMessageInterpreter.
     */
    public CompositeMessageInterpreter() {
        interpreterMap = new HashMap<>();
    }

    /**
     * Interprets all messages that are stored in the map using their respective interpret methods.
     *
     * @param message to be interpreted.
     */
    @Override
    public void interpret(MessageBody message) {
        if (message == null) {
            return;
        }
        List<MessageInterpreter> interpreters = interpreterMap.get(message.getType());
        if (interpreters != null) {
            for (MessageInterpreter interpreter : interpreters) {
                interpreter.interpret(message);
            }
        }
    }

    /**
     * Adds an interpreter to the type specified by the AC35MessageType.
     *
     * @param type of message the MessageInterpreter (parameter) will listen to.
     * @param interpreter the MessageInterpreter to be added with the type of message.
     */
    @Override
    public void add(int type, MessageInterpreter interpreter) {
        List<MessageInterpreter> interpreters = interpreterMap.computeIfAbsent(type, k -> new ArrayList<>());
        interpreters.add(interpreter);
    }

    /**
     * Removes an interpreter from the type specified by the AC35MessageType.
     *
     * @param type of message the MessageInterpreter (parameter) was listening to.
     * @param interpreter the MessageInterpreter to be removed from the type of message.
     */
    @Override
    public void remove(int type, MessageInterpreter interpreter) {
        List<MessageInterpreter> interpreters = interpreterMap.get(type);
        if (interpreters != null) {
            if (interpreters.contains(interpreter)) {
                interpreters.remove(interpreter);
            }
        }
    }
}
