package seng302.team18.visualiser.messageinterpreting;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.messageparsing.AC35RaceStatusMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.visualiser.display.RaceClock;

import java.util.HashMap;

/**
 * Created by david on 4/28/17.
 */
public class RaceClockInterpreterTest {
    private RaceClock raceClock;
    private MessageInterpreter interpreter;
    private MessageBody message;
    private Label label;

    public static class AsNonApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // noop
        }
    }

    @Before
    public void setUp() {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(AsNonApp.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();

        label = new Label("");
        raceClock = new RaceClock(label);
        interpreter = new RaceClockInterpreter(raceClock);
        message = new AC35RaceStatusMessage(12000, 0, 2000, 0, new HashMap<>());
    }


    @Test
    public void interpretTest() {
        interpreter.interpret(message);
        RaceClock expected = new RaceClock(label);
        expected.setTime(10L);
        Assert.assertEquals(expected, raceClock);
        Assert.assertEquals("", label.getText());
    }

}
