package seng302.team18.visualiser.util;

/**
 * Created by sbe67 on 10/05/17.
 */
public class Session {
    private static Session instance = new Session();

    private Boolean boatNameImportant;
    private Boolean boatSpeedImportant;
    private Boolean estimatedTimeImportant;
    private Boolean timeSinceLastMarkImportant;

    private Session() {
    }

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
