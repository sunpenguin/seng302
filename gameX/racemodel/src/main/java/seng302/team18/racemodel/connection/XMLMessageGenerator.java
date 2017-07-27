package seng302.team18.racemodel.connection;

import seng302.team18.message.AC35MessageType;
import seng302.team18.message.XmlMessage;
import seng302.team18.racemodel.ac35_xml_encoding.XmlEncoder;
import seng302.team18.util.ByteCheck;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Generates a XML Message as
 * Note: Doesn't extend Scheduled. However, works similarly.
 */
public abstract class XMLMessageGenerator<T extends XmlMessage> extends MessageGenerator {

    private short sequenceNum = 0; //increments each time new message sent
    private short ackNum; //Sequence number of message. This number will be reflected in an AcknowledgeMessage in the future.
    private final static byte versionNumber = 1; //Always 1
    private final T message;

    /**
     * Constructs a new instance of XMLMessageGenerator
     *
     * @param message the message to be sent
     */
    protected XMLMessageGenerator(T message) {
        super(AC35MessageType.XML_MESSAGE.getCode());
        this.message = message;
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

        byte[] byteXML;
        XmlEncoder<T> encoder = getEncoder();
        try {
            byteXML = encoder.encode(message).getBytes(StandardCharsets.UTF_8);
        } catch (TransformerException | ParserConfigurationException e) {
            throw new IOException(e);
        }
        byte[] lengthOfXMLAsBytes = ByteCheck.shortToByteArray((short) byteXML.length);


        payloadStream.write(versionNumber);
        payloadStream.write(ackNumAsBytes);
        payloadStream.write(timeStamp);
        payloadStream.write(message.getType());
        payloadStream.write(seqNoASBytes);
        payloadStream.write(lengthOfXMLAsBytes);
        payloadStream.write(byteXML);

        return payloadStream.toByteArray();
    }

    protected abstract XmlEncoder<T> getEncoder();

}
