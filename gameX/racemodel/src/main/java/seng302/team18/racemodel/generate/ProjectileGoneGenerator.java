package seng302.team18.racemodel.generate;

import seng302.team18.message.AC35MessageType;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Generates ProjectileGone messages.
 */
public class ProjectileGoneGenerator extends MessageGenerator {

    private int id;


    /**
     * Constructor for ProjectileGoneGenerator.
     *
     * @param id of the projectile.
     */
    public ProjectileGoneGenerator(int id) {
        super(AC35MessageType.PROJECTILE_GONE.getCode());
        this.id = id;
    }


    @Override
    protected byte[] getPayload() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] id = ByteCheck.intToByteArray(this.id);

        outStream.write(id);

        return outStream.toByteArray();
    }

}
