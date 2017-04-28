package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.MessageBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 4/13/17.
 */
public class CompositeMessageInterpreter extends MessageInterpreter {

    private Map<Integer, List<MessageInterpreter>> interpreterMap;

    public CompositeMessageInterpreter() {
        interpreterMap = new HashMap<>();
    }

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

    @Override
    public void add(int type, MessageInterpreter interpreter) {
        List<MessageInterpreter> interpreters = interpreterMap.get(type);
        if (interpreters == null) {
            interpreters =  new ArrayList<>();
            interpreterMap.put(type, interpreters);
        }
        interpreters.add(interpreter);
    }

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
