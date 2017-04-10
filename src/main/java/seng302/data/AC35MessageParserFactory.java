package seng302.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35MessageParserFactory implements MessageParserFactory {

    @Override
    public MessageHeadParser makeHeadParser() {
        return new AC35MessageHeadParser();
    }

    @Override
    public MessageBodyParser makeBodyParser(int type) { // TODO change to map
        AC35MessageType ac35Type = AC35MessageType.from(type);
        switch (ac35Type) {
            case YACHT_EVENT:
                return new AC35YachtEventParser();
            case BOAT_LOCATION:
                return new AC35BoatLocationParser();
            case XML_MESSAGE:
                return new AC35XMLMessageParser(new AC35XMLParserFactory());
            default:
                return null;
        }
    }

    @Override
    public MessageErrorDetector makeDetector() {
        return new CRCChecker(); // TODO JESS
    }
}
