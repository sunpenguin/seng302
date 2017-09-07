package seng302.team18.racemodel.message_generating;

import org.junit.Test;
import seng302.team18.message.AC35MessageType;
import seng302.team18.model.*;
import seng302.team18.racemodel.message_generating.MarkRoundingMessageGenerator;
import seng302.team18.util.ByteCheck;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by Anton J on 4/05/2017.
 */
public class MarkRoundingMessageGeneratorTest {
    private byte[] message;
    private int index = 0;

    private static final int RACE_ID = 123456;

    @Test
    public void getPayload() throws Exception {
        final Boat boat = new Boat("testBoat", "tBoat", 101, 14.019);
        boat.setStatus(BoatStatus.RACING);
        final CompoundMark mark = new CompoundMark(
                "TestMark",
                Arrays.asList(
                        new Mark(11, "Mark 1", "M1"),
                        new Mark(12, "Mark 2", "M2")
                ),
                100
        );
        final MarkRounding rounding = new MarkRounding(1, mark, MarkRounding.Direction.PORT, 3);
        final long time = System.currentTimeMillis();

        MarkRoundingEvent event = new MarkRoundingEvent(time, boat, rounding);
        MarkRoundingMessageGenerator messageGenerator = new MarkRoundingMessageGenerator(event, RACE_ID);

        message = messageGenerator.getMessage();

        checkField("sync byte 1", 0x47, 1);
        checkField("sync byte 2", 0x83, 1);
        checkField("message type", AC35MessageType.MARK_ROUNDING.getCode(), 1);
        index += 6; //checkField("timestamp", 0L, 6);
        index += 4; //checkField("source id", 0, 4);
        checkField("length", 21, 2);

        // Body
        checkField("version num", 1, 1);
        checkField("time", time, 6);
        checkField("ack num", 0, 2);
        checkField("race id", RACE_ID, 4);
        checkField("yacht id", boat.getId(), 4);
        checkField("boat status", 1, 1);
        checkField("rounding side", 1, 1);
        checkField("mark type", (mark.getMarks().size() == 2) ? 0x02 : 0x01, 1);
        checkField("mark id", mark.getId(), 1);

        index += 4; // checksum
        assertEquals("didn't read entire message", index, message.length);
    }

    private void checkField(String assertion, int expected, int length) {
        assertEquals(assertion, expected, ByteCheck.byteToInt(message, index, length));
        index += length;
    }

    private void checkField(String assertion, long expected, int length) {
        assertEquals(assertion, expected, ByteCheck.byteToLong(message, index, length));
        index += length;
    }
}