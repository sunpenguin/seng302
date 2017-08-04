package seng302.team18.racemodel.ac35_xml_encoding;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.XmlMessage;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import static org.junit.Assert.assertEquals;


public class XmlEncoderTest {

    private final String messageValue = "test";
    private final String elementName = "element";

    @Test
    public void encode() throws Exception {
        final ConcreteXmlEncoder encoder = new ConcreteXmlEncoder();
        final ConcreteXmlMessage message = new ConcreteXmlMessage(messageValue);

        String xml = encoder.encode(message);
        String element = xml.replaceFirst("<\\?xml.*?\\?>", "");
        String expected = "<" + elementName + ">" + messageValue + "</" + elementName + ">";
        assertEquals("Xml string has not been encoded correctly", expected, element);
    }


    private class ConcreteXmlMessage implements XmlMessage {

        private final String value;

        ConcreteXmlMessage(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }

        @Override
        public int getType() {
            return 0;
        }
    }


    private class ConcreteXmlEncoder extends XmlEncoder<ConcreteXmlMessage> {

        @Override
        public DOMSource getDomSource(ConcreteXmlMessage xmlMessage) throws ParserConfigurationException {
            Document document = createDocument();
            Element element = document.createElement(elementName);
            element.setTextContent(xmlMessage.getValue());
            document.appendChild(element);
            return new DOMSource(document);
        }
    }

}