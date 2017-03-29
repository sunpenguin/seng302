package seng302;

import org.junit.Test;
import org.xml.sax.SAXException;
import seng302.model.Boat;
import seng302.parser.XMLBoatParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by dhl25 on 30/03/17.
 */
public class XMLBoatParserTest {

    private File boatFile = new File("src/main/resources/boats.xml");

    @Test
    public void testParseBoats() throws IOException, SAXException, ParserConfigurationException {

        List<Boat> boats = XMLBoatParser.parseBoats(boatFile);
        Boat testBoat = boats.get(4);
        assertEquals("Land Rover BAR", testBoat.getBoatName());
        assertEquals("GBR", testBoat.getTeamName());
        assertEquals(65.0, testBoat.getSpeed(), 0.1);
        assertEquals(6, boats.size());
    }

}
