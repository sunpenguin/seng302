package seng302.team18.racemodel.ac35_xml_encoding;

/**
 * Defaults for when building a Boats.xml message from model classes
 */
public class BoatXmlDefaults implements IBoatsXmlDefaults {

    public int getVersion() {
        return 12;
    }


    public String getRaceBoatType() {
        return "AC45";
    }


    public double getBoatLength() {
        return 14.019d;
    }


    public double getHullLength() {
        return 13.449d;
    }


    public double getMarkZoneSize() {
        return 40.347d;
    }


    public double getCourseZoneSize() {
        return 40.347d;
    }


    public double getLimit1() {
        return 200d;
    }


    public double getLimit2() {
        return 100d;
    }


    public double getLimit3() {
        return 40.347;
    }


    public double getLimit4() {
        return 0d;
    }


    public double getLimit5() {
        return -100d;
    }


    public double getGpsX() {
        return 0.001;
    }


    public double getGpsY() {
        return 0.625;
    }


    public double getGpsZ() {
        return 1.738;
    }


    public double getFlagX() {
        return 0.000;
    }


    public double getFlagY() {
        return 4.233;
    }


    public double getFlagZ() {
        return 21.496;
    }
}
