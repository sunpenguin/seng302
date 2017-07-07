package seng302.team18.test_mock.model;

import seng302.team18.message.AC35XMLRegattaMessage;

/**
 * Class to generate a new regatta.xml model for AC35 XML RaceMessage.
 */
public class RegattaModel {
    private double centralLat;
    private double centralLong;
    private String utcOffset;
    private int regattaId;
    private String regattaName;
    private AC35XMLRegattaMessage regattaMessage;


    /**
     * Method that create a AC35 XML RegattaMessage.
     * @return a AC35XMLRegattaMessage
     */
    public AC35XMLRegattaMessage getRegattaMessage() {
        regattaId = 4;
        regattaName = "Atlantic/Bermuda Test";
        centralLat = 32.298501;
        centralLong = -64.8435;
        utcOffset = "-3";

        regattaMessage = new AC35XMLRegattaMessage(regattaId, regattaName, centralLat, centralLong, utcOffset);

        return regattaMessage;
    }
}
