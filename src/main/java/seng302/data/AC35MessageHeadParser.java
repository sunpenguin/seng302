package seng302.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35MessageHeadParser implements MessageHeadParser {

    private final int HEADER_BYTE_SIZE = 15;
//    private final byte FIRST_SYNC_BYTE = 0x47;
//    private final byte SECOND_SYNC_BYTE = 0x83;


    @Override
    public MessageHead parse(byte[] header) {

//        MessageHead head = new AC35MessageHead();
        return null;
    }


    @Override
    public int headerSize() {
        return HEADER_BYTE_SIZE;
    }
}
