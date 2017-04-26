package seng302.team18.test_mock;

import seng302.team18.test_mock.connection.CRCGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Justin on 18/04/2017.
 */
public class Main {

    public static void main (String[] args) {
        TestMock testMock = new TestMock();
        testMock.run();

        CRCGenerator crcGenerator = new CRCGenerator();
//        byte[] message = new byte[4];
//        message[0] = 0;
//        message[1] = 1;
//        message[2] = 2;
//        message[3] = 3;
//
//        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
//
//        try {
//            outputSteam.write(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        byte[] check = outputSteam.toByteArray();
        System.out.println(crcGenerator.generateCRC());
    }
}
