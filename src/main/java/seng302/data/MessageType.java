package seng302.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public enum MessageType {
    XML_MESSAGE (26), YACHT_EVENT (29), BOAT_LOCATION (37);

    MessageType(int code) {

    }

    public static MessageType from(int code) {
        switch (code) {
            case 26:
                return XML_MESSAGE;
            case 29:
                return YACHT_EVENT;
            case 37:
                return BOAT_LOCATION;
            default:
                return null;
        }
    }

}
