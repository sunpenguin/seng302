package seng302.team18.test_mock.connection;

import seng302.team18.message.AC35MessageType;

/**
 * Created by csl62 on 30/06/17.
 */
public class RequestMessageGenerator extends MessageGenerator{

    private int field;

    public RequestMessageGenerator(int field){
        super(AC35MessageType.XML_MESSAGE.getCode());
        this.field = field;
    }

    public byte[] getPayload(){
        byte[] payload = {0};
        return payload;
    }

}
