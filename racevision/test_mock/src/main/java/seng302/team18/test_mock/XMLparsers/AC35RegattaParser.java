package seng302.team18.test_mock.XMLparsers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;

/**
 * Created by jth102 on 19/04/17.
 */
public class AC35RegattaParser {

    public void parse(InputStream regattaFile) {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = dbFactory.newDocumentBuilder();


        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }


    }
}
