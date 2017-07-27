package seng302.team18.racemodel.connection;

import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.racemodel.ac35_xml_encoding.RegattaXmlEncoder;
import seng302.team18.racemodel.ac35_xml_encoding.XmlEncoder;

/**
 * Class that returns a new RegattaXmlEncoder.
 */
public class XmlMessageGeneratorRegatta extends XMLMessageGenerator<AC35XMLRegattaMessage> {
    public XmlMessageGeneratorRegatta(AC35XMLRegattaMessage message) {
        super(message);
    }


    @Override
    protected XmlEncoder<AC35XMLRegattaMessage> getEncoder() {
        return new RegattaXmlEncoder();
    }
}
