package seng302.team18.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by david on 4/12/17.
 */
public class AC35XMLRegattaParser implements MessageBodyParser {

    @Override
    public MessageBody parse(byte[] bytes) {
        final String REGATTA_TAG = "RegattaConfig";
        final String REGATTA_ID = "RegattaID";
        final String CENTER_LAT = "CentralLatitude";
        final String CENTER_LONG = "CentralLongitude";
        final String UTC_OFFSET = "UtcOffset";

        InputStream stream = new ByteArrayInputStream(bytes);
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
        Element regattaElement = doc.getElementById(REGATTA_TAG);
//        int regattaID = Integer.parseInt(regattaElement.getAttribute(REGATTA_ID));
        double centralLat = Double.parseDouble(regattaElement.getAttribute(CENTER_LAT));
        double centralLong = Double.parseDouble(regattaElement.getAttribute(CENTER_LONG));
        String utcOffset = regattaElement.getAttribute(CENTER_LONG);

        return new AC35XMLRegattaMessage(centralLat, centralLong, utcOffset);
    }


}
