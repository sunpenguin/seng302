package seng302.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35MessageParserFactory implements MessageParserFactory {

    @Override
    public MessageHeadParser makeHeadParser() {
        return new AC35MessageHeadParser();
    }

    @Override
    public MessageBodyParser makeBodyParser(MessageType type) {
        switch (type) {
            case YACHT_EVENT:
                return new AC35YachtEventParser();
            case BOAT_LOCATION:
                return new AC35BoatLocationParser();
            default:
                return null;
        }
    }

    @Override
    public MessageErrorDetector makeDetector() {
        return new CRCChecker(); // TODO JESS
    }
}
