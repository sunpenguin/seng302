package seng302.team18.test_mock.connection;

import seng302.team18.message.AC35MessageType;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Generates a XML Message as
 * Note: Doesn't extend Scheduled. However, works similary.
 */
public class XMLMessageGenerator extends MessageGenerator {

    private short sequenceNum = 0; //increments each time new message sent
    private final byte messageSubtype; //5,6 or 7 depending on boats race or reggata xml message
    private short ackNum; //Sequence number of message. This number will be reflected in an AcknowledgeMessage in the future.
    private final static byte versionNumber = 1; //Always 1
    private final String pathToFile; //

    /**
     * Constructs a new instance of XMLMessageGenerator
     *
     * @param messageSubtype the type of the XML message
     * @param pathToFile the path to the XML file to be sent in the message
     */
    public XMLMessageGenerator(byte messageSubtype, String pathToFile) {
        super(AC35MessageType.XML_MESSAGE.getCode());
        this.messageSubtype = messageSubtype;
        this.pathToFile = pathToFile;
        ackNum = 0;
    }

    /**
     * Builds the payload of the XML message
     *
     * @return the payload as a byte array
     * @throws IOException if an I/O error occurs
     */
    protected byte[] getPayload() throws IOException {
        ByteArrayOutputStream payloadStream = new ByteArrayOutputStream();
        byte[] ackNumAsBytes = ByteCheck.shortToByteArray(ackNum);
        byte[] timeStamp = ByteCheck.getCurrentTime6Bytes();
        byte[] seqNoASBytes = ByteCheck.shortToByteArray(sequenceNum);

        byte[] byteXML = getXMLAsBytes();
        byte[] lengthOfXMLAsBytes = ByteCheck.shortToByteArray((short) byteXML.length);


        payloadStream.write(versionNumber);
        payloadStream.write(ackNumAsBytes);
        payloadStream.write(timeStamp);
        payloadStream.write(messageSubtype);
        payloadStream.write(seqNoASBytes);
        payloadStream.write(lengthOfXMLAsBytes);
        payloadStream.write(byteXML);

        return payloadStream.toByteArray();
    }

    private byte[] getXMLAsBytes() throws IOException {
        InputStream XML = this.getClass().getResourceAsStream(pathToFile);
        String content = new Scanner(XML).useDelimiter("\\Z").next();
        return content.getBytes(StandardCharsets.UTF_8);
    }

}
