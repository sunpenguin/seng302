package seng302.team18.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
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
        DocumentBuilder builder = factory.newDocumentBuilder(); // parser configuration exception
        Document doc = builder.parse(stream); // io exception
        doc.getDocumentElement().normalize();
        Element regattaElement = (Element) doc.getElementsByTagName(REGATTA_TAG).item(0);
        int regattaID =
                Integer.parseInt(regattaElement.getElementsByTagName(REGATTA_ID).item(0).getTextContent());
        double centralLat =
                Double.parseDouble(regattaElement.getElementsByTagName(CENTER_LAT).item(0).getTextContent());
        double centralLong =
                Double.parseDouble(regattaElement.getElementsByTagName(CENTER_LONG).item(0).getTextContent());
        String utcOffset = regattaElement.getElementsByTagName(CENTER_LONG).item(0).getTextContent();
        return null;
    }

//<RegattaConfig>
//<RegattaID>3</RegattaID>
//    <RegattaName>New Zealand Test</RegattaName>
//    <CourseName>North Head</CourseName>
//<CentralLatitude>-36.82791529</CentralLatitude>
//<CentralLongitude>174.81218919</CentralLongitude>
//<CentralAltitude>0.00</CentralAltitude>
//<UtcOffset>12</UtcOffset>
//<MagneticVariation>14.1</MagneticVariation>
//</RegattaConfig>

}
