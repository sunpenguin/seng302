package seng302.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public class CRCChecker implements MessageErrorDetector {


    @Override
    public int errorCheckSize() {
        return 0;
    }

    @Override
    public Boolean isValid(byte[] bytes) {
        return null;
    }
}
