//package seng302.team18.messageparsing;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
///**
// * Created by jds112 on 26/04/17.
// */
//public class AC35XMLMessageParserTest {
//
//    private AC35XMLMessageParser parser;
//    byte[] yachtHeaderBytes;
//    byte[] boatLocationHeaderBytes;
//    int yachtEvent;
//    int boatLocation;
//
//    @Before
//    public void setUp() throws Exception {
//        byte[] bytes = {71, -125, 29, 113, 80, 107, 28, 83, 1, -54, 0, 0, 0, 63, 0};
//        yachtHeaderBytes = bytes;
//        byte[] bytes2 = {71, -125, 37, 113, 80, 107, 28, 83, 1, -54, 0, 0, 0, 63, 0};
//        boatLocationHeaderBytes = bytes2;
//
//        yachtEvent = AC35MessageType.YACHT_EVENT.getCode();
//        boatLocation = AC35MessageType.BOAT_LOCATION.getCode();
//    }
//
//    @Test
//    public void parse() throws Exception {
//        AC35XMLMessageParser parser = new AC35XMLMessageParser(new AC35XMLParserFactory());
//        MessageBody yachtBodyParser = parser.parse(yachtHeaderBytes);
//        MessageBody boatBodyParser = parser.parse(boatLocationHeaderBytes);
//
//        boolean expectedYachtType = true;
//        boolean actualYachtType = (yachtBodyParser.getType() == 29);
//        boolean expectedYBoatType = true;
//        boolean actualBoatType = (boatBodyParser.getType() == 37);
//
//        assertEquals("The message parser did not create a parser of the correct type.", expectedYBoatType, actualBoatType);
//    }
//
//}