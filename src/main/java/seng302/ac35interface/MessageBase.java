package seng302.ac35interface;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.ByteBuffer;

/**
 * Top level encapsulation of a binary message in the AC35 Streaming Data
 * Interface Specification.
 *
 * Uses little endian-order
 *
 * Header           15 bytes
 * { Sync byte 1    1 byte      0x47,
 *   Sync byte 2    1 byte      0x83,
 *   Message type   1 byte     {    0x01 - Heartbeat
 *                                  0x0C - Race Status
 *                                  0x14 - Display Text Message
 *                                  0x1A - XML Message
 *                                  0x1B - Race Start Status
 *                                  0x1D - Yacht Event Code
 *                                  0x1F - Yacht Action Code
 *                                  0x24 - Chatter Text
 *                                  0x25 - Boat Location
 *                                  0x26 - Mark Rounding
 *                                  0x2C - Course Wind
 *                                  0x2F - Avg Wind
 *                              },
 *   Timestamp      6 bytes,
 *   Source ID      4 bytes,
 *   Message Length 2 bytes
 * },
 * Message body     (varies),
 * CRC              4 bytes
 */
public class MessageBase extends BinaryMessage {
    // WARNING: all values are unsigned but are stored in signed types.
    public final byte SYNC_BYTE_1 = 0x47;
    public final byte SYNC_BYTE_2 = (byte) 0x83;

    private byte messageType;
    private long timestamp;
    private int sourceId;
    private short messageLength;
    private MessageBody messageBody;

    @Override
    public ByteBuffer encode() {
        throw new NotImplementedException();
    }

    @Override
    public void decode(ByteBuffer streamIn) {
        throw new NotImplementedException();
    }
}
