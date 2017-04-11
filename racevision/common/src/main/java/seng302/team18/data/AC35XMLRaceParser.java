package seng302.team18.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by dhl25 on 11/04/17.
 */
public class AC35XMLRaceParser implements MessageBodyParser {



    @Override
    public MessageBody parse(byte[] bytes) {
        final String RACE_TAG = "Race";


        InputStream stream = new ByteArrayInputStream(bytes);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(stream);
        doc.getDocumentElement().normalize();
        Element raceElement = (Element) doc.getElementsByTagName(RACE_TAG).item(0);


        return null;
    }
}
