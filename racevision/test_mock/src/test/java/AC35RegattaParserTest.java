import org.junit.Before;
import org.junit.Test;
import seng302.team18.test_mock.XMLparsers.AC35RegattaContainer;
import seng302.team18.test_mock.XMLparsers.AC35RegattaParser;

import static org.junit.Assert.assertEquals;

/**
 * Created by hqi19 on 20/04/17.
 */
public class AC35RegattaParserTest {
    private AC35RegattaContainer ac35RegattaContainer;

    @Before
    public void setup() {
        AC35RegattaParser ac35RegattaParser = new AC35RegattaParser();
        ac35RegattaContainer = ac35RegattaParser.parse(this.getClass().getResourceAsStream("/regatta_test1.xml"));
    }

    @Test
    public void testParser() {
        // Retrieve everything from the container
        int regattaID = ac35RegattaContainer.getRegattaID();
        double centralLat = ac35RegattaContainer.getCentralLatitude();
        double centralLong = ac35RegattaContainer.getCentralLongtitude();
        String uTcOffset = ac35RegattaContainer.getuTcOffset();

        // Assert that everything in the container is correct
        assertEquals(4, regattaID);
        assertEquals(32.298501, centralLat, 0.000001);
        assertEquals(-64.8435, centralLong, 0.000001);
        assertEquals("-3", uTcOffset);
    }
}
