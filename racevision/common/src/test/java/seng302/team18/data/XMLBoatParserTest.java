package seng302.team18.data;

import org.junit.Test;
import org.xml.sax.SAXException;
import seng302.team18.model.Boat;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by dhl25 on 30/03/17.
 */
public class XMLBoatParserTest {

//    private InputStream boatFile = new BufferedInputStream(getClass().getResourceAsStream("/test-boats.xml"));

//    @Test
//    public void testParseBoats() throws IOException, SAXException, ParserConfigurationException {
//
//        List<Boat> actual = XMLBoatParser.parseBoats(boatFile);
//        List<Boat> expected = new ArrayList<>();
//        expected.add(new Boat("Emirates Team New Zealand", "NZL", 45.0));
//        expected.add(new Boat("Oracle Team USA", "USA", 50));
//        expected.add(new Boat("Artemis Racing", "SWE", 55.0));
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < expected.size(); i++) {
//            Boat exp = expected.get(i);
//            Boat act = actual.get(i);
//            assertEquals(exp.getBoatName(), act.getBoatName());
//            assertEquals(exp.getShortName(), act.getShortName());
//            assertEquals(exp.getCoordinate(), act.getCoordinate());
//            assertEquals(exp.getDestination(), act.getDestination());
//            assertEquals(exp.getHeading(), act.getHeading(), 0.1);
//            assertEquals(exp.getLeg(), act.getLeg());
//            assertEquals(exp.getPlace(), act.getPlace());
//            assertEquals(exp.getSpeed(), act.getSpeed(), 0.1);
//        }
//    }

}
