package seng302.team18.message;

/**
 * MessageBody that contains the regatta information from the XML file sent by the AC35 streaming protocol.
 */
public class AC35XMLRegattaMessage implements XmlMessage {
    private double centralLat;
    private double centralLong;
    private String utcOffset;
    private int id;
    private String regattaName;
    private String courseName;

    /**
     * Constructor for AC35XMLRegattaMessage.
     * Note: centralLat and centralLong may be incorrect from the XML.
     *
     * @param id          the regatta ID.
     * @param regattaName the race regattaName.
     * @param centralLat  the central latitude of the course.
     * @param centralLong the central longitude of the course.
     * @param utcOffset   the UTC offset of where the race is held.
     * @param courseName  the name of the course
     */
    public AC35XMLRegattaMessage(int id, String regattaName, String courseName, double centralLat, double centralLong, String utcOffset) {
        this.id = id;
        this.regattaName = regattaName;
        this.courseName = courseName;
        this.centralLat = centralLat;
        this.centralLong = centralLong;
        this.utcOffset = utcOffset;
    }


    @Override
    public int getType() {
        return AC35MessageType.XML_REGATTA.getCode();
    }

    /**
     * Getter for the central latitude of the course.
     *
     * @return the central latitude of the course.
     */
    public double getCentralLat() {
        return centralLat;
    }

    /**
     * Getter for the central longitude of the course.
     *
     * @return the central longitude of the course.
     */
    public double getCentralLong() {
        return centralLong;
    }

    /**
     * Getter for the the UTC offset of where the race is held.
     *
     * @return the UTC offset of where the race is held.
     */
    public String getUtcOffset() {
        return utcOffset;
    }

    /**
     * Getter for the regatta ID.
     *
     * @return the regatta ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the race regattaName.
     *
     * @return the race regattaName.
     */
    public String getRegattaName() {
        return regattaName;
    }

    public String getCourseName() {
        return courseName;
    }

}
