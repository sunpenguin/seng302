package seng302.team18.test_mock.connection;

import seng302.team18.message.AC35MessageType;
import seng302.team18.util.ByteCheck;

import java.io.IOException;

/**
 * Class to generate response messages to clients connecting to the server, informing them of their boat's ID.
 */
public class ResponseMessageGenerator extends MessageGenerator {

    private int sourceID;


    /**
     * Constructs a response packet with the specified source ID indicating the boat the player controls.
     *
     * @param sourceID int, ID of the boat
     */
    public ResponseMessageGenerator(int sourceID) {
        super(AC35MessageType.ACCEPTANCE.getCode());
        this.sourceID = sourceID;
    }

    @Override
    protected byte[] getPayload() throws IOException {
        return ByteCheck.intToByteArray(sourceID);
    }
}
