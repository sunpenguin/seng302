package seng302.team18.test_mock;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Created by Justin on 18/04/2017.
 */
public class MockDataStream {

    public static void main (String[] args) throws TransformerException, ParserConfigurationException{
        TestMock testMock = new TestMock();
        testMock.run();
    }
}
