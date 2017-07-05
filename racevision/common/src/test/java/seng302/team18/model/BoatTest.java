package seng302.team18.model;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A test class for the boat class
 */
public class BoatTest {
    
    Boat testBoat;

    //BoatHeadings
    double BoatHeadingN = 0;
    double BoatHeadingNE = 30;
    double BoatHeadingE = 90;
    double BoatHeadingSE = 135;
    double BoatHeadingS = 180;
    double BoatHeadingSW = 235;
    double BoatHeadingW = 270;
    double BoatHeadingNW = 300;
    double BoatHeadingN2 = 360;

    //WindDirections
    double windDirectionN = 0;
    double windDirectionNE = 45;
    double windDirectionE = 90;
    double windDirectionSE = 120;
    double windDirectionS = 180;
    double windDirectionSW = 220;
    double windDirectionW = 270;
    double windDirectionNW = 315;
    double windDirectionN2 = 360;
    
    @Before
    public void setUp(){
        testBoat = new Boat("Enterprise", "Starfleet", 10);
    }

    @Test
    public void testBoatName() {
        assertEquals("Enterprise", testBoat.getName());
    }

    @Test
    public void getTrueWindAngleTestNorthWind() {
        //GetAnswers For Wind Direction North
        testBoat.setHeading(BoatHeadingN);
        double WNBN = testBoat.getTrueWindAngle(windDirectionN); //WNBN = Wind North Boat North
        testBoat.setHeading(BoatHeadingNE);
        double WNBNE = testBoat.getTrueWindAngle(windDirectionN);
        double WNBE = testBoat.getTrueWindAngle(windDirectionN, BoatHeadingE);
        double WNBSE = testBoat.getTrueWindAngle(windDirectionN, BoatHeadingSE);
        double WNBS = testBoat.getTrueWindAngle(windDirectionN, BoatHeadingS);
        double WNBSW = testBoat.getTrueWindAngle(windDirectionN, BoatHeadingSW);
        double WNBW = testBoat.getTrueWindAngle(windDirectionN, BoatHeadingW);
        double WNBNW = testBoat.getTrueWindAngle(windDirectionN, BoatHeadingNW);
        double WNBN2 = testBoat.getTrueWindAngle(windDirectionN, BoatHeadingN2);

        //Test Answers for wind direction North
        TestCase.assertEquals(180, WNBN, 0);
        TestCase.assertEquals(150, WNBNE, 0);
        TestCase.assertEquals(90, WNBE, 0);
        TestCase.assertEquals(45, WNBSE, 0);
        TestCase.assertEquals(0, WNBS, 0);
        TestCase.assertEquals(55, WNBSW, 0);
        TestCase.assertEquals(90, WNBW, 0);
        TestCase.assertEquals(120, WNBNW, 0);
        TestCase.assertEquals(180, WNBN2, 0);
    }


    @Test
    public void getTrueWindAngleTestNorthEastWind() {
        //GetAnswers For Wind Direction North East
        testBoat.setHeading(BoatHeadingN);
        double WNEBN = testBoat.getTrueWindAngle(windDirectionNE); //WNEBN = Wind North East Boat North
        testBoat.setHeading(BoatHeadingNE);
        double WNEBNE = testBoat.getTrueWindAngle(windDirectionNE);
        double WNEBE = testBoat.getTrueWindAngle(windDirectionNE, BoatHeadingE);
        double WNEBSE = testBoat.getTrueWindAngle(windDirectionNE, BoatHeadingSE);
        double WNEBS = testBoat.getTrueWindAngle(windDirectionNE, BoatHeadingS);
        double WNEBSW = testBoat.getTrueWindAngle(windDirectionNE, BoatHeadingSW);
        double WNEBW = testBoat.getTrueWindAngle(windDirectionNE, BoatHeadingW);
        double WNEBNW = testBoat.getTrueWindAngle(windDirectionNE, BoatHeadingNW);
        double WNEBN2 = testBoat.getTrueWindAngle(windDirectionNE, BoatHeadingN2);

        //Test Answers for wind direction North East
        TestCase.assertEquals(135, WNEBN, 0);
        TestCase.assertEquals(165, WNEBNE, 0);
        TestCase.assertEquals(135, WNEBE, 0);
        TestCase.assertEquals(90, WNEBSE, 0);
        TestCase.assertEquals(45, WNEBS, 0);
        TestCase.assertEquals(10, WNEBSW, 0);
        TestCase.assertEquals(45, WNEBW, 0);
        TestCase.assertEquals(75, WNEBNW, 0);
        TestCase.assertEquals(135, WNEBN2, 0);
    }


    @Test
    public void getTrueWindAngleTestEastWind() {
        //GetAnswers For Wind Direction East
        testBoat.setHeading(BoatHeadingN);
        double WEBN = testBoat.getTrueWindAngle(windDirectionE); //WEBN = Wind East Boat North
        testBoat.setHeading(BoatHeadingNE);
        double WEBNE = testBoat.getTrueWindAngle(windDirectionE);
        double WEBE = testBoat.getTrueWindAngle(windDirectionE, BoatHeadingE);
        double WEBSE = testBoat.getTrueWindAngle(windDirectionE, BoatHeadingSE);
        double WEBS = testBoat.getTrueWindAngle(windDirectionE, BoatHeadingS);
        double WEBSW = testBoat.getTrueWindAngle(windDirectionE, BoatHeadingSW);
        double WEBW = testBoat.getTrueWindAngle(windDirectionE, BoatHeadingW);
        double WEBNW = testBoat.getTrueWindAngle(windDirectionE, BoatHeadingNW);
        double WEBN2 = testBoat.getTrueWindAngle(windDirectionE, BoatHeadingN2);

        //Test Answers for wind direction East
        TestCase.assertEquals(90, WEBN, 0);
        TestCase.assertEquals(120, WEBNE, 0);
        TestCase.assertEquals(180, WEBE, 0);
        TestCase.assertEquals(135, WEBSE, 0);
        TestCase.assertEquals(90, WEBS, 0);
        TestCase.assertEquals(35, WEBSW, 0);
        TestCase.assertEquals(0, WEBW, 0);
        TestCase.assertEquals(30, WEBNW, 0);
        TestCase.assertEquals(90, WEBN2, 0);
    }


    @Test
    public void getTrueWindAngleTestSouthEastWind() {
        //GetAnswers For Wind Direction South East
        testBoat.setHeading(BoatHeadingN);
        double WSEBN = testBoat.getTrueWindAngle(windDirectionSE); //WSEBN = Wind South East Boat North
        testBoat.setHeading(BoatHeadingNE);
        double WSEBNE = testBoat.getTrueWindAngle(windDirectionSE);
        double WSEBE = testBoat.getTrueWindAngle(windDirectionSE, BoatHeadingE);
        double WSEBSE = testBoat.getTrueWindAngle(windDirectionSE, BoatHeadingSE);
        double WSEBS = testBoat.getTrueWindAngle(windDirectionSE, BoatHeadingS);
        double WSEBSW = testBoat.getTrueWindAngle(windDirectionSE, BoatHeadingSW);
        double WSEBW = testBoat.getTrueWindAngle(windDirectionSE, BoatHeadingW);
        double WSEBNW = testBoat.getTrueWindAngle(windDirectionSE, BoatHeadingNW);
        double WSEBN2 = testBoat.getTrueWindAngle(windDirectionSE, BoatHeadingN2);

        //Test Answers for wind direction South East
        TestCase.assertEquals(60, WSEBN, 0);
        TestCase.assertEquals(90, WSEBNE, 0);
        TestCase.assertEquals(150, WSEBE, 0);
        TestCase.assertEquals(165, WSEBSE, 0);
        TestCase.assertEquals(120, WSEBS, 0);
        TestCase.assertEquals(65, WSEBSW, 0);
        TestCase.assertEquals(30, WSEBW, 0);
        TestCase.assertEquals(0, WSEBNW, 0);
        TestCase.assertEquals(60, WSEBN2, 0);
    }


    @Test
    public void getTrueWindAngleTestSouthWind() {
        //GetAnswers For Wind Direction South
        testBoat.setHeading(BoatHeadingN);
        double WSBN = testBoat.getTrueWindAngle(windDirectionS); //WSBN = Wind South Boat North
        testBoat.setHeading(BoatHeadingNE);
        double WSBNE = testBoat.getTrueWindAngle(windDirectionS);
        double WSBE = testBoat.getTrueWindAngle(windDirectionS, BoatHeadingE);
        double WSBSE = testBoat.getTrueWindAngle(windDirectionS, BoatHeadingSE);
        double WSBS = testBoat.getTrueWindAngle(windDirectionS, BoatHeadingS);
        double WSBSW = testBoat.getTrueWindAngle(windDirectionS, BoatHeadingSW);
        double WSBW = testBoat.getTrueWindAngle(windDirectionS, BoatHeadingW);
        double WSBNW = testBoat.getTrueWindAngle(windDirectionS, BoatHeadingNW);
        double WSBN2 = testBoat.getTrueWindAngle(windDirectionS, BoatHeadingN2);

        //Test Answers for wind direction South
        TestCase.assertEquals(0, WSBN, 0);
        TestCase.assertEquals(30, WSBNE, 0);
        TestCase.assertEquals(90, WSBE, 0);
        TestCase.assertEquals(135, WSBSE, 0);
        TestCase.assertEquals(180, WSBS, 0);
        TestCase.assertEquals(125, WSBSW, 0);
        TestCase.assertEquals(90, WSBW, 0);
        TestCase.assertEquals(60, WSBNW, 0);
        TestCase.assertEquals(0, WSBN2, 0);
    }


    @Test
    public void getTrueWindAngleTestSouthWestWind() {
        //GetAnswers For Wind Direction South West
        testBoat.setHeading(BoatHeadingN);
        double WSWBN = testBoat.getTrueWindAngle(windDirectionSW); //WSWBN = Wind South West Boat North
        testBoat.setHeading(BoatHeadingNE);
        double WSWBNE = testBoat.getTrueWindAngle(windDirectionSW);
        double WSWBE = testBoat.getTrueWindAngle(windDirectionSW, BoatHeadingE);
        double WSWBSE = testBoat.getTrueWindAngle(windDirectionSW, BoatHeadingSE);
        double WSWBS = testBoat.getTrueWindAngle(windDirectionSW, BoatHeadingS);
        double WSWBSW = testBoat.getTrueWindAngle(windDirectionSW, BoatHeadingSW);
        double WSWBW = testBoat.getTrueWindAngle(windDirectionSW, BoatHeadingW);
        double WSWBNW = testBoat.getTrueWindAngle(windDirectionSW, BoatHeadingNW);
        double WSWBN2 = testBoat.getTrueWindAngle(windDirectionSW, BoatHeadingN2);

        //Test Answers for wind direction South West
        TestCase.assertEquals(40, WSWBN, 0);
        TestCase.assertEquals(10, WSWBNE, 0);
        TestCase.assertEquals(50, WSWBE, 0);
        TestCase.assertEquals(95, WSWBSE, 0);
        TestCase.assertEquals(140, WSWBS, 0);
        TestCase.assertEquals(165, WSWBSW, 0);
        TestCase.assertEquals(130, WSWBW, 0);
        TestCase.assertEquals(100, WSWBNW, 0);
        TestCase.assertEquals(40, WSWBN2, 0);

    }


    @Test
    public void getTrueWindAngleTestWestWind() {
        //GetAnswers For Wind Direction West
        testBoat.setHeading(BoatHeadingN);
        double WWBN = testBoat.getTrueWindAngle(windDirectionW); //WWBN = Wind West Boat North
        testBoat.setHeading(BoatHeadingNE);
        double WWBNE = testBoat.getTrueWindAngle(windDirectionW);
        double WWBE = testBoat.getTrueWindAngle(windDirectionW, BoatHeadingE);
        double WWBSE = testBoat.getTrueWindAngle(windDirectionW, BoatHeadingSE);
        double WWBS = testBoat.getTrueWindAngle(windDirectionW, BoatHeadingS);
        double WWBSW = testBoat.getTrueWindAngle(windDirectionW, BoatHeadingSW);
        double WWBW = testBoat.getTrueWindAngle(windDirectionW, BoatHeadingW);
        double WWBNW = testBoat.getTrueWindAngle(windDirectionW, BoatHeadingNW);
        double WWBN2 = testBoat.getTrueWindAngle(windDirectionW, BoatHeadingN2);

        //Test Answers for wind direction West
        TestCase.assertEquals(90, WWBN, 0);
        TestCase.assertEquals(60, WWBNE, 0);
        TestCase.assertEquals(0, WWBE, 0);
        TestCase.assertEquals(45, WWBSE, 0);
        TestCase.assertEquals(90, WWBS, 0);
        TestCase.assertEquals(145, WWBSW, 0);
        TestCase.assertEquals(180, WWBW, 0);
        TestCase.assertEquals(150, WWBNW, 0);
        TestCase.assertEquals(90, WWBN2, 0);
    }


    @Test
    public void getTrueWindAngleTestNorthWestWind() {
        //GetAnswers For Wind Direction North West
        testBoat.setHeading(BoatHeadingN);
        double WNWBN = testBoat.getTrueWindAngle(windDirectionNW); //WNWBN = Wind North West Boat North
        testBoat.setHeading(BoatHeadingNE);
        double WNWBNE = testBoat.getTrueWindAngle(windDirectionNW);
        double WNWBE = testBoat.getTrueWindAngle(windDirectionNW, BoatHeadingE);
        double WNWBSE = testBoat.getTrueWindAngle(windDirectionNW, BoatHeadingSE);
        double WNWBS = testBoat.getTrueWindAngle(windDirectionNW, BoatHeadingS);
        double WNWBSW = testBoat.getTrueWindAngle(windDirectionNW, BoatHeadingSW);
        double WNWBW = testBoat.getTrueWindAngle(windDirectionNW, BoatHeadingW);
        double WNWBNW = testBoat.getTrueWindAngle(windDirectionNW, BoatHeadingNW);
        double WNWBN2 = testBoat.getTrueWindAngle(windDirectionNW, BoatHeadingN2);

        //Test Answers for wind direction North West
        TestCase.assertEquals(135, WNWBN, 0);
        TestCase.assertEquals(105, WNWBNE, 0);
        TestCase.assertEquals(45, WNWBE, 0);
        TestCase.assertEquals(0, WNWBSE, 0);
        TestCase.assertEquals(45, WNWBS, 0);
        TestCase.assertEquals(100, WNWBSW, 0);
        TestCase.assertEquals(135, WNWBW, 0);
        TestCase.assertEquals(165, WNWBNW, 0);
        TestCase.assertEquals(135, WNWBN2, 0);
    }


    @Test
    public void getTrueWindAngleTestNorthWind2(){
        //GetAnswers For Wind Direction North2
        testBoat.setHeading(BoatHeadingN);
        double WN2BN = testBoat.getTrueWindAngle(windDirectionN2); //WN2BN = Wind North2 Boat North
        testBoat.setHeading(BoatHeadingNE);
        double WN2BNE = testBoat.getTrueWindAngle(windDirectionN2, BoatHeadingNE);
        double WN2BE = testBoat.getTrueWindAngle(windDirectionN2, BoatHeadingE);
        double WN2BSE = testBoat.getTrueWindAngle(windDirectionN2, BoatHeadingSE);
        double WN2BS = testBoat.getTrueWindAngle(windDirectionN2, BoatHeadingS);
        double WN2BSW = testBoat.getTrueWindAngle(windDirectionN2, BoatHeadingSW);
        double WN2BW = testBoat.getTrueWindAngle(windDirectionN2, BoatHeadingW);
        double WN2BNW = testBoat.getTrueWindAngle(windDirectionN2, BoatHeadingNW);
        double WN2BN2 = testBoat.getTrueWindAngle(windDirectionN2, BoatHeadingN2);

        //Test Answers for wind direction North2
        TestCase.assertEquals(180, WN2BN, 0);
        TestCase.assertEquals(150, WN2BNE, 0);
        TestCase.assertEquals(90, WN2BE, 0);
        TestCase.assertEquals(45, WN2BSE, 0);
        TestCase.assertEquals(0, WN2BS, 0);
        TestCase.assertEquals(55, WN2BSW, 0);
        TestCase.assertEquals(90, WN2BW, 0);
        TestCase.assertEquals(120, WN2BNW, 0);
        TestCase.assertEquals(180, WN2BN2, 0);
    }


}
