package seng302.team18.visualiser.send;

import seng302.team18.message.AC35MessageType;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by David-chan on 2/07/17.
 */
public class ControllerMessageFactory implements MessageComposerFactory {


    private final Map<Integer, MessageComposer> composerMap = initialiseMap();

    @Override
    public MessageComposer getComposer(int id) {
        return composerMap.get(id);
    }


    /**
     * Tells the parser generator which type corresponds to which message parser
     *
     * @return A message parser corresponding to the given type
     */
    private Map<Integer, MessageComposer> initialiseMap() {
        Map<Integer, MessageComposer> composerMap = new HashMap<>();
        composerMap.put(AC35MessageType.BOAT_ACTION.getCode(), new BoatActionMessageComposer());

        return Collections.unmodifiableMap(composerMap);
    }

}
