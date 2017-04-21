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
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

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
//        System.out.println("AC35XMLRegattaParser::parse called");
//        try {
//            System.out.println(new String(Arrays.copyOfRange(bytes, 14, bytes.length), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        for (byte b : bytes) {
//            System.out.println(b);
//        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder(); // parser configuration exception
            doc = builder.parse(stream); // io exception, SAXException
        } catch (ParserConfigurationException | SAXException | IOException e) {
//            System.out.println("cant build document");
//            e.printStackTrace();
//            System.out.println();
            return null;
        }
        doc.getDocumentElement().normalize();
        Element regattaElement = (Element) doc.getElementsByTagName(REGATTA_TAG).item(0);
//        int regattaID = Integer.parseInt(regattaElement.getElementsByTagName(REGATTA_ID).item(0).getTextContent());
//        System.out.println(regattaElement.getElementsByTagName(CENTER_LAT).item(0).getTextContent());
        double centralLat =
                Double.parseDouble(regattaElement.getElementsByTagName(CENTER_LAT).item(0).getTextContent());
        double centralLong =
                Double.parseDouble(regattaElement.getElementsByTagName(CENTER_LONG).item(0).getTextContent());
        String utcOffset = regattaElement.getElementsByTagName(UTC_OFFSET).item(0).getTextContent();

        return new AC35XMLRegattaMessage(centralLat, centralLong, utcOffset);
    }




}
