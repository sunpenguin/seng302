package seng302.team18.visualiser.send;

import seng302.team18.message.AC35MessageType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by David-chan on 2/07/17.
 */
public class ControllerMessageFactory implements MessageEncoderFactory {

    private final Map<Integer, MessageEncoder> composerMap = initialiseMap();

    @Override
    public MessageEncoder getComposer(int id) {
        return composerMap.get(id);
    }

    /**
     * Tells the parser generator which type corresponds to which message parser
     *
     * @return A message parser corresponding to the given type
     */
    private Map<Integer, MessageEncoder> initialiseMap() {
        Map<Integer, MessageEncoder> composerMap = new HashMap<>();
        composerMap.put(AC35MessageType.BOAT_ACTION.getCode(), new BoatActionMessageEncoder());

        return Collections.unmodifiableMap(composerMap);
    }
}
