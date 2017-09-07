package seng302.team18.racemodel.message_generating;

import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by csl62 on 7/09/17.
 */
public class ProjectileGoneGenerator extends MessageGenerator {

    private int id;

    public ProjectileGoneGenerator(int type, int id) {
        super(type);
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
