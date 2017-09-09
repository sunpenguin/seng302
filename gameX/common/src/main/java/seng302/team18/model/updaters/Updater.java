package seng302.team18.model.updaters;

import seng302.team18.model.Race;

/**
 * Interface to update races
 */
public interface Updater {

    /**
     * Updates the given race by the given amount of time.
     *
     * @param race to update
     * @param time to update by (in milliseconds)
     */
    void update(Race race, double time);
}
