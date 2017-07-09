package seng302.team18.messageparsing;

import seng302.team18.message.AC35MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory method for messages sent by the AC35 streaming protocol.
 */
public class AC35MessageParserFactory implements MessageParserFactory {

    private final Map<AC35MessageType, MessageBodyParser> parserMap = initialiseMap();

    @Override
    public MessageHeadParser makeHeadParser() {
        return new AC35MessageHeadParser();
    }

    /**
     * Creates a message parser of a specific type.
     *
     * @param type An integer value corresponding ot a specific type of message parser.
     * @return A message parser of a specific type.
     */
    @Override
    public MessageBodyParser makeBodyParser(int type) {
        AC35MessageType ac35Type = AC35MessageType.from(type);
        return parserMap.get(ac35Type);
    }

    @Override
    public MessageErrorDetector makeDetector() {
        return new CRCChecker();
    }

    /**
     * Tells the parser generator which type corresponds to which message parser
     *
     * @return A message parser corresponding to the given type
     */
    private Map<AC35MessageType, MessageBodyParser> initialiseMap() {
        Map<AC35MessageType, MessageBodyParser> parserMap = new HashMap<>();
        parserMap.put(AC35MessageType.YACHT_EVENT, new AC35YachtEventParser());
        parserMap.put(AC35MessageType.BOAT_LOCATION, new AC35BoatLocationParser());
        parserMap.put(AC35MessageType.XML_MESSAGE, new AC35XMLMessageParser(new AC35XMLParserFactory()));
        parserMap.put(AC35MessageType.RACE_STATUS, new AC35RaceStatusParser());
        parserMap.put(AC35MessageType.MARK_ROUNDING, new AC35MarkRoundingParser());
        parserMap.put(AC35MessageType.ACCEPTANCE, new AcceptanceParser());
        parserMap.put(AC35MessageType.REQUEST, new RequestParser());

        return parserMap;
    }
}
