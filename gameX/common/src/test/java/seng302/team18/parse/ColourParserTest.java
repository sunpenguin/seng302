package seng302.team18.parse;

import javafx.scene.paint.Color;
import org.junit.Test;
import seng302.team18.message.ColourMessage;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by sbe67 on 17/08/17.
 */
public class ColourParserTest {

    private ColourParser testParser = new ColourParser();

    @Test
    public void runTest() {
            byte[] bytes = {105, 0, 0, 0, 16, 12, 14};
            MessageBodyParser parser = new ColourParser();
            ColourMessage message = (ColourMessage) parser.parse(bytes);

            int  expectedID= 105;
            int actualID = message.getSourceID();
            Color expectedColour = Color.rgb(16, 12, 14);
            Color actualColour = message.getColour();

            assertEquals(expectedID, actualID);
            assertEquals(expectedColour,actualColour);
;
    }
}
