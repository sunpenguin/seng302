package seng302.team18.data;

/**
 * Created by dhl25 on 11/04/17.
 */
public class AC35XMLBoatMessage implements MessageBody {

    @Override
    public AC35MessageType getType() {
        return AC35MessageType.XML_BOATS;
    }

}
