package seng302.team18.racemodel.message_generating;

import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.racemodel.ac35_xml_encoding.RaceXmlEncoder;
import seng302.team18.racemodel.ac35_xml_encoding.XmlEncoder;

/**
 * Created by hqi19 on 5/07/17.
 */
public class XmlMessageGeneratorRace extends XMLMessageGenerator<AC35XMLRaceMessage> {

    public XmlMessageGeneratorRace(AC35XMLRaceMessage message) {
        super(message);
    }

    @Override
    protected XmlEncoder<AC35XMLRaceMessage> getEncoder() {
        return new RaceXmlEncoder();
    }
}
