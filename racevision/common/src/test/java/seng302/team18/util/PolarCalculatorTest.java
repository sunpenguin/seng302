package seng302.team18.util;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.Polar;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Class to Test polar Calculator
 */
public class PolarCalculatorTest {
    PolarCalculator calculator;
    Polar polar1;
    Polar polar2;
    Polar polar3;

    @Before
    public void setUp(){
        //Make polar 1
        polar1 = new Polar(12, 43, 14.4, 153,21.6);
        polar1.addToMap(0,	0);
        polar1.addToMap(30,11);
        polar1.addToMap(60,16);
        polar1.addToMap(75,20);
        polar1.addToMap(90,23);
        polar1.addToMap(115,24);
        polar1.addToMap(145,23);
        polar1.addToMap(175,14);

        //Make polar 2
        polar2 = new Polar(25, 40,30, 151,47);
        polar2.addToMap(0,0);
        polar2.addToMap(30,15);
        polar2.addToMap(60,38);
        polar2.addToMap(75,44);
        polar2.addToMap(90,49);
        polar2.addToMap(115,50);
        polar2.addToMap(145,49);
        polar2.addToMap(175,30);

        //Make polar 3
        polar3 = new Polar(30,42,30,150,46);
        polar3.addToMap(0,0);
        polar3.addToMap(30,15);
        polar3.addToMap(60,37);
        polar3.addToMap(75,42);
        polar3.addToMap(90,48);
        polar3.addToMap(115,49);
        polar3.addToMap(145,48);
        polar3.addToMap(175,32);


        List<Polar> polarList = new ArrayList<>();
        polarList.add(polar1);
        polarList.add(polar2);
        polarList.add(polar3);

        calculator = new PolarCalculator(polarList);

    }

    @After
    public void tearDown(){
        calculator = null;
        polar1 = null;
        polar2 = null;
        polar3 = null;
    }

    @Test
    public void getTrueWindAngleTest(){

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

        //GetAnswers For Wind Direction North
        double WNBN = calculator.getTrueWindAngle(windDirectionN, BoatHeadingN);
        double WNBNE = calculator.getTrueWindAngle(windDirectionN, BoatHeadingNE);
        double WNBE = calculator.getTrueWindAngle(windDirectionN, BoatHeadingE);
        double WNBSE = calculator.getTrueWindAngle(windDirectionN, BoatHeadingSE);
        double WNBS = calculator.getTrueWindAngle(windDirectionN, BoatHeadingS);
        double WNBSW = calculator.getTrueWindAngle(windDirectionN, BoatHeadingSW);
        double WNBW = calculator.getTrueWindAngle(windDirectionN, BoatHeadingW);
        double WNBNW = calculator.getTrueWindAngle(windDirectionN, BoatHeadingNW);
        double WNBN2 = calculator.getTrueWindAngle(windDirectionN, BoatHeadingN2);

        //Test Answers for wind direction North
        assertEquals(180, WNBN, 0);
        assertEquals(150, WNBNE, 0);
        assertEquals(90, WNBE, 0);
        assertEquals(45, WNBSE, 0);
        assertEquals(0, WNBS, 0);
        assertEquals(55, WNBSW, 0);
        assertEquals(90, WNBW, 0);
        assertEquals(120, WNBNW, 0);
        assertEquals(180, WNBN2, 0);

        //GetAnswers For Wind Direction North East
        double WNEBN = calculator.getTrueWindAngle(windDirectionNE, BoatHeadingN);
        double WNEBNE = calculator.getTrueWindAngle(windDirectionNE, BoatHeadingNE);
        double WNEBE = calculator.getTrueWindAngle(windDirectionNE, BoatHeadingE);
        double WNEBSE = calculator.getTrueWindAngle(windDirectionNE, BoatHeadingSE);
        double WNEBS = calculator.getTrueWindAngle(windDirectionNE, BoatHeadingS);
        double WNEBSW = calculator.getTrueWindAngle(windDirectionNE, BoatHeadingSW);
        double WNEBW = calculator.getTrueWindAngle(windDirectionNE, BoatHeadingW);
        double WNEBNW = calculator.getTrueWindAngle(windDirectionNE, BoatHeadingNW);
        double WNEBN2 = calculator.getTrueWindAngle(windDirectionNE, BoatHeadingN2);

        //Test Answers for wind direction North East
        assertEquals(135, WNEBN, 0);
        assertEquals(165, WNEBNE, 0);
        assertEquals(135, WNEBE, 0);
        assertEquals(90, WNEBSE, 0);
        assertEquals(45, WNEBS, 0);
        assertEquals(10, WNEBSW, 0);
        assertEquals(45, WNEBW, 0);
        assertEquals(75, WNEBNW, 0);
        assertEquals(135, WNEBN2, 0);

        //GetAnswers For Wind Direction East
        double WEBN = calculator.getTrueWindAngle(windDirectionE, BoatHeadingN);
        double WEBNE = calculator.getTrueWindAngle(windDirectionE, BoatHeadingNE);
        double WEBE = calculator.getTrueWindAngle(windDirectionE, BoatHeadingE);
        double WEBSE = calculator.getTrueWindAngle(windDirectionE, BoatHeadingSE);
        double WEBS = calculator.getTrueWindAngle(windDirectionE, BoatHeadingS);
        double WEBSW = calculator.getTrueWindAngle(windDirectionE, BoatHeadingSW);
        double WEBW = calculator.getTrueWindAngle(windDirectionE, BoatHeadingW);
        double WEBNW = calculator.getTrueWindAngle(windDirectionE, BoatHeadingNW);
        double WEBN2 = calculator.getTrueWindAngle(windDirectionE, BoatHeadingN2);

        //Test Answers for wind direction East
        assertEquals(90, WEBN, 0);
        assertEquals(120, WEBNE, 0);
        assertEquals(180, WEBE, 0);
        assertEquals(135, WEBSE, 0);
        assertEquals(90, WEBS, 0);
        assertEquals(35, WEBSW, 0);
        assertEquals(0, WEBW, 0);
        assertEquals(30, WEBNW, 0);
        assertEquals(90, WEBN2, 0);

        //GetAnswers For Wind Direction South East
        double WSEBN = calculator.getTrueWindAngle(windDirectionSE, BoatHeadingN);
        double WSEBNE = calculator.getTrueWindAngle(windDirectionSE, BoatHeadingNE);
        double WSEBE = calculator.getTrueWindAngle(windDirectionSE, BoatHeadingE);
        double WSEBSE = calculator.getTrueWindAngle(windDirectionSE, BoatHeadingSE);
        double WSEBS = calculator.getTrueWindAngle(windDirectionSE, BoatHeadingS);
        double WSEBSW = calculator.getTrueWindAngle(windDirectionSE, BoatHeadingSW);
        double WSEBW = calculator.getTrueWindAngle(windDirectionSE, BoatHeadingW);
        double WSEBNW = calculator.getTrueWindAngle(windDirectionSE, BoatHeadingNW);
        double WSEBN2 = calculator.getTrueWindAngle(windDirectionSE, BoatHeadingN2);

        //Test Answers for wind direction South East
        assertEquals(60, WSEBN, 0);
        assertEquals(90, WSEBNE, 0);
        assertEquals(150, WSEBE, 0);
        assertEquals(165, WSEBSE, 0);
        assertEquals(120, WSEBS, 0);
        assertEquals(65, WSEBSW, 0);
        assertEquals(30, WSEBW, 0);
        assertEquals(0, WSEBNW, 0);
        assertEquals(60, WSEBN2, 0);

        //GetAnswers For Wind Direction South
        double WSBN = calculator.getTrueWindAngle(windDirectionS, BoatHeadingN);
        double WSBNE = calculator.getTrueWindAngle(windDirectionS, BoatHeadingNE);
        double WSBE = calculator.getTrueWindAngle(windDirectionS, BoatHeadingE);
        double WSBSE = calculator.getTrueWindAngle(windDirectionS, BoatHeadingSE);
        double WSBS = calculator.getTrueWindAngle(windDirectionS, BoatHeadingS);
        double WSBSW = calculator.getTrueWindAngle(windDirectionS, BoatHeadingSW);
        double WSBW = calculator.getTrueWindAngle(windDirectionS, BoatHeadingW);
        double WSBNW = calculator.getTrueWindAngle(windDirectionS, BoatHeadingNW);
        double WSBN2 = calculator.getTrueWindAngle(windDirectionS, BoatHeadingN2);

        //Test Answers for wind direction South
        assertEquals(0, WSBN, 0);
        assertEquals(30, WSBNE, 0);
        assertEquals(90, WSBE, 0);
        assertEquals(135, WSBSE, 0);
        assertEquals(180, WSBS, 0);
        assertEquals(125, WSBSW, 0);
        assertEquals(90, WSBW, 0);
        assertEquals(60, WSBNW, 0);
        assertEquals(0, WSBN2, 0);

        //GetAnswers For Wind Direction South West
        double WSWBN = calculator.getTrueWindAngle(windDirectionSW, BoatHeadingN);
        double WSWBNE = calculator.getTrueWindAngle(windDirectionSW, BoatHeadingNE);
        double WSWBE = calculator.getTrueWindAngle(windDirectionSW, BoatHeadingE);
        double WSWBSE = calculator.getTrueWindAngle(windDirectionSW, BoatHeadingSE);
        double WSWBS = calculator.getTrueWindAngle(windDirectionSW, BoatHeadingS);
        double WSWBSW = calculator.getTrueWindAngle(windDirectionSW, BoatHeadingSW);
        double WSWBW = calculator.getTrueWindAngle(windDirectionSW, BoatHeadingW);
        double WSWBNW = calculator.getTrueWindAngle(windDirectionSW, BoatHeadingNW);
        double WSWBN2 = calculator.getTrueWindAngle(windDirectionSW, BoatHeadingN2);

        //Test Answers for wind direction South West
        assertEquals(40, WSWBN, 0);
        assertEquals(10, WSWBNE, 0);
        assertEquals(50, WSWBE, 0);
        assertEquals(95, WSWBSE, 0);
        assertEquals(140, WSWBS, 0);
        assertEquals(165, WSWBSW, 0);
        assertEquals(130, WSWBW, 0);
        assertEquals(100, WSWBNW, 0);
        assertEquals(40, WSWBN2, 0);

        //GetAnswers For Wind Direction West
        double WWBN = calculator.getTrueWindAngle(windDirectionW, BoatHeadingN);
        double WWBNE = calculator.getTrueWindAngle(windDirectionW, BoatHeadingNE);
        double WWBE = calculator.getTrueWindAngle(windDirectionW, BoatHeadingE);
        double WWBSE = calculator.getTrueWindAngle(windDirectionW, BoatHeadingSE);
        double WWBS = calculator.getTrueWindAngle(windDirectionW, BoatHeadingS);
        double WWBSW = calculator.getTrueWindAngle(windDirectionW, BoatHeadingSW);
        double WWBW = calculator.getTrueWindAngle(windDirectionW, BoatHeadingW);
        double WWBNW = calculator.getTrueWindAngle(windDirectionW, BoatHeadingNW);
        double WWBN2 = calculator.getTrueWindAngle(windDirectionW, BoatHeadingN2);

        //Test Answers for wind direction West
        assertEquals(90, WWBN, 0);
        assertEquals(60, WWBNE, 0);
        assertEquals(0, WWBE, 0);
        assertEquals(45, WWBSE, 0);
        assertEquals(90, WWBS, 0);
        assertEquals(145, WWBSW, 0);
        assertEquals(180, WWBW, 0);
        assertEquals(150, WWBNW, 0);
        assertEquals(90, WWBN2, 0);

        //GetAnswers For Wind Direction North West
        double WNWBN = calculator.getTrueWindAngle(windDirectionNW, BoatHeadingN);
        double WNWBNE = calculator.getTrueWindAngle(windDirectionNW, BoatHeadingNE);
        double WNWBE = calculator.getTrueWindAngle(windDirectionNW, BoatHeadingE);
        double WNWBSE = calculator.getTrueWindAngle(windDirectionNW, BoatHeadingSE);
        double WNWBS = calculator.getTrueWindAngle(windDirectionNW, BoatHeadingS);
        double WNWBSW = calculator.getTrueWindAngle(windDirectionNW, BoatHeadingSW);
        double WNWBW = calculator.getTrueWindAngle(windDirectionNW, BoatHeadingW);
        double WNWBNW = calculator.getTrueWindAngle(windDirectionNW, BoatHeadingNW);
        double WNWBN2 = calculator.getTrueWindAngle(windDirectionNW, BoatHeadingN2);

        //Test Answers for wind direction North West
        assertEquals(135, WNWBN, 0);
        assertEquals(105, WNWBNE, 0);
        assertEquals(45, WNWBE, 0);
        assertEquals(0, WNWBSE, 0);
        assertEquals(45, WNWBS, 0);
        assertEquals(100, WNWBSW, 0);
        assertEquals(135, WNWBW, 0);
        assertEquals(165, WNWBNW, 0);
        assertEquals(135, WNWBN2, 0);

        //GetAnswers For Wind Direction North2
        double WN2BN = calculator.getTrueWindAngle(windDirectionN2, BoatHeadingN);
        double WN2BNE = calculator.getTrueWindAngle(windDirectionN2, BoatHeadingNE);
        double WN2BE = calculator.getTrueWindAngle(windDirectionN2, BoatHeadingE);
        double WN2BSE = calculator.getTrueWindAngle(windDirectionN2, BoatHeadingSE);
        double WN2BS = calculator.getTrueWindAngle(windDirectionN2, BoatHeadingS);
        double WN2BSW = calculator.getTrueWindAngle(windDirectionN2, BoatHeadingSW);
        double WN2BW = calculator.getTrueWindAngle(windDirectionN2, BoatHeadingW);
        double WN2BNW = calculator.getTrueWindAngle(windDirectionN2, BoatHeadingNW);
        double WN2BN2 = calculator.getTrueWindAngle(windDirectionN2, BoatHeadingN2);

        //Test Answers for wind direction North2
        assertEquals(180, WN2BN, 0);
        assertEquals(150, WN2BNE, 0);
        assertEquals(90, WN2BE, 0);
        assertEquals(45, WN2BSE, 0);
        assertEquals(0, WN2BS, 0);
        assertEquals(55, WN2BSW, 0);
        assertEquals(90, WN2BW, 0);
        assertEquals(120, WN2BNW, 0);
        assertEquals(180, WN2BN2, 0);

    }

    @Test
    public void getPolarForWindSpeedTest(){
        //Create Windspeeds to test with
        //windspeeds Closest to polar1
        double windspeed1 = 10;
        double windspeed2 = 12;
        double windspeed3 = 14;
        //windspeed in middle of polar1 & polar2
        double windspeed4 = 18.5;
        //windspeeds closest to polar 2
        double windspeed5 = 20;
        double windspeed6 = 25;
        double windspeed7 = 27;
        //windspeed in middle of polar2 & polar3
        double windspeed8 = 27.5;
        //windspeeds closest to polar 3
        double windspeed9 = 29;
        double windspeed10 = 30;
        double windspeed11 = 33;

        Polar returned1 = calculator.getPolarForWindSpeed(windspeed1);
        Polar returned2 = calculator.getPolarForWindSpeed(windspeed2);
        Polar returned3 = calculator.getPolarForWindSpeed(windspeed3);
        Polar returned4 = calculator.getPolarForWindSpeed(windspeed4);
        Polar returned5 = calculator.getPolarForWindSpeed(windspeed5);
        Polar returned6 = calculator.getPolarForWindSpeed(windspeed6);
        Polar returned7 = calculator.getPolarForWindSpeed(windspeed7);
        Polar returned8 = calculator.getPolarForWindSpeed(windspeed8);
        Polar returned9 = calculator.getPolarForWindSpeed(windspeed9);
        Polar returned10 = calculator.getPolarForWindSpeed(windspeed10);
        Polar returned11 = calculator.getPolarForWindSpeed(windspeed11);

        //Should equal polar1
        assertEquals(polar1, returned1);
        assertEquals(polar1, returned2);
        assertEquals(polar1, returned3);
        assertEquals(polar1, returned4);

        //Should equal polar2
        assertEquals(polar2, returned5);
        assertEquals(polar2, returned6);
        assertEquals(polar2, returned7);
        assertEquals(polar2, returned8);

        //Should equal polar3
        assertEquals(polar3, returned9);
        assertEquals(polar3, returned10);
        assertEquals(polar3, returned11);
    }

}
