package seng302.team18.messageparsing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 4/10/17.
 */
public class AC35XMLParserFactory implements MessageParserFactory {

    private final Map<AC35MessageType, MessageBodyParser> parserMap = initialiseMap();

    @Override
    public MessageHeadParser makeHeadParser() {
        return new AC35XMLHeadParser();
    }

    @Override
    public MessageBodyParser makeBodyParser(int type) {
        AC35MessageType xmlType = AC35MessageType.from(type);
        return parserMap.get(xmlType);
    }

//    @Override
//    public MessageBodyParser makeBodyParser(int type) {
//        AC35MessageType xmlType = AC35MessageType.from(type);
//        switch (xmlType) {
//            case XML_REGATTA:
//                return new AC35XMLRegattaParser();
//            case XML_RACE:
//                return new AC35XMLRaceParser();
//            case XML_BOATS:
//                return new AC35XMLBoatParser();
//            default:
//                return null;
//        }
//    }


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

    private Map<AC35MessageType, MessageBodyParser> initialiseMap() {
        Map<AC35MessageType, MessageBodyParser> parserMap = new HashMap<>();
        parserMap.put(AC35MessageType.XML_REGATTA, new AC35XMLRegattaParser());
        parserMap.put(AC35MessageType.XML_RACE, new AC35XMLRaceParser());
        parserMap.put(AC35MessageType.XML_BOATS, new AC35XMLBoatParser());
        return parserMap;
    }



}
