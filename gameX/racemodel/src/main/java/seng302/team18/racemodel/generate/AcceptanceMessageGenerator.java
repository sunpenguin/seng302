package seng302.team18.racemodel.generate;

import seng302.team18.message.AC35MessageType;
import seng302.team18.message.RequestType;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class to generate response messages to clients connecting to the server, informing them of their boat's ID.
 */
public class AcceptanceMessageGenerator extends MessageGenerator {

    private int sourceID;
    private RequestType status;


    /**
     * Constructs a response packet with the specified source ID indicating the boat the player controls.
     *
     *
     * @param sourceID int, ID of the boat
     */
    public AcceptanceMessageGenerator(int sourceID, RequestType status) {
        super(AC35MessageType.ACCEPTANCE.getCode());
        this.sourceID = sourceID;
        this.status = status;
    }


    @Override
    protected byte[] getPayload() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] sourceIDBytes = ByteCheck.intToByteArray(sourceID);
        byte statusByte = (byte) status.getCode();

        outStream.write(sourceIDBytes);
        outStream.write(statusByte);

        return outStream.toByteArray();
    }
}
