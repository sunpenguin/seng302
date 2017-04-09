package seng302.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jds112 on 9/04/17.
 */
public class LiveDataListenerTest {
    private LiveDataListener liveDataListener;

    @Before
    public void setUp() {
        liveDataListener = new LiveDataListener(4941);
    }


    @Test
    public void runTest() throws Exception {
        assertEquals("The current line was not empty before setting up a connection", "", liveDataListener.getCurrentline());

        Thread testInputStream = new Thread() {
            @Override
            public void run() {
                while (liveDataListener.isKeepRunning()) {
                    liveDataListener.run();
                }
            }
        };

        testInputStream.start();
        testInputStream.sleep(250);
        assertEquals("The current line was empty after setting up a connection", false, liveDataListener.getCurrentline().isEmpty());
        liveDataListener.setKeepRunning(false);

    }


    @After
    public void tearDown() throws Exception {
        liveDataListener.getSocket().close();
    }
}