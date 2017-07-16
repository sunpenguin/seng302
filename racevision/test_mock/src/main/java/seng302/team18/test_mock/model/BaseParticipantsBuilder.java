package seng302.team18.test_mock.model;

import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Boat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Anton J on 11/07/2017.
 */
public abstract class BaseParticipantsBuilder {

    private final List<Boat> participantPool = buildParticipantPool();

    public List<Boat> getParticipantPool() {
        return Collections.unmodifiableList(participantPool);
    }

    public List<Integer> getIdPool() {
        return participantPool.stream().map(AbstractBoat::getId).collect(Collectors.toList());
    }

    protected abstract List<Boat> buildParticipantPool();
}
