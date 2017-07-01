package seng302.team18.test_mock.ac35_xml_encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.Ac35XmlBoatComponents;
import seng302.team18.model.Boat;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import java.util.List;

/**
 * Created by afj19 on 1/07/17.
 */
public class BoatsXmlEncoder extends XmlEncoder<AC35XMLBoatMessage> {

    public DOMSource getDomSource(AC35XMLBoatMessage raceMessage) throws ParserConfigurationException {
        //Root
        Document doc = createDocument();
        Element root = doc.createElement(Ac35XmlBoatComponents.ROOT_BOATS.toString());
        doc.appendChild(root);

        // Modified

        // Version

        // Settings

        // Boat Shapes

        // Boats



        return new DOMSource(doc);
    }

    private Element encodeBoats(Document doc, List<Boat> boats) {
        Element element = doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOATS.toString());

        // loop and encode boats

        return element;
    }
}
