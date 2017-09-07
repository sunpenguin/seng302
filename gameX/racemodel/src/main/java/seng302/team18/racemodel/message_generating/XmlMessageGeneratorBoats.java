package seng302.team18.racemodel.message_generating;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.racemodel.ac35_xml_encoding.BoatsXmlEncoder;
import seng302.team18.racemodel.ac35_xml_encoding.XmlEncoder;

/**
 * Created by hqi19 on 5/07/17.
 */
public class XmlMessageGeneratorBoats extends XMLMessageGenerator<AC35XMLBoatMessage> {

    public XmlMessageGeneratorBoats(AC35XMLBoatMessage message) {
        super(message);
    }

    @Override
    protected XmlEncoder<AC35XMLBoatMessage> getEncoder() {
        return new BoatsXmlEncoder();
    }
}
