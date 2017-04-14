package seng302.team18.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by jds112 on 9/04/17.
 */
public class LiveDataListenerTest {
    private LiveDataListener liveDataListener;

    @Before
    public void setUp() {
        try {
            liveDataListener = new LiveDataListener(4941, new AC35MessageParserFactory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runTest() throws Exception {
        int x = 0;
        boolean X = true;
        while(X) {
            liveDataListener.nextMessage();
            x ++;
            if (x == 100000000 - 1) {
                X = false;
            }
        }
    }


//    @After
//    public void tearDown() throws Exception {
//        liveDataListener.getSocket().close();
//    }
}