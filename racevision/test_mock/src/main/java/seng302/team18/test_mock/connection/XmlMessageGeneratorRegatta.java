package seng302.team18.test_mock.connection;

import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.test_mock.ac35_xml_encoding.XmlEncoder;

/**
 * Created by hqi19 on 5/07/17.
 */
public class XmlMessageGeneratorRegatta extends XMLMessageGenerator<AC35XMLRegattaMessage> {
    public XmlMessageGeneratorRegatta(AC35XMLRegattaMessage message) {
        super(message);
    }


    @Override
    protected XmlEncoder<AC35XMLRegattaMessage> getEncoder() {
        return null;
    }
}
