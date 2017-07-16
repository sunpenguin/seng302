package seng302.team18.test_mock.connection;

import seng302.team18.message.AC35MessageType;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.MarkRoundingEvent;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Generates a mark rounding message a mark rounding event
 * Each instance of this class will only generate messages for the event
 * specified during construction.
 *
 * @see seng302.team18.model.MarkRoundingEvent
 */
public class MarkRoundingMessageGenerator extends MessageGenerator {
    private final static int VERSION_NUMBER = 0x01;
    private final short ACK_NUMBER = 0x00;

    private final MarkRoundingEvent markRoundingEvent;
    private final int raceId;

    /**
     * Constructs a MarkRoundingMessageGenerator for the specified
     * MarkRoundingEvent and race ID
     *
     * @param event  the mark rounding event
     * @param raceId the id of the race the event occurred in
     */
    public MarkRoundingMessageGenerator(MarkRoundingEvent event, int raceId) {
        super(AC35MessageType.MARK_ROUNDING.getCode());

        markRoundingEvent = event;
        this.raceId = raceId;
    }

    @Override
    public byte[] getPayload() throws IOException {
        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        // Version number
        outputSteam.write(0xFF & VERSION_NUMBER);

        // Time
        outputSteam.write(ByteCheck.longTo6ByteArray(markRoundingEvent.getTime()));

        //Ack number
        outputSteam.write(ByteCheck.shortToByteArray(ACK_NUMBER));

        // Race id
        outputSteam.write(ByteCheck.intToByteArray(raceId));

        // Boat(source) id
        outputSteam.write(ByteCheck.intToByteArray(markRoundingEvent.getBoat().getId()));

        // Boat status - defaulting to 0 for 'unknown'
        outputSteam.write(0x00);

        // Rounding side - defaulting to 0 for 'unknown'
        outputSteam.write(0x00);

        // Mark type
        byte markType = 0x00;
        switch (markRoundingEvent.getCompoundMark().getMarks().size()) {
            case CompoundMark.MARK_SIZE:
                markType = 0x01;
                break;
            case CompoundMark.GATE_SIZE:
                markType = 0x02;
                break;
        }
        outputSteam.write(markType);

        // Mark id
        outputSteam.write(markRoundingEvent.getCompoundMark().getId());

        return outputSteam.toByteArray();
    }
}
