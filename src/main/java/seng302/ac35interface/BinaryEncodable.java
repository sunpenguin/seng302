package seng302.ac35interface;

import java.nio.ByteBuffer;

/**
 * Created by Anton J on 9/04/2017.
 */
public interface BinaryEncodable {
    // TODO look at whether better to use ByteBuffer or DataOutStream etc.
    ByteBuffer encode();
}
