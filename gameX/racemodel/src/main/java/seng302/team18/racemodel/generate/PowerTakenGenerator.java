package seng302.team18.racemodel.generate;

import seng302.team18.message.AC35MessageType;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dhl25 on 3/09/17.
 */
public class PowerTakenGenerator extends MessageGenerator {

    private int powerId;
    private double duration;
    private int boatId;

    /**
     * Creates a PowerTakenMessage that signals when a power has been taken and by who.
     *
     * @param boatId of the boat that picked the power.
     * @param powerId of the power up.
     * @param duration of the power up.
     */
    public PowerTakenGenerator(int boatId, int powerId, double duration) {
        super(AC35MessageType.POWER_TAKEN.getCode());
        this.boatId = boatId;
        this.powerId = powerId;
        this.duration = duration;
    }


    @Override
    protected byte[] getPayload() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] powerId = ByteCheck.intToByteArray(this.powerId);
        byte[] boatIdd = ByteCheck.intToByteArray(boatId);
        byte[] duration = ByteCheck.intToByteArray((int) this.duration);

        outStream.write(boatIdd);
        outStream.write(powerId);
        outStream.write(duration);

        return outStream.toByteArray();
    }
}
