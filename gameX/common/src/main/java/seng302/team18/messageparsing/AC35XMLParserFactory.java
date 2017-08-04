package seng302.team18.messageparsing;

import seng302.team18.message.AC35MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory which reads mxl message and parser types and creates parsers of the type corresponding to the stream
 * information.
 */
public class AC35XMLParserFactory implements MessageParserFactory {

    private final Map<AC35MessageType, MessageBodyParser> parserMap = initialiseMap();

    @Override
    public MessageHeadParser makeHeadParser() {
        return new AC35XMLHeadParser();
    }

    /**
     * Create a message body parser of the given integer type.
     *
     * @param type An integer representation of the required type.
     * @return A message body parser matching the given type.
     */
    @Override
    public MessageBodyParser makeBodyParser(int type) {
        AC35MessageType xmlType = AC35MessageType.from(type);
        return parserMap.get(xmlType);
    }

    /**
     * Overriddes the current error detector methods and returns a new error detector.
     *
     * @return An error detector.
     */
    @Override
    public MessageErrorDetector makeDetector() {
        return new MessageErrorDetector() {
            @Override
            public int errorCheckSize() {
                return 0;
            }

            @Override
            public Boolean isValid(byte[] checkSum, byte[] messageBytes, byte[] headerBytes) {
                return true;
            }
        };
    }

    /**
     * Maps each XML message type to its parser.
     *
     * @return A message body type and body parser map.
     */
    private Map<AC35MessageType, MessageBodyParser> initialiseMap() {
        Map<AC35MessageType, MessageBodyParser> parserMap = new HashMap<>();
        parserMap.put(AC35MessageType.XML_REGATTA, new AC35XMLRegattaParser());
        parserMap.put(AC35MessageType.XML_RACE, new AC35XMLRaceParser());
        parserMap.put(AC35MessageType.XML_BOATS, new AC35XMLBoatParser());
        return parserMap;
    }


}
