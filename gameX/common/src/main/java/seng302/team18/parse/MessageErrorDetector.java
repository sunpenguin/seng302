package seng302.team18.parse;

/**
 * Interface for MessageErrorDetection.
 */
public interface MessageErrorDetector {

    int errorCheckSize();

    Boolean isValid(byte[] checkSum, byte[] messageBytes, byte[] headerBytes);
}
