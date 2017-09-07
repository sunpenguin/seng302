package seng302.team18.message;

/**
 * Enum for the different actions of boat and their associated codes.
 */
public enum BoatActionStatus {
    AUTOPILOT((byte) 1),
    SAIL_IN((byte) 2),
    SAIL_OUT((byte) 3),
    TACK_GYBE((byte) 4),
    UPWIND((byte) 5),
    DOWNWIND((byte) 6),
    POWER_UP((byte) 7);

    private byte action;


    BoatActionStatus(byte action) {
        this.action = action;
    }


    public byte action() {
        return action;
    }
}
