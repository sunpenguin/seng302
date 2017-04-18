package seng302.team18.test_mock.AC35XMLconstructors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.model.Regatta;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by Justin on 18/04/2017.
 */
public class AC35RegattaXMLConstructor {

    /**
     * Construct regatta.XML to conform to the AC35 protocol.
     */
    public void construct(Regatta regatta) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // root - RegattaConfig
            Element rootElement = doc.createElement("RegattaConfig");
            doc.appendChild(rootElement);

            // children
            Element regattaID = doc.createElement("RegattaID");
            regattaID.appendChild(doc.createTextNode(Integer.toString(regatta.getRegattaID())));
            rootElement.appendChild(regattaID);

            Element regattaName = doc.createElement("RegattaName");
            regattaName.appendChild(doc.createTextNode(regatta.getRegattaName()));
            rootElement.appendChild(regattaName);

            Element uTcOffset = doc.createElement("UtcOffset");
            uTcOffset.appendChild(doc.createTextNode(Double.toString(regatta.getUTcOffset())));
            rootElement.appendChild(uTcOffset);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("regatta.XML"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
