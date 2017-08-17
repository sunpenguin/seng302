package seng302.team18.racemodel.connection;

import seng302.team18.message.AC35MessageType;
import seng302.team18.model.YachtEvent;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Generates a yacht event message from a yacht event
 * Each instance of this class will only generate messages for the event
 * specified during construction.
 *
 * @see seng302.team18.model.YachtEvent
 */
public class YachtEventCodeMessageGenerator extends MessageGenerator {
    private final static int VERSION_NUMBER = 0x01;
    private final short ACK_NUMBER = 0x00;

    private final YachtEvent event;
    private final int raceId;


    /**
     * Constructs a YachtEventCodeMessageGenerator for the specified
     * YachtEvent and race ID
     *
     * @param event  the yacht event
     * @param raceId the id of the race the event occurred in
     */
    public YachtEventCodeMessageGenerator(YachtEvent event, int raceId) {
        super(AC35MessageType.YACHT_EVENT.getCode());

        this.event = event;
        this.raceId = raceId;
    }


    @Override
    protected byte[] getPayload() throws IOException {
        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        // Version number
        outputSteam.write(0xFF & VERSION_NUMBER);

        // Time
        outputSteam.write(ByteCheck.longTo6ByteArray(event.getTime()));

        //Ack number
        outputSteam.write(ByteCheck.shortToByteArray(ACK_NUMBER));

        // Race id
        outputSteam.write(ByteCheck.intToByteArray(raceId));

        // Boat(source) id
        outputSteam.write(ByteCheck.intToByteArray(event.getBoatId()));

        // Incident ID
        outputSteam.write(ByteCheck.intToByteArray(0));

        // Event ID
        outputSteam.write(event.getEventCode().getCode());

        return outputSteam.toByteArray();
    }
}
