package seng302.team18.racemodel.message_generating;

import seng302.team18.message.AC35MessageType;
import seng302.team18.model.PickUp;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dhl25 on 3/09/17.
 */
public class PowerTakenGenerator extends MessageGenerator {

    private PickUp pickUp;
    private int boatId;

    /**
     * Creates a PowerTakenMessage that signals when a power has been taken and by who.
     *
     * @param boatId of the boat that picked the power.
     * @param pickUp that was taken.
     */
    public PowerTakenGenerator(int boatId, PickUp pickUp) {
        super(AC35MessageType.POWER_TAKEN.getCode());
        this.boatId = boatId;
        this.pickUp = pickUp;
    }


    @Override
    protected byte[] getPayload() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] powerId = ByteCheck.intToByteArray(pickUp.getId());
        byte[] boatIdd = ByteCheck.intToByteArray(boatId);
        byte[] duration = ByteCheck.intToByteArray((int) pickUp.getPowerDuration());

        outStream.write(boatIdd);
        outStream.write(powerId);
        outStream.write(duration);

        return outStream.toByteArray();
    }
}
