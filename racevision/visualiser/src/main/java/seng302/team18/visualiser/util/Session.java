package seng302.team18.visualiser.util;

/**
 * The session class to hold variables between controller switching.
 */
public class Session {

    private Boolean boatNameImportant;
    private Boolean boatSpeedImportant;
    private Boolean estimatedTimeImportant;
    private Boolean timeSinceLastMarkImportant;

    /**
     * Constructs a single instance of the session.
     * Static method.
     */
    private static Session instance = new Session();

    /**
     * Private constructor to construct the singleton session class.
     */
    private Session() {
    }

    /**
     * Getter to return the current instance of the session class.
     * Static method.
     * @return the current instance.
     */
    public static Session getInstance() {
        return instance;
    }

    public Boolean getBoatNameImportant() {
        return boatNameImportant;
    }

    public void setBoatNameImportant(Boolean boatNameImportant) {
        this.boatNameImportant = boatNameImportant;
    }

    public Boolean getBoatSpeedImportant() {
        return boatSpeedImportant;
    }

    public void setBoatSpeedImportant(Boolean boatSpeedImportant) {
        this.boatSpeedImportant = boatSpeedImportant;
    }

    public Boolean getEstimatedTimeImportant() {
        return estimatedTimeImportant;
    }

    public void setEstimatedTimeImportant(Boolean estimatedTimeImportant) {
        this.estimatedTimeImportant = estimatedTimeImportant;
    }

    public Boolean getTimeSinceLastMarkImportant() {
        return timeSinceLastMarkImportant;
    }

    public void setTimeSinceLastMarkImportant(Boolean timeSinceLastMarkImportant) {
        this.timeSinceLastMarkImportant = timeSinceLastMarkImportant;
    }

}
