package seng302.team18.racemodel.generate;

import seng302.team18.message.AC35MessageType;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class to create projectile location messages
 */
public class ProjectileCreationMessageGenerator extends MessageGenerator {

    private int projectile_id;

    public ProjectileCreationMessageGenerator(int projectile_id) {
        super(AC35MessageType.PROJECTILE_CREATION.getCode());
        this.projectile_id = projectile_id;
    }

    @Override
    protected byte[] getPayload() throws IOException {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            byte[] id = ByteCheck.intToByteArray(this.projectile_id);

            outStream.write(id);

            return outStream.toByteArray();
    }
}
