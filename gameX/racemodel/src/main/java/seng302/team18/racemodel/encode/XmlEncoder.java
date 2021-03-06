package seng302.team18.racemodel.encode;

import org.w3c.dom.Document;
import seng302.team18.message.XmlMessage;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Abstract base class for encoding a XmlMessage type class into a XML-formatted string
 */
public abstract class XmlEncoder<T extends XmlMessage> {

    /**
     * Encodes a XML message to a string
     *
     * @param xmlMessage the message to encode
     * @return the message as a XML encoded string
     * @throws TransformerException         if the XML encoding fails
     * @throws ParserConfigurationException if the XML encoding cannot be undertaken
     */
    public String encode(T xmlMessage) throws TransformerException, ParserConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        StringWriter writer = new StringWriter();
        transformer.transform(getDomSource(xmlMessage), new StreamResult(writer));
        return writer.getBuffer().toString();
    }


    /**
     * Builds a document builder from the given message
     *
     * @param xmlMessage the message to encode
     * @return the message encoded into a document
     * @throws ParserConfigurationException if the XML encoding fails
     */
    public abstract DOMSource getDomSource(T xmlMessage) throws ParserConfigurationException;


    /**
     * Creates a new document
     *
     * @return a new document
     * @throws ParserConfigurationException if the document cannot be created
     */
    protected Document createDocument() throws ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }
}
