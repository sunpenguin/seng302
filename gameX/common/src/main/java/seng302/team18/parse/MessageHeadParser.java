package seng302.team18.parse;

import seng302.team18.message.MessageHead;

/**
 * Interface for MessageHeadParser.
 */
public interface MessageHeadParser {


     MessageHead parse(byte[] header);

     int headerSize();


}
