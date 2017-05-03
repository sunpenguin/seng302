package seng302.team18.test_mock.connection;

import seng302.team18.messageparsing.AC35MessageType;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.MarkRoundingEvent;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by afj19 on 28/04/17.
 */
public class MarkRoundingMessageGenerator extends MessageGenerator {
    private final static int VERSION_NUMBER = 0x01;
    private final short ACK_NUMBER = 0x00;

    private final MarkRoundingEvent markRoundingEvent;
    private final int raceId;

    public MarkRoundingMessageGenerator(MarkRoundingEvent event, int raceId) {
        super(AC35MessageType.MARK_ROUNDING.getCode());

        markRoundingEvent = event;
        this.raceId = raceId;
    }

    @Override
    public byte[] getPayload() throws IOException {
        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        // Version number
        outputSteam.write(ByteCheck.intToByteArray(VERSION_NUMBER));
        // Time
        outputSteam.write(ByteCheck.convertLongTo6ByteArray(markRoundingEvent.getTime()));
        //Ack number
        outputSteam.write(ByteCheck.shortToByteArray(ACK_NUMBER));
        // Race id
        outputSteam.write(raceId);
        // Boat(source) id
        outputSteam.write(markRoundingEvent.getBoat().getId());
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
