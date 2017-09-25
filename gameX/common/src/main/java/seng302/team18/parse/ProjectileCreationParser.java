package seng302.team18.parse;

import com.google.common.io.ByteStreams;
import seng302.team18.message.MessageBody;
import seng302.team18.message.ProjectileCreationMessage;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class to parse projectile location messages
 */
public class ProjectileCreationParser implements MessageBodyParser {

    @Override
    public MessageBody parse(InputStream stream) {
        try {
            return parse(ByteStreams.toByteArray(stream));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public MessageBody parse(byte[] bytes) {
        final int PROJECTILE_ID_INDEX = 0;
        final int PROJECTILE_ID_LENGTH = 4;

        int sourceID = ByteCheck.byteToInt(bytes, PROJECTILE_ID_INDEX, PROJECTILE_ID_LENGTH);

        return new ProjectileCreationMessage(sourceID);

    }
}
