package seng302.team18.message;

import java.time.format.DateTimeFormatter;

/**
 * Marker interface for messages in XML format
 */
public interface XmlMessage extends MessageBody {
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
}
