package seng302.team18.message;

import seng302.team18.model.*;

/**
 * MessageBody that contains information about a boats leg number, status and estimated time at next mark.
 */
public class AC35BoatStatusMessage {

    private int boatId;
    private int legNumber;
    private BoatStatus boatStatus;
    private long estimatedTimeAtNextMark;

    /**
     * Constructor for the AC35BoatStatusMessage. Contains information on a boats status.
     *
     * @param boatId                  the boats id.
     * @param legNumber               sequence number of the current leg.
     * @param boatStatus              status of the boat.
     * @param estimatedTimeAtNextMark in Epoch milliseconds.
     */
    public AC35BoatStatusMessage(int boatId, int legNumber, BoatStatus boatStatus, long estimatedTimeAtNextMark) {
        this.boatId = boatId;
        this.legNumber = legNumber;
        this.boatStatus = boatStatus;
        this.estimatedTimeAtNextMark = estimatedTimeAtNextMark;
    }

    /**
     * Getter for the boats ID.
     *
     * @return the boats ID.
     */
    public int getBoatId() {
        return boatId;
    }

    /**
     * Getter for legNumber.
     *
     * @return the number of the leg the boat is on.
     */
    public int getLegNumber() {
        return legNumber;
    }

    /**
     * Getter for the boats status.
     *
     * 0 Undefined
     * 1 Prestart
     * 2 Racing
     * 3 Finished
     * 4 DNS (did not start)
     * 5 DNF (did not finish)
     * 6 DSQ (disqualified)
     * 7 OCS (On Course Side – across start line early)
     *
     * @return that represents the boats status.
     */
    public BoatStatus getBoatStatus() {
        return boatStatus;
    }

    /**
     * Getter for the estimated time at the next mark.
     *
     * @return estimated time at next mark in Epoch milliseconds.
     */
    public long getEstimatedTimeAtNextMark() {
        return estimatedTimeAtNextMark;
    }
}
