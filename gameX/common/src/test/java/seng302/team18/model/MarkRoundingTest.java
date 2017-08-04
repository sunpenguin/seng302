package seng302.team18.model;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class MarkRoundingTest {
    private final Mark mark = new Mark(101, "Mark", "m");
    private final Mark mark2 = new Mark(102, "Mark2", "m2");
    private final CompoundMark compoundMark = new CompoundMark("CompoundMark", Collections.singletonList(mark), 101);
    private final CompoundMark compoundMark2 = new CompoundMark("CompoundMark2", Collections.singletonList(mark2), 102);

    private final MarkRounding markRounding1 = new MarkRounding(1, compoundMark, MarkRounding.Direction.PORT, 3);
    private final MarkRounding markRounding2 = new MarkRounding(1, compoundMark, MarkRounding.Direction.PORT, 3);
    private final MarkRounding markRounding_seqNo = new MarkRounding(2, compoundMark, MarkRounding.Direction.PORT, 3);
    private final MarkRounding markRounding_direction = new MarkRounding(1, compoundMark, MarkRounding.Direction.SP, 3);
    private final MarkRounding markRounding_zoneSize = new MarkRounding(1, compoundMark, MarkRounding.Direction.PORT, 4);
    private final MarkRounding markRounding_compoundMark = new MarkRounding(1, compoundMark2, MarkRounding.Direction.PORT, 3);

    @Test
    public void equalsTest_equal() throws Exception {
        assertEquals(markRounding1, markRounding2);
    }


    @Test
    public void equalsTest_identical() throws Exception {
        assertEquals(markRounding1, markRounding1);
    }


    @Test
    public void equalsTest_seqNo() throws Exception {
        assertNotEquals(markRounding1, markRounding_seqNo);
    }


    @Test
    public void equalsTest_compoundMark() throws Exception {
        assertNotEquals(markRounding1, markRounding_compoundMark);
    }


    @Test
    public void equalsTest_direction() throws Exception {
        assertNotEquals(markRounding1, markRounding_direction);
    }


    @Test
    public void equalsTest_zoneSize() throws Exception {
        assertNotEquals(markRounding1, markRounding_zoneSize);
    }
}