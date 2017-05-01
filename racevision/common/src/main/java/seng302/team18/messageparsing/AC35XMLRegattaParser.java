package seng302.team18.messageparsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by david on 4/12/17.
 */
public class AC35XMLRegattaParser implements MessageBodyParser {

    @Override
    public AC35XMLRegattaMessage parse(InputStream stream) {
        final String REGATTA_TAG = "RegattaConfig";
        final String REGATTA_ID = "RegattaID";
        final String CENTER_LAT = "CentralLatitude";
        final String CENTER_LONG = "CentralLongitude";
        final String UTC_OFFSET = "UtcOffset";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder(); // parser configuration exception
            doc = builder.parse(stream); // io exception, SAXException
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
        doc.getDocumentElement().normalize();
        Element regattaElement = (Element) doc.getElementsByTagName(REGATTA_TAG).item(0);
        int regattaID = Integer.parseInt(regattaElement.getElementsByTagName(REGATTA_ID).item(0).getTextContent());
//        System.out.println(regattaElement.getElementsByTagName(CENTER_LAT).item(0).getTextContent());

        double centralLat =
                Double.parseDouble(regattaElement.getElementsByTagName(CENTER_LAT).item(0).getTextContent());
        double centralLong =
                Double.parseDouble(regattaElement.getElementsByTagName(CENTER_LONG).item(0).getTextContent());

        String utcOffset = regattaElement.getElementsByTagName(UTC_OFFSET).item(0).getTextContent();

        return new AC35XMLRegattaMessage(regattaID, centralLat, centralLong, utcOffset);
    }



    @Override
    public AC35XMLRegattaMessage parse(byte[] bytes) {
        InputStream stream = new ByteArrayInputStream(new String(bytes, StandardCharsets.UTF_8).trim().getBytes());
        return parse(stream);
    }


}