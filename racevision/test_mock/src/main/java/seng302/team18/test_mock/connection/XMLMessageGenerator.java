package seng302.team18.test_mock.connection;

import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Class to send XML Files.
 * Note: Doesn't extend Scheduled. However, works similary.
 */
public class XMLMessageGenerator {

    private int messageType = 26; //For header always, 26
    private short sequenceNum = 0; //increments each time new message sent
    private byte messageSubtype; //5,6 or 7 depending on boats race or reggata xml message
    private short ackNum; //Sequence number of message. This number will be reflected in an AcknowledgeMessage in the future.
    private short payloadLength; //length
    private short lengthOfXML;
    private byte versionNumber = 1; //Always 1
    private String pathToFile; //

    public XMLMessageGenerator(byte messageSubtype, String pathToFile){
        this.messageSubtype = messageSubtype;
        this.pathToFile = pathToFile;
        ackNum = 0;
    }

    /**
     * Gets the message of a XMLMessage with header payload and CRC.
     * @return byte[] of the message
     */
    public byte[] getMessage(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] payload;
        try {
            payload = getPayload();
            byte[] header = getMessageHeader();
            outputStream.write(header);
            outputStream.write(payload);
            byte[] crc = CRCGenerator.generateCRC(outputStream.toByteArray());
            outputStream.write(crc);
            byte[] message = outputStream.toByteArray();
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            byte[] error = {0};
            return error; //return failed message
        }
    }

    private byte[] getMessageHeader() throws IOException {
        byte[] header = HeaderGenerator.generateHeader(messageType, payloadLength);
        return header;
    }

    private byte[] getPayload() throws IOException {
        ByteArrayOutputStream payloadStream = new ByteArrayOutputStream();
        byte[] ackNumAsBytes = ByteCheck.shortToByteArray(ackNum);
        byte[] timeStamp = ByteCheck.getCurrentTime6Bytes();
        byte[] seqNoASBytes = ByteCheck.shortToByteArray(sequenceNum);

        byte[] byteXML = getXMLAsBytes();
        byte[] lengthOfXMLAsBytes = ByteCheck.shortToByteArray(lengthOfXML);


        payloadStream.write(versionNumber);
        payloadStream.write(ackNumAsBytes);
        payloadStream.write(timeStamp);
        payloadStream.write(messageSubtype);
        payloadStream.write(seqNoASBytes);
        payloadStream.write(lengthOfXMLAsBytes);
        payloadStream.write(byteXML);

        byte[] payload =  payloadStream.toByteArray();
        payloadLength = (short) payload.length;
        return payload;
    }

    private byte[] getXMLAsBytes()throws IOException{
        File XML = new File(this.getClass().getResource(pathToFile).getFile());
        String content = new Scanner(XML).useDelimiter("\\Z").next();
        byte[] xmlBytes =  content.getBytes(StandardCharsets.UTF_8);
        lengthOfXML = (short)xmlBytes.length;
        return xmlBytes;
    }

    /**
     * To be used if and XML needs to be change to a new file
     * @param pathToFile path to the new file
     */
    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

}
