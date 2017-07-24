package seng302.team18.messageparsing;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Boat;
import seng302.team18.model.Mark;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class AC35XMLBoatParserTest {

    private AC35XMLBoatMessage message;
    private List<AbstractBoat> expectedBoats;

    @Before
    public void setUp() throws Exception {
        Path file = Paths.get("src/test/resources/boatsTest.xml");
        byte[] bytes = Files.readAllBytes(file);
        AC35XMLBoatParser parser = new AC35XMLBoatParser();
        message = parser.parse(bytes);

        setupBoats();
    }

    private void setupBoats() {
        expectedBoats = new ArrayList<>();

        expectedBoats.add(new Mark(122, "Constellation", "Constellation"));
        expectedBoats.add(new Mark(123, "Mischief", "Mischief"));
        expectedBoats.add(new Mark(124, "Atalanta", "Atalanta"));
        expectedBoats.add(new Mark(126, "Defender", "Defender"));

        expectedBoats.add(new Boat("Oracle Team USA", "USA", 101, 14.019));
        expectedBoats.add(new Boat("Emirates Team New Zealand", "NZL", 102, 14.019));
        expectedBoats.add(new Boat("Artemis Racing", "SWE", 103, 14.019));
    }

    @Test
    public void parseBoatType() throws Exception {
        List<AbstractBoat> parsedBoats = message.getBoats();
        for (int i = 0; i < parsedBoats.size(); i++) {
            assertEquals("Boat type parsed incorrectly for boat " + i,
                    expectedBoats.get(i).getType(), parsedBoats.get(i).getType());
        }
    }

    @Test
    public void parseBoatName() throws Exception {
        List<AbstractBoat> parsedBoats = message.getBoats();
        for (int i = 0; i < parsedBoats.size(); i++) {
            assertEquals("Boat name parsed incorrectly for boat " + i,
                    expectedBoats.get(i).getName(), parsedBoats.get(i).getName());
        }
    }

    @Test
    public void parseShortName() throws Exception {
        List<AbstractBoat> parsedBoats = message.getBoats();
        for (int i = 0; i < parsedBoats.size(); i++) {
            assertEquals("Short name parsed incorrectly for boat " + i,
                    expectedBoats.get(i).getShortName(), parsedBoats.get(i).getShortName());
        }
    }

    @Test
    public void parseSourceId() throws Exception {
        List<AbstractBoat> parsedBoats = message.getBoats();
        for (int i = 0; i < parsedBoats.size(); i++) {
            assertEquals("Source ID parsed incorrectly for boat " + i,
                    expectedBoats.get(i).getId(), parsedBoats.get(i).getId());
        }
    }

    @Test
    public void parseBoatLength() throws Exception {
        List<AbstractBoat>  parsedBoats = message.getBoats();
        for (int i = 0; i < parsedBoats.size(); i++) {
            if (parsedBoats.get(i) instanceof Boat) {
                assertEquals(( expectedBoats.get(i)).getLength(), (parsedBoats.get(i)).getLength(), 0.1);
            }
        }
    }
}