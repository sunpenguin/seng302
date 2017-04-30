package seng302.team18.messageparsing;

/**
 * Created by dhl25 on 10/04/17.
 */
public interface MessageErrorDetector {

    public int errorCheckSize();

    public Boolean isValid(byte[] checkSum, byte[] messageBytes, byte[] headerBytes);
}
