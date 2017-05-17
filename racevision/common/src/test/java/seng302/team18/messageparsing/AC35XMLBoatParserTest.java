package seng302.team18.messageparsing;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.model.Boat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jds112 on 25/04/17.
 */
public class AC35XMLBoatParserTest {

    private AC35XMLBoatMessage message;
    private AC35XMLBoatParser parser;

    @Before
    public void setUp() throws Exception {
        Path file = Paths.get("src/main/resources/boatsTest.xml");
        byte[] boatXMLbytes = Files.readAllBytes(file);
        parser = new AC35XMLBoatParser();
        message = (AC35XMLBoatMessage) parser.parse(boatXMLbytes);
    }

    @Test
    public void parse() throws Exception {
        List<Boat> expected = new ArrayList<>();
        expected.add(new Boat("Oracle Team USA", "USA", 101));
        expected.add(new Boat("Emirates Team New Zealand", "NZL", 103));
        expected.add(new Boat("Artemis Racing", "SWE", 102));

        List<Boat> actual = message.getBoats();

        assertEquals("An incorrect number of boats were added to the message object.",expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            Boat exp = expected.get(i);
            Boat act = actual.get(i);
            assertEquals(exp.getBoatName(), act.getBoatName());
            assertEquals(exp.getShortName(), act.getShortName());
            assertEquals(exp.getCoordinate(), act.getCoordinate());
            assertEquals(exp.getDestination(), act.getDestination());
            assertEquals(exp.getHeading(), act.getHeading(), 0.1);
            assertEquals(exp.getLegNumber(), act.getLegNumber());
            assertEquals(exp.getPlace(), act.getPlace());
            assertEquals(exp.getSpeed(), act.getSpeed(), 0.1);
        }
    }

}