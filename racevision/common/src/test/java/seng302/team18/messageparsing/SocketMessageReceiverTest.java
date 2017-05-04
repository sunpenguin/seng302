//package seng302.team18.messageparsing;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//
///**
// * Created by jds112 on 9/04/17.
// */
//public class SocketMessageReceiverTest {
//    private SocketMessageReceiver socketMessageReceiver;
//
//    @Before
//    public void setUp() {
//        try {
//            socketMessageReceiver = new SocketMessageReceiver("livedata.americascup.com", 4941, new AC35MessageParserFactory());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    public void runTest() throws Exception {
//        int x = 0;
//        boolean X = true;
//        while (X) {
//            MessageBody message = socketMessageReceiver.nextMessage();
//            x++;
//            if (x == 100 - 1) {
//                X = false;
//            }
//            Thread.sleep(100);
//        }
//    }
//
//
//    @After
//    public void tearDown() throws Exception {
////        socketMessageReceiver.getSocket().close();
//    }
//}


/* todo find a way around the firewall on the CI machine to enable this test. */