package seng302.team18.messageparsing;

import org.junit.Test;
import seng302.team18.message.AC35XMLBoatMessage;

import static org.junit.Assert.assertEquals;

/**
 * Test the parser for boats.xml.
 */
public class AC35BoatsParserTest {

    private AC35XMLBoatMessage boatsMessage;

    public AC35BoatsParserTest() {
        AC35XMLBoatParser boatsParser = new AC35XMLBoatParser();
        boatsMessage = boatsParser.parse(this.getClass().getResourceAsStream("/boats_test1.xml"));
    }

    /*
    Check every boats in the boats xml file are parsed.
     */
    @Test
    public void boatNumTest() {
        int expectedBoatNum = 6;
        int actual = boatsMessage.getBoats().size();
        assertEquals(expectedBoatNum, actual);
    }

    /*
    Test the name of boats are parsed correctly.
     */
    @Test
    public void boatNameTest() {
        String boat1 = boatsMessage.getBoats().get(0).getName();
        assertEquals("Emirates Team New Zealand", boat1);
        String boat2 = boatsMessage.getBoats().get(1).getName();
        assertEquals("Oracle Team USA", boat2);
        String boat3 = boatsMessage.getBoats().get(2).getName();
        assertEquals("Artemis Racing", boat3);
        String boat4 = boatsMessage.getBoats().get(3).getName();
        assertEquals("Groupama Team France", boat4);
        String boat5 = boatsMessage.getBoats().get(4).getName();
        assertEquals("Land Rover BAR", boat5);
        String boat6 = boatsMessage.getBoats().get(5).getName();
        assertEquals("Softbank Team Japan", boat6);
    }

    /*
    Test the short name of boats are parsed correctly.
     */
    @Test
    public void boatShortNameTest() {
        String boat1 = boatsMessage.getBoats().get(0).getNameShort();
        assertEquals("TEAM New Zealand", boat1);
        String boat2 = boatsMessage.getBoats().get(1).getNameShort();
        assertEquals("TEAM USA", boat2);
        String boat3 = boatsMessage.getBoats().get(2).getNameShort();
        assertEquals("TEAM SWISE", boat3);
        String boat4 = boatsMessage.getBoats().get(3).getNameShort();
        assertEquals("TEAM France", boat4);
        String boat5 = boatsMessage.getBoats().get(4).getNameShort();
        assertEquals("TEAM Britain", boat5);
        String boat6 = boatsMessage.getBoats().get(5).getNameShort();
        assertEquals("TEAM Japan", boat6);
    }

    /*
    Test the SourceID of boats are parsed correctly.
     */
    @Test
    public void boatIDTest() {
        int boat1 = boatsMessage.getBoats().get(0).getId();
        assertEquals(111, boat1);
        int boat2 = boatsMessage.getBoats().get(1).getId();
        assertEquals(112, boat2);
        int boat3 = boatsMessage.getBoats().get(2).getId();
        assertEquals(113, boat3);
        int boat4 = boatsMessage.getBoats().get(3).getId();
        assertEquals(114, boat4);
        int boat5 = boatsMessage.getBoats().get(4).getId();
        assertEquals(115, boat5);
        int boat6 = boatsMessage.getBoats().get(5).getId();
        assertEquals(116, boat6);
    }
}