package seng302.team18.test_mock.XMLparsers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import seng302.team18.model.Course;
import seng302.team18.model.Regatta;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jth102 on 19/04/17.
 */
public class AC35RegattaParser {

    private AC35RegattaContainer container = new AC35RegattaContainer();

    public AC35RegattaContainer parse(InputStream stream) {

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

            doc.getDocumentElement().normalize();
            Element regattaElement = (Element) doc.getElementsByTagName(REGATTA_TAG).item(0);

            int regattaID = Integer.parseInt(regattaElement.getElementsByTagName(REGATTA_ID).item(0).getTextContent());

            double centralLat =
                    Double.parseDouble(regattaElement.getElementsByTagName(CENTER_LAT).item(0).getTextContent());
            double centralLong =
                    Double.parseDouble(regattaElement.getElementsByTagName(CENTER_LONG).item(0).getTextContent());

            String utcOffset = regattaElement.getElementsByTagName(UTC_OFFSET).item(0).getTextContent();

            container.setRegattaID(regattaID);
            container.setCentralLatitude(centralLat);
            container.setGetCentralLongtitude(centralLong);
            container.setuTcOffset(utcOffset);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return container;
    }
}
