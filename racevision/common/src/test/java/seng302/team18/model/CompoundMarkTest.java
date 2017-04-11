package seng302.team18.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 3/21/17.
 */
public class CompoundMarkTest {

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setErr(null);
    }


    @Test
    public void constructorTest() {
        ArrayList<Mark> testMarks = new ArrayList<>();
        testMarks.add(new Mark("test", new Coordinate(1, 1)));
        CompoundMark testCompoundMark1 = new CompoundMark("Test", testMarks);
        assertEquals("", errContent.toString());

        testMarks.add(new Mark("test", new Coordinate(1, 1)));
        testMarks.add(new Mark("test", new Coordinate(1, 1)));
        CompoundMark testCompoundMark2 = new CompoundMark("Test", testMarks);
        assertEquals("A compound mark cannot have more than 2 marks\n", errContent.toString());
    }

}
