package seng302.team18.test_mock;

import seng302.team18.test_mock.connection.CRCGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 18/04/2017.
 */
public class Main {

    public static void main (String[] args) {
        TestMock testMock = new TestMock();
        testMock.run();
    }
}
