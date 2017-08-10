package seng302.team18.visualiser.display;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

/**
 * Test class for the controls tutorial.
 */
public class ControlsTutorialTest {
    private ControlsTutorial tutorial;


    @Before
    public void setup() {
        tutorial = new ControlsTutorial(new Pane());
    }

    @Test
    public void checkProgressedTestBlueSkies() {
        List<KeyCode> keyCodes = new ArrayList<>();
        keyCodes.add(KeyCode.SHIFT);
        keyCodes.add(KeyCode.UP);
        keyCodes.add(KeyCode.DOWN);
        keyCodes.add(KeyCode.SPACE);
        keyCodes.add(KeyCode.ENTER);
        keyCodes.add(KeyCode.ENTER);
        keyCodes.add(KeyCode.SHIFT);

        List<Boolean> actual = new ArrayList<>();

        for (KeyCode key : keyCodes) {
            actual.add(tutorial.checkIfProgressed(key));
        }

        List<Boolean> expected = new ArrayList<>();
        expected.add(true);
        expected.add(true);
        expected.add(true);
        expected.add(true);
        expected.add(true);
        expected.add(true);
        expected.add(true);

        Assert.assertEquals(expected,actual);
    }


    @Test
    public void checkProgressedTestBlueSkies2() {
        List<KeyCode> keyCodes = new ArrayList<>();
        keyCodes.add(KeyCode.SHIFT);
        keyCodes.add(KeyCode.PAGE_UP);
        keyCodes.add(KeyCode.PAGE_DOWN);
        keyCodes.add(KeyCode.SPACE);
        keyCodes.add(KeyCode.ENTER);
        keyCodes.add(KeyCode.ENTER);
        keyCodes.add(KeyCode.SHIFT);

        List<Boolean> actual = new ArrayList<>();

        for (KeyCode key : keyCodes) {
            actual.add(tutorial.checkIfProgressed(key));
        }

        List<Boolean> expected = new ArrayList<>();
        expected.add(true);
        expected.add(true);
        expected.add(true);
        expected.add(true);
        expected.add(true);
        expected.add(true);
        expected.add(true);

        Assert.assertEquals(expected,actual);
    }


    @Test
    public void checkProgressedTestAcceptable() {
        List<KeyCode> keyCodes = new ArrayList<>();
        keyCodes.add(KeyCode.SHIFT);
        keyCodes.add(KeyCode.SHIFT);
        keyCodes.add(KeyCode.SHIFT);
        keyCodes.add(KeyCode.PAGE_UP);
        keyCodes.add(KeyCode.PAGE_UP);
        keyCodes.add(KeyCode.PAGE_DOWN);
        keyCodes.add(KeyCode.SPACE);
        keyCodes.add(KeyCode.SPACE);
        keyCodes.add(KeyCode.SHIFT);
        keyCodes.add(KeyCode.PAGE_DOWN);
        keyCodes.add(KeyCode.ENTER);
        keyCodes.add(KeyCode.ENTER);
        keyCodes.add(KeyCode.ENTER);
        keyCodes.add(KeyCode.PAGE_UP);
        keyCodes.add(KeyCode.SHIFT);

        List<Boolean> actual = new ArrayList<>();

        for (KeyCode key : keyCodes) {
            actual.add(tutorial.checkIfProgressed(key));
        }

        List<Boolean> expected = new ArrayList<>();
        expected.add(true); //Shift
        expected.add(false); //Shift
        expected.add(false); //Shift
        expected.add(true); //PG_uP
        expected.add(false);//Page_UP
        expected.add(true);//PgDN
        expected.add(true);//SPace
        expected.add(false);//Space
        expected.add(false);//Shift
        expected.add(false);//PGDN
        expected.add(true);//ENTER
        expected.add(true);//ENTER
        expected.add(false);//ENTER
        expected.add(false);//PGUP
        expected.add(true);//Shift



        Assert.assertEquals(expected,actual);
    }


    @Test
    public void checkProgressedTestMonkeyFoundAKeyboard() {
        List<KeyCode> keyCodes = createAwfulList();

        List<Boolean> actual = new ArrayList<>();

        for (KeyCode key : keyCodes) {
            actual.add(tutorial.checkIfProgressed(key));
        }

        List<Boolean> expected = createAwfulExpected();

        Assert.assertEquals(expected,actual);
    }

    private List<KeyCode> createAwfulList(){

        List<KeyCode> keyCodes = new ArrayList<>();
        keyCodes.add(KeyCode.G);//F
        keyCodes.add(KeyCode.S);//F
        keyCodes.add(KeyCode.B);//F
        keyCodes.add(KeyCode.G);//F
        keyCodes.add(KeyCode.S);//F
        keyCodes.add(KeyCode.NUMPAD6);//F
        keyCodes.add(KeyCode.ENTER);//F
        keyCodes.add(KeyCode.SHIFT);//T
        keyCodes.add(KeyCode.ENTER);//F
        keyCodes.add(KeyCode.PAGE_DOWN);//F
        keyCodes.add(KeyCode.ENTER);//F
        keyCodes.add(KeyCode.Y);//F
        keyCodes.add(KeyCode.C);//F
        keyCodes.add(KeyCode.SPACE);//F
        keyCodes.add(KeyCode.X);//F
        keyCodes.add(KeyCode.PAGE_UP);//T
        keyCodes.add(KeyCode.U);//F
        keyCodes.add(KeyCode.SHIFT);//F
        keyCodes.add(KeyCode.DOWN);//T
        keyCodes.add(KeyCode.UP);//F
        keyCodes.add(KeyCode.NUMPAD6);//F
        keyCodes.add(KeyCode.ENTER);//F
        keyCodes.add(KeyCode.SHIFT);//F
        keyCodes.add(KeyCode.ENTER);//F
        keyCodes.add(KeyCode.PAGE_DOWN);//F
        keyCodes.add(KeyCode.ENTER);//F
        keyCodes.add(KeyCode.Y);//F
        keyCodes.add(KeyCode.C);//F
        keyCodes.add(KeyCode.SPACE);//T
        keyCodes.add(KeyCode.X);//F
        keyCodes.add(KeyCode.G);//F
        keyCodes.add(KeyCode.S);//F
        keyCodes.add(KeyCode.B);//F
        keyCodes.add(KeyCode.G);//F
        keyCodes.add(KeyCode.S);//F
        keyCodes.add(KeyCode.NUMPAD6);//F
        keyCodes.add(KeyCode.ENTER);//T
        keyCodes.add(KeyCode.SHIFT);//F
        keyCodes.add(KeyCode.SPACE);//F
        keyCodes.add(KeyCode.PAGE_DOWN);//F
        keyCodes.add(KeyCode.ENTER);//T
        keyCodes.add(KeyCode.Y);//F
        keyCodes.add(KeyCode.C);//F
        keyCodes.add(KeyCode.SPACE);//F
        keyCodes.add(KeyCode.SHIFT);//T

        return keyCodes;
    }

    private  List<Boolean> createAwfulExpected(){
        List<Boolean> expected = new ArrayList<>();
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(true);

        return expected;
    }
}
