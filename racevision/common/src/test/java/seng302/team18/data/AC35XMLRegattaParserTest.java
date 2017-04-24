package seng302.team18.data;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by csl62 on 20/04/17.
 */
public class AC35XMLRegattaParserTest {

    private AC35XMLRegattaParser testParser = new AC35XMLRegattaParser();

    @Test
    public void runTest() throws IOException {
        InputStream testFile = new BufferedInputStream(getClass().getResourceAsStream("/Regatta.xml"));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = testFile.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] testBytes = buffer.toByteArray();
        MessageBody message = testParser.parse(testBytes);
        AC35XMLRegattaMessage messageTest = (AC35XMLRegattaMessage) message;
        assertEquals("-5", messageTest.getUtcOffset());
        assertEquals(41.48815029598392, messageTest.getCentralLat(), 0.0001);
        assertEquals(-71.34977476608917, messageTest.getCentralLong(), 0.0001);
        assertEquals(5, messageTest.getType());
    }

}
