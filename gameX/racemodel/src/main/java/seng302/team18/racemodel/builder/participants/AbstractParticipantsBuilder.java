package seng302.team18.racemodel.builder.participants;

import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Boat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base implementation class for participants builder.
 * <p>
 * This class provides a skeleton for classes that builder a participant pool.
 */
public abstract class AbstractParticipantsBuilder {

    private final List<Boat> participantPool = buildParticipantPool();


    /**
     * Get the entire pool of participants
     *
     * @return the participant pool
     */
    public List<Boat> getParticipantPool() {
        return Collections.unmodifiableList(participantPool);
    }


    /**
     * Get the IDs of all participants in the pool
     *
     * @return the IDs of all participants in the pool
     */
    public List<Integer> getIdPool() {
        return participantPool.stream().map(AbstractBoat::getId).collect(Collectors.toList());
    }


    protected abstract List<Boat> buildParticipantPool();
}
