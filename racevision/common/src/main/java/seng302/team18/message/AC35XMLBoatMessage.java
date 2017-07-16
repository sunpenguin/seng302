package seng302.team18.message;

import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Boat;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MessageBody that contains the boat information from the XML file sent by the AC35 streaming protocol.
 */
public class AC35XMLBoatMessage implements XmlMessage {

    private List<AbstractBoat> boats;

    private int version;
    private String raceBoatType;
    private double boatLength;
    private double hullLength;
    private double markZoneSize;
    private double courseZoneSize;
    private double limit1;
    private double limit2;
    private double limit3;
    private double limit4;
    private double limit5;

    private double defaultGpsX;
    private double defaultGpsY;
    private double defaultGpsZ;
    private double defaultFlagX;
    private double defaultFlagY;
    private double defaultFlagZ;

    /**
     * Constructor for AC35XMLBoatMessage.
     *
     * @param boats the boats participating in the race.
     */
    public AC35XMLBoatMessage(List<AbstractBoat> boats) {
        this.boats = boats;
    }

    @Override
    public int getType() {
        return AC35MessageType.XML_BOATS.getCode();
    }

    public List<AbstractBoat> getBoats() {
        return boats;
    }

    public List<Boat> getYachts() {
        return boats.stream()
                .filter(abstractBoat -> abstractBoat instanceof Boat)
                .map(abstractBoat -> (Boat) abstractBoat)
                .collect(Collectors.toList());
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getRaceBoatType() {
        return raceBoatType;
    }

    public void setRaceBoatType(String raceBoatType) {
        this.raceBoatType = raceBoatType;
    }

    public double getBoatLength() {
        return boatLength;
    }

    public void setBoatLength(double boatLength) {
        this.boatLength = boatLength;
    }

    public double getHullLength() {
        return hullLength;
    }

    public void setHullLength(double hullLength) {
        this.hullLength = hullLength;
    }

    public double getMarkZoneSize() {
        return markZoneSize;
    }

    public void setMarkZoneSize(double markZoneSize) {
        this.markZoneSize = markZoneSize;
    }

    public double getCourseZoneSize() {
        return courseZoneSize;
    }

    public void setCourseZoneSize(double courseZoneSize) {
        this.courseZoneSize = courseZoneSize;
    }

    public double getLimit1() {
        return limit1;
    }

    public void setLimit1(double limit1) {
        this.limit1 = limit1;
    }

    public double getLimit2() {
        return limit2;
    }

    public void setLimit2(double limit2) {
        this.limit2 = limit2;
    }

    public double getLimit3() {
        return limit3;
    }

    public void setLimit3(double limit3) {
        this.limit3 = limit3;
    }

    public double getLimit4() {
        return limit4;
    }

    public void setLimit4(double limit4) {
        this.limit4 = limit4;
    }

    public double getLimit5() {
        return limit5;
    }

    public void setLimit5(double limit5) {
        this.limit5 = limit5;
    }

    public double getDefaultGpsX() {
        return defaultGpsX;
    }

    public void setDefaultGpsX(double defaultGpsX) {
        this.defaultGpsX = defaultGpsX;
    }

    public double getDefaultGpsY() {
        return defaultGpsY;
    }

    public void setDefaultGpsY(double defaultGpsY) {
        this.defaultGpsY = defaultGpsY;
    }

    public double getDefaultGpsZ() {
        return defaultGpsZ;
    }

    public void setDefaultGpsZ(double defaultGpsZ) {
        this.defaultGpsZ = defaultGpsZ;
    }

    public double getDefaultFlagX() {
        return defaultFlagX;
    }

    public void setDefaultFlagX(double defaultFlagX) {
        this.defaultFlagX = defaultFlagX;
    }

    public double getDefaultFlagY() {
        return defaultFlagY;
    }

    public void setDefaultFlagY(double defaultFlagY) {
        this.defaultFlagY = defaultFlagY;
    }

    public double getDefaultFlagZ() {
        return defaultFlagZ;
    }

    public void setDefaultFlagZ(double defaultFlagZ) {
        this.defaultFlagZ = defaultFlagZ;
    }
}
