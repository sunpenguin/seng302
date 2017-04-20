import org.junit.Test;
import seng302.team18.test_mock.XMLparsers.AC35BoatsContainer;
import seng302.team18.test_mock.XMLparsers.AC35BoatsParser;

import static org.junit.Assert.assertEquals;

/**
 * Test the parser for boats.xml.
 */
public class AC35BoatsParserTest {

    private AC35BoatsContainer boatsContainer;

    public AC35BoatsParserTest() {
        AC35BoatsParser boatsParser = new AC35BoatsParser();
        boatsContainer = boatsParser.parse(this.getClass().getResourceAsStream("/boats_test1.xml"));
    }

    /*
    Check every boats in the boats xml file are parsed.
     */
    @Test
    public void boatNumTest() {
        int expectedBoatNum = 6;
        int actual = boatsContainer.getBoats().size();
        assertEquals(expectedBoatNum, actual);
    }

    /*
    Test the name of boats are parsed correctly.
     */
    @Test
    public void boatNameTest() {
        String boat1 = boatsContainer.getBoats().get(0).getBoatName();
        assertEquals("Emirates Team New Zealand", boat1);
        String boat2 = boatsContainer.getBoats().get(1).getBoatName();
        assertEquals("Oracle Team USA", boat2);
        String boat3 = boatsContainer.getBoats().get(2).getBoatName();
        assertEquals("Artemis Racing", boat3);
        String boat4 = boatsContainer.getBoats().get(3).getBoatName();
        assertEquals("Groupama Team France", boat4);
        String boat5 = boatsContainer.getBoats().get(4).getBoatName();
        assertEquals("Land Rover BAR", boat5);
        String boat6 = boatsContainer.getBoats().get(5).getBoatName();
        assertEquals("Softbank Team Japan", boat6);
    }

    /*
    Test the short name of boats are parsed correctly.
     */
    @Test
    public void boatShortNameTest() {
        String boat1 = boatsContainer.getBoats().get(0).getShortName();
        assertEquals("TEAM New Zealand", boat1);
        String boat2 = boatsContainer.getBoats().get(1).getShortName();
        assertEquals("TEAM USA", boat2);
        String boat3 = boatsContainer.getBoats().get(2).getShortName();
        assertEquals("TEAM SWISE", boat3);
        String boat4 = boatsContainer.getBoats().get(3).getShortName();
        assertEquals("TEAM France", boat4);
        String boat5 = boatsContainer.getBoats().get(4).getShortName();
        assertEquals("TEAM Britain", boat5);
        String boat6 = boatsContainer.getBoats().get(5).getShortName();
        assertEquals("TEAM Japan", boat6);
    }

    /*
    Test the SourceID of boats are parsed correctly.
     */
    @Test
    public void boatIDTest() {
        int boat1 = boatsContainer.getBoats().get(0).getId();
        assertEquals(111, boat1);
        int boat2 = boatsContainer.getBoats().get(1).getId();
        assertEquals(112, boat2);
        int boat3 = boatsContainer.getBoats().get(2).getId();
        assertEquals(113, boat3);
        int boat4 = boatsContainer.getBoats().get(3).getId();
        assertEquals(114, boat4);
        int boat5 = boatsContainer.getBoats().get(4).getId();
        assertEquals(115, boat5);
        int boat6 = boatsContainer.getBoats().get(5).getId();
        assertEquals(116, boat6);
    }
}