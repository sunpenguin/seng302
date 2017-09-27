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

    /**
     * Gets the type of the message
     *
     * @return The XMLBoatMessage type
     */
    @Override
    public int getType() {
        return AC35MessageType.XML_BOATS.getCode();
    }

    /**
     * Gets the list of the boats
     *
     * @return The list of boats
     */
    public List<AbstractBoat> getBoats() {
        return boats;
    }

    /**
     * Gets a list of all the yachts
     *
     * @return A list of all the yachts
     */
    public List<Boat> getYachts() {
        return boats.stream()
                .filter(abstractBoat -> abstractBoat instanceof Boat)
                .map(abstractBoat -> (Boat) abstractBoat)
                .collect(Collectors.toList());
    }

    /**
     * Gets the Version of the AC35XMLBoatMessage
     *
     * @return the value of the version field
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the value of the version field for the AC35XMLBoatMessage
     *
     * @param version the value the version field will be set to
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Gets the raceBoatType of the AC35XMLBoatMessage
     *
     * @return the value of the raceBoatType field
     */
    public String getRaceBoatType() {
        return raceBoatType;
    }

    /**
     * Sets the value of the raceBoatType field for the AC35XMLBoatMessage
     *
     * @param raceBoatType the value the version field will be set to
     */
    public void setRaceBoatType(String raceBoatType) {
        this.raceBoatType = raceBoatType;
    }

    /**
     * Gets the boatLength of the AC35XMLBoatMessage
     *
     * @return the value of the boatLength field
     */
    public double getBoatLength() {
        return boatLength;
    }

    /**
     * Sets the value of the boatLength field for the AC35XMLBoatMessage
     *
     * @param boatLength the value the version field will be set to
     */
    public void setBoatLength(double boatLength) {
        this.boatLength = boatLength;
    }

    /**
     * Gets the hullLength of the AC35XMLBoatMessage
     *
     * @return the value of the hullLength field
     */
    public double getHullLength() {
        return hullLength;
    }

    /**
     * Sets the value of the hullLength field for the AC35XMLBoatMessage
     *
     * @param hullLength the value the version field will be set to
     */
    public void setHullLength(double hullLength) {
        this.hullLength = hullLength;
    }

    /**
     * Gets the markZoneSize of the AC35XMLBoatMessage
     *
     * @return the value of the markZoneSize field
     */
    public double getMarkZoneSize() {
        return markZoneSize;
    }

    /**
     * Sets the value of the markZoneSize field for the AC35XMLBoatMessage
     *
     * @param markZoneSize the value the version field will be set to
     */
    public void setMarkZoneSize(double markZoneSize) {
        this.markZoneSize = markZoneSize;
    }

    /**
     * Gets the courseZoneSize of the AC35XMLBoatMessage
     *
     * @return the value of the courseZoneSize field
     */
    public double getCourseZoneSize() {
        return courseZoneSize;
    }

    /**
     * Sets the value of the courseZoneSize field for the AC35XMLBoatMessage
     *
     * @param courseZoneSize the value the version field will be set to
     */
    public void setCourseZoneSize(double courseZoneSize) {
        this.courseZoneSize = courseZoneSize;
    }

    /**
     * Gets the limit1 of the AC35XMLBoatMessage
     *
     * @return the value of the limit1 field
     */
    public double getLimit1() {
        return limit1;
    }

    /**
     * Sets the value of the limit1 field for the AC35XMLBoatMessage
     *
     * @param limit1 the value the version field will be set to
     */
    public void setLimit1(double limit1) {
        this.limit1 = limit1;
    }

    /**
     * Gets the limit2 of the AC35XMLBoatMessage
     *
     * @return the value of the limit2 field
     */
    public double getLimit2() {
        return limit2;
    }

    /**
     * Sets the value of the limit2 field for the AC35XMLBoatMessage
     *
     * @param limit2 the value the version field will be set to
     */
    public void setLimit2(double limit2) {
        this.limit2 = limit2;
    }

    /**
     * Gets the limit3 of the AC35XMLBoatMessage
     *
     * @return the value of the limit3 field
     */
    public double getLimit3() {
        return limit3;
    }

    /**
     * Sets the value of the limit3 field for the AC35XMLBoatMessage
     *
     * @param limit3 the value the version field will be set to
     */
    public void setLimit3(double limit3) {
        this.limit3 = limit3;
    }

    /**
     * Gets the limit4 of the AC35XMLBoatMessage
     *
     * @return the value of the limit4 field
     */
    public double getLimit4() {
        return limit4;
    }

    /**
     * Sets the value of the limit4 field for the AC35XMLBoatMessage
     *
     * @param limit4 the value the version field will be set to
     */
    public void setLimit4(double limit4) {
        this.limit4 = limit4;
    }

    /**
     * Gets the limit5 of the AC35XMLBoatMessage
     *
     * @return the value of the limit5 field
     */
    public double getLimit5() {
        return limit5;
    }

    /**
     * Sets the value of the limit5 field for the AC35XMLBoatMessage
     *
     * @param limit5 the value the version field will be set to
     */
    public void setLimit5(double limit5) {
        this.limit5 = limit5;
    }

    /**
     * Gets the defaultGpsX of the AC35XMLBoatMessage
     *
     * @return the value of the defaultGpsX field
     */
    public double getDefaultGpsX() {
        return defaultGpsX;
    }

    /**
     * Sets the value of the defaultGpsX field for the AC35XMLBoatMessage
     *
     * @param defaultGpsX the value the version field will be set to
     */
    public void setDefaultGpsX(double defaultGpsX) {
        this.defaultGpsX = defaultGpsX;
    }

    /**
     * Gets the defaultGpsY of the AC35XMLBoatMessage
     *
     * @return the value of the defaultGpsY field
     */
    public double getDefaultGpsY() {
        return defaultGpsY;
    }

    /**
     * Sets the value of the defaultGpsY field for the AC35XMLBoatMessage
     *
     * @param defaultGpsY the value the version field will be set to
     */
    public void setDefaultGpsY(double defaultGpsY) {
        this.defaultGpsY = defaultGpsY;
    }

    /**
     * Gets the defaultGpsZ of the AC35XMLBoatMessage
     *
     * @return the value of the defaultGpsZ field
     */
    public double getDefaultGpsZ() {
        return defaultGpsZ;
    }

    /**
     * Sets the value of the defaultGpsZ field for the AC35XMLBoatMessage
     *
     * @param defaultGpsZ the value the version field will be set to
     */
    public void setDefaultGpsZ(double defaultGpsZ) {
        this.defaultGpsZ = defaultGpsZ;
    }

    /**
     * Gets the defaultFlagX of the AC35XMLBoatMessage
     *
     * @return the value of the defaultFlagX field
     */
    public double getDefaultFlagX() {
        return defaultFlagX;
    }

    /**
     * Sets the value of the defaultFlagX field for the AC35XMLBoatMessage
     *
     * @param defaultFlagX the value the version field will be set to
     */
    public void setDefaultFlagX(double defaultFlagX) {
        this.defaultFlagX = defaultFlagX;
    }

    /**
     * Gets the defaultFlagY of the AC35XMLBoatMessage
     *
     * @return the value of the defaultFlagY field
     */
    public double getDefaultFlagY() {
        return defaultFlagY;
    }

    /**
     * Sets the value of the defaultFlagY field for the AC35XMLBoatMessage
     *
     * @param defaultFlagY the value the version field will be set to
     */
    public void setDefaultFlagY(double defaultFlagY) {
        this.defaultFlagY = defaultFlagY;
    }

    /**
     * Gets the defaultFlagZ of the AC35XMLBoatMessage
     *
     * @return the value of the defaultFlagZ field
     */
    public double getDefaultFlagZ() {
        return defaultFlagZ;
    }

    /**
     * Sets the value of the defaultFlagZ field for the AC35XMLBoatMessage
     *
     * @param defaultFlagZ the value the version field will be set to
     */
    public void setDefaultFlagZ(double defaultFlagZ) {
        this.defaultFlagZ = defaultFlagZ;
    }

}
