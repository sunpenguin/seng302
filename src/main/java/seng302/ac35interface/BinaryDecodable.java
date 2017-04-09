package seng302.ac35interface;

import java.nio.ByteBuffer;

/**
 * Created by Anton J on 9/04/2017.
 */
public interface BinaryDecodable {
    // TODO look at whether better to use ByteBuffer or DataInStream etc.
    void decode(ByteBuffer streamIn);
}
