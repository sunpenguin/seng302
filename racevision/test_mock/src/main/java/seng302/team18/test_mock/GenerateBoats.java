package seng302.team18.test_mock;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by lenovo on 2017/4/17.
 */
public class GenerateBoats {

    public GenerateBoats () {
        try {
            setUpBoats();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void setUpBoats() throws ParserConfigurationException, TransformerException {
        StreamResult result = new StreamResult(new File("racevision/test_mock/src/main/resources/boats.xml"));

        // create xml file
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element boats = doc.createElement("BoatConfig");
        doc.appendChild(boats);

        Element modifyTime = doc.createElement("Modified");
        modifyTime.appendChild(doc.createTextNode("2012-05-17T07:49:40+0200"));
        boats.appendChild(modifyTime);

        Element version = doc.createElement("Version");
        version.appendChild(doc.createTextNode("12"));
        boats.appendChild(version);

        Element setting = doc.createElement("Settings");
        boats.appendChild(setting);
        Element boatType = doc.createElement("RaceBoatType");
        boatType.setAttribute("Type", "AC35");
        setting.appendChild(boatType);
        Element boatDimension = doc.createElement("BoatDimension");
        boatDimension.setAttribute("BoatLength", "14.019");
        boatDimension.setAttribute("HullLength", "13.449");
        setting.appendChild(boatDimension);
        Element zoneSize = doc.createElement("ZoneSize");
        zoneSize.setAttribute("MarkZoneSize", "40.347");
        zoneSize.setAttribute("CourseZoneSize", "40.347");
        setting.appendChild(zoneSize);
        Element zoneLimit = doc.createElement("ZoneLimits");
        zoneLimit.setAttribute("Limit1", "200");
        zoneLimit.setAttribute("Limit2", "100");
        zoneLimit.setAttribute("Limit3", "40.347");
        zoneLimit.setAttribute("Limit4", "0");
        zoneLimit.setAttribute("Limit5", "-100");
        setting.appendChild(zoneLimit);

        Element boatShapes = doc.createElement("BoatShapes");
        boats.appendChild(boatShapes);
        Element shape0 = doc.createElement("BoatShape");
        shape0.setAttribute("ShapeID","0");
        boatShapes.appendChild(shape0);
        Element vertice1 = doc.createElement("Vertices");
        shape0.appendChild(vertice1);



        // output result
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
    }
}
