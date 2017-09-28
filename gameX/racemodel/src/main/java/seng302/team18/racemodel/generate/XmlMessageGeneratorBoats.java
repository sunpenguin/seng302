package seng302.team18.racemodel.generate;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.racemodel.encode.BoatsXmlEncoder;
import seng302.team18.racemodel.encode.XmlEncoder;

/**
 * Generates a XMLBoatsMessage
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
