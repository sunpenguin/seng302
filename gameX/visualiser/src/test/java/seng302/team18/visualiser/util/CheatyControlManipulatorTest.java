package seng302.team18.visualiser.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;

import java.util.ArrayList;
import java.util.List;

/**
 * These tests do not cover the case when going clockwise / counter clockwise and the boat is heading exactly downwind.
 * However, it does cover the were the boat is moving directly upwind which is very similar.
 */
public class CheatyControlManipulatorTest {

    private CheatyControlManipulator controlManipulator;
    private int id;


    @Before
    public void setUp() {
        id = 1;
        controlManipulator = new CheatyControlManipulator();
        controlManipulator.setPlayerId(id);
    }

    /**
     * Turn clockwise while on right (0 ~ 180) side of the wind.
     */
    @Test
    public void clockwiseRightTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeDownwindMessage());
        List<MessageBody> actual = controlManipulator.generateClockwise(0, 90);
        Assert.assertTrue(listEqual(expected, actual));
    }


    /**
     * Turn clockwise while on left (180 ~ 360) side of the wind.
     */
    @Test
    public void clockwiseLeftTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeUpwindMessage());
        List<MessageBody> actual = controlManipulator.generateClockwise(180, 90);
        Assert.assertTrue(listEqual(expected, actual));
    }


    /**
     * test what happens when going clockwise
     * and the boats heading is the same as the wind (upwind)
     * and the boats heading was moving clockwise previously
     */
    @Test
    public void clockwiseContinueTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeDownwindMessage());
        controlManipulator.generateClockwise(270, 270 - 2);
        List<MessageBody> actual = controlManipulator.generateClockwise(270, 270);
        Assert.assertTrue(listEqual(expected, actual));
    }


    /**
     * test what happens when going clockwise
     * and the boats heading is the same as the wind (upwind)
     * and the boats heading was moving counter clockwise previously
     */
    @Test
    public void clockwiseOppositeTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeDownwindMessage());
        expected.add(makeUpwindMessage());
        expected.add(makeDownwindMessage());
        controlManipulator.generateCounterClockwise(270, 270 + 2);
        List<MessageBody> actual = controlManipulator.generateClockwise(270, 270);
        Assert.assertTrue(listEqual(expected, actual));
    }


    /**
     * test what happens when going counter clockwise
     * and the boats heading is the same as the wind (upwind)
     * and the boats heading was moving counter clockwise previously
     */
    @Test
    public void counterClockwiseContinueTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeDownwindMessage());
        controlManipulator.generateCounterClockwise(270, 270 + 2);
        List<MessageBody> actual = controlManipulator.generateCounterClockwise(270, 270);
        Assert.assertTrue(listEqual(expected, actual));
    }


    @Test
    public void counterClockwiseOppositeTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeDownwindMessage());
        expected.add(makeUpwindMessage());
        expected.add(makeDownwindMessage());
        controlManipulator.generateClockwise(270, 270 - 2);
        List<MessageBody> actual = controlManipulator.generateCounterClockwise(270, 270);
        Assert.assertTrue(listEqual(expected, actual));
    }


    /**
     * Turn counter clockwise while on left (180 ~ 360) side of the wind.
     */
    @Test
    public void counterClockwiseRightTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeUpwindMessage());
        List<MessageBody> actual = controlManipulator.generateCounterClockwise(90, 180);
        Assert.assertTrue(listEqual(expected, actual));
    }


    /**
     * Turn counter clockwise while on left (180 ~ 360) side of the wind.
     */
    @Test
    public void counterClockwiseLeftTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeDownwindMessage());
        List<MessageBody> actual = controlManipulator.generateCounterClockwise(180, 90);
        Assert.assertTrue(listEqual(expected, actual));
    }


    /**
     * tests if the manipulator makes upwind messages correctly
     */
    @Test
    public void upwindTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeUpwindMessage());
        List<MessageBody> actual = controlManipulator.generateUpwind(1, 90);
        Assert.assertTrue(listEqual(expected, actual));
    }


    /**
     * tests if the manipulator makes downwind messages correctly
     */
    @Test
    public void downwindTest() {
        List<MessageBody> expected = new ArrayList<>();
        expected.add(makeDownwindMessage());
        List<MessageBody> actual = controlManipulator.generateDownwind(1, 1);
        Assert.assertTrue(listEqual(expected, actual));
    }




    /**
     * makes an upwind message
     * @return an upwind message.
     */
    private MessageBody makeUpwindMessage() {
        BoatActionMessage message = new BoatActionMessage(id);
        message.setUpwind();
        return message;
    }


    /**
     * makes a downwind message.
     * @return a downwind message.
     */
    private MessageBody makeDownwindMessage() {
        BoatActionMessage message = new BoatActionMessage(id);
        message.setDownwind();
        return message;
    }


    /**
     * determines if two lists of messages are equal.
     * @param expected exactly that
     * @param actual exactly that
     * @return if they are equal
     */
    private boolean listEqual(List<MessageBody> expected, List<MessageBody> actual) {
        if (expected.size() != actual.size()) {
            System.err.println("not same size expected = " + expected.size() + " actual = " + actual.size());
            System.err.println("expected = " + expected);
            System.err.println("actual = " + actual);
            return false;
        }
        for (int i = 0; i < expected.size(); i++) {
            if (!expected.get(i).equals(actual.get(i))) {
                System.err.println("item at index " + i + " is not equal");
                System.err.println("expected = " + expected);
                System.err.println("actual = " + actual);
                return false;
            }
        }
        return true;
    }
}
