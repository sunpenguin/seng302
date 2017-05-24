package seng302.team18.messageparsing;

/**
 * Interface for MessageErrorDetection.
 */
public interface MessageErrorDetector {

    int errorCheckSize();

    Boolean isValid(byte[] checkSum, byte[] messageBytes, byte[] headerBytes);
}
