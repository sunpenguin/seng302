import org.junit.Before;
import org.junit.Test;
import seng302.team18.test_mock.XMLparsers.AC35RaceContainer;
import seng302.team18.test_mock.XMLparsers.AC35RaceParser;
import seng302.team18.test_mock.XMLparsers.AC35RegattaContainer;
import seng302.team18.test_mock.XMLparsers.AC35RegattaParser;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 20/04/2017.
 */
public class AC35RaceParserTest {

    private AC35RaceContainer ac35RaceContainer;

    @Before
    public void setup() {
        AC35RaceParser ac35RaceParser = new AC35RaceParser();
        ac35RaceContainer = ac35RaceParser.parse(this.getClass().getResourceAsStream("/race_test1.xml"));
    }

    @Test
    public void testParser() {
        // Retrieve everything from the container


        // Assert that everything in the container is correct

    }
}
