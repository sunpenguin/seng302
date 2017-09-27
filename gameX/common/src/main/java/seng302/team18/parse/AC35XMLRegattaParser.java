package seng302.team18.parse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.message.AC35XmlRegattaComponents;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * A parser which reads information from an XML stream and creates messages representing information about the regatta.
 */
public class AC35XMLRegattaParser implements MessageBodyParser {

    /**
     * Takes the input stream (XML holding information about the regatta) and creates a regatta message to hold this
     * information.
     *
     * @param stream A stream of data in the form of an XML document.
     * @return A regatta message created by the parser with information from the input stream.
     */
    @Override
    public AC35XMLRegattaMessage parse(InputStream stream) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder(); // parser configuration exception
            doc = builder.parse(stream); // io exception, SAXException
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        doc.getDocumentElement().normalize();

        Element regattaElement = (Element) doc.getElementsByTagName(AC35XmlRegattaComponents.ROOT_REGATTA.toString()).item(0);

        int regattaID = Integer.parseInt(regattaElement.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_ID.toString()).item(0).getTextContent());
        String regattaName = regattaElement.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_NAME.toString()).item(0).getTextContent();
        String courseName = regattaElement.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_COURSE_NAME.toString()).item(0).getTextContent();

        double centralLat =
                Double.parseDouble(regattaElement.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_CENTER_LAT.toString()).item(0).getTextContent());
        double centralLong =
                Double.parseDouble(regattaElement.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_CENTER_LONG.toString()).item(0).getTextContent());

        String utcOffset = regattaElement.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_OFFSET.toString()).item(0).getTextContent();

        return new AC35XMLRegattaMessage(regattaID, regattaName, courseName, centralLat, centralLong, utcOffset);
    }

    /**
     * Converts the input byte stream to standard characters and passes these the the parser to read them and create the
     * regatta message.
     *
     * @param bytes an array of bytes from the input stream
     * @return a regatta message parsed from the input byte stream
     */
    @Override
    public AC35XMLRegattaMessage parse(byte[] bytes) {
        InputStream stream = new ByteArrayInputStream(new String(bytes, StandardCharsets.UTF_8).trim().getBytes());
        return parse(stream);
    }


}
