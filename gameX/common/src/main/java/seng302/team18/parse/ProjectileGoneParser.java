package seng302.team18.parse;

import com.google.common.io.ByteStreams;
import seng302.team18.message.MessageBody;
import seng302.team18.message.ProjectileGoneMessage;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * Parser for ProjectileGone messages.
 */
public class ProjectileGoneParser implements MessageBodyParser {


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
        final int SOURCE_ID_INDEX = 0;
        final int SOURCE_ID_LENGTH = 4;

        int sourceID = ByteCheck.byteToInt(bytes, SOURCE_ID_INDEX, SOURCE_ID_LENGTH);

        return new ProjectileGoneMessage(sourceID);
    }
}
