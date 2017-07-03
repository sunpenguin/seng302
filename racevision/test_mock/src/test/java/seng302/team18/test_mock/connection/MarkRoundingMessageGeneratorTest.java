package seng302.team18.test_mock.connection;

import org.junit.Test;
import seng302.team18.message.AC35MessageType;
import seng302.team18.model.Yacht;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.MarkRoundingEvent;
import seng302.team18.model.Race;
import seng302.team18.test_mock.TestMock;
import seng302.team18.util.ByteCheck;

import static org.junit.Assert.assertEquals;

/**
 * Created by Anton J on 4/05/2017.
 */
public class MarkRoundingMessageGeneratorTest {
    private byte[] message;
    private int index = 0;

    @Test
    public void getPayload() throws Exception {
        final TestMock testMock = new TestMock();
        final Race race = testMock.testRun();
        final Yacht yacht = race.getStartingList().get(0);
        final CompoundMark mark = race.getCourse().getCompoundMarks().get(0);
        final long time = System.currentTimeMillis();

        MarkRoundingEvent event = new MarkRoundingEvent(time, yacht, mark);
        MarkRoundingMessageGenerator messageGenerator = new MarkRoundingMessageGenerator(event, race.getId());

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
        checkField("race id", race.getId(), 4);
        checkField("yacht id", yacht.getId(), 4);
        checkField("boat status", 0, 1);
        checkField("rounding side", 0, 1);
        checkField("mark type", (mark.getMarks().size() == 2) ? 0x02 : 0x01, 1);
        checkField("mark id", mark.getId(), 1);

        index += 4; // checksum
        assertEquals("didn't read entire message", index, message.length);
    }

    private void checkField(String assertion, int expected, int length) {
        System.out.println();
        assertEquals(assertion, expected, ByteCheck.byteToIntConverter(message, index, length));
        index += length;
    }

    private void checkField(String assertion, long expected, int length) {
        System.out.println();
        assertEquals(assertion, expected, ByteCheck.byteToLongConverter(message, index, length));
        index += length;
    }
}