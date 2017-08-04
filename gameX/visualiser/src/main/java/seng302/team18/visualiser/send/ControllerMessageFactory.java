package seng302.team18.visualiser.send;

import seng302.team18.message.AC35MessageType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Message encoder factory for the Controller protocol.
 */
public class ControllerMessageFactory implements MessageEncoderFactory {


    private final Map<Integer, MessageEncoder> composerMap = initialiseMap();


    /**
     * Returns the encoder of the message
     *
     * @param id of the message to be composed
     * @return The encoder for the message
     */
    @Override
    public MessageEncoder getEncoder(int id) {
        return composerMap.get(id);
    }


    /**
     * Tells the parser generator which type corresponds to which message parser
     *
     * @return A message parser corresponding to the given type
     */
    private Map<Integer, MessageEncoder> initialiseMap() {
        Map<Integer, MessageEncoder> composerMap = new HashMap<>();
        composerMap.put(AC35MessageType.BOAT_ACTION.getCode(), new BoatActionEncoder());
        composerMap.put(AC35MessageType.REQUEST.getCode(), new RequestEncoder());

        return Collections.unmodifiableMap(composerMap);
    }
}
