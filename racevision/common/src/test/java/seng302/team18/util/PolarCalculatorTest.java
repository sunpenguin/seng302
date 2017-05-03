package seng302.team18.util;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by sbe67 on 3/05/17.
 */
public class PolarCalculatorTest {

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
        double WNBN = PolarCalculator.getTrueWindAngle(windDirectionN, BoatHeadingN);
        double WNBNE = PolarCalculator.getTrueWindAngle(windDirectionN, BoatHeadingNE);
        double WNBE = PolarCalculator.getTrueWindAngle(windDirectionN, BoatHeadingE);
        double WNBSE = PolarCalculator.getTrueWindAngle(windDirectionN, BoatHeadingSE);
        double WNBS = PolarCalculator.getTrueWindAngle(windDirectionN, BoatHeadingS);
        double WNBSW = PolarCalculator.getTrueWindAngle(windDirectionN, BoatHeadingSW);
        double WNBW = PolarCalculator.getTrueWindAngle(windDirectionN, BoatHeadingW);
        double WNBNW = PolarCalculator.getTrueWindAngle(windDirectionN, BoatHeadingNW);
        double WNBN2 = PolarCalculator.getTrueWindAngle(windDirectionN, BoatHeadingN2);

        //Test Answers for wind direction North
        assertEquals(0.0, WNBN, 0);
        assertEquals(30, WNBNE, 0);
        assertEquals(90, WNBE, 0);
        assertEquals(135, WNBSE, 0);
        assertEquals(180, WNBS, 0);
        assertEquals(125, WNBSW, 0);
        assertEquals(90, WNBW, 0);
        assertEquals(60, WNBNW, 0);
        assertEquals(0, WNBN2, 0);

        //GetAnswers For Wind Direction North East
        double WNEBN = PolarCalculator.getTrueWindAngle(windDirectionNE, BoatHeadingN);
        double WNEBNE = PolarCalculator.getTrueWindAngle(windDirectionNE, BoatHeadingNE);
        double WNEBE = PolarCalculator.getTrueWindAngle(windDirectionNE, BoatHeadingE);
        double WNEBSE = PolarCalculator.getTrueWindAngle(windDirectionNE, BoatHeadingSE);
        double WNEBS = PolarCalculator.getTrueWindAngle(windDirectionNE, BoatHeadingS);
        double WNEBSW = PolarCalculator.getTrueWindAngle(windDirectionNE, BoatHeadingSW);
        double WNEBW = PolarCalculator.getTrueWindAngle(windDirectionNE, BoatHeadingW);
        double WNEBNW = PolarCalculator.getTrueWindAngle(windDirectionNE, BoatHeadingNW);
        double WNEBN2 = PolarCalculator.getTrueWindAngle(windDirectionNE, BoatHeadingN2);

        //Test Answers for wind direction North East
        assertEquals(45, WNEBN, 0);
        assertEquals(15, WNEBNE, 0);
        assertEquals(45, WNEBE, 0);
        assertEquals(90, WNEBSE, 0);
        assertEquals(135, WNEBS, 0);
        assertEquals(170, WNEBSW, 0);
        assertEquals(135, WNEBW, 0);
        assertEquals(105, WNEBNW, 0);
        assertEquals(45, WNEBN2, 0);

        //GetAnswers For Wind Direction East
        double WEBN = PolarCalculator.getTrueWindAngle(windDirectionE, BoatHeadingN);
        double WEBNE = PolarCalculator.getTrueWindAngle(windDirectionE, BoatHeadingNE);
        double WEBE = PolarCalculator.getTrueWindAngle(windDirectionE, BoatHeadingE);
        double WEBSE = PolarCalculator.getTrueWindAngle(windDirectionE, BoatHeadingSE);
        double WEBS = PolarCalculator.getTrueWindAngle(windDirectionE, BoatHeadingS);
        double WEBSW = PolarCalculator.getTrueWindAngle(windDirectionE, BoatHeadingSW);
        double WEBW = PolarCalculator.getTrueWindAngle(windDirectionE, BoatHeadingW);
        double WEBNW = PolarCalculator.getTrueWindAngle(windDirectionE, BoatHeadingNW);
        double WEBN2 = PolarCalculator.getTrueWindAngle(windDirectionE, BoatHeadingN2);

        //Test Answers for wind direction East
        assertEquals(90, WEBN, 0);
        assertEquals(60, WEBNE, 0);
        assertEquals(0, WEBE, 0);
        assertEquals(45, WEBSE, 0);
        assertEquals(90, WEBS, 0);
        assertEquals(145, WEBSW, 0);
        assertEquals(180, WEBW, 0);
        assertEquals(150, WEBNW, 0);
        assertEquals(90, WEBN2, 0);

        //GetAnswers For Wind Direction South East
        double WSEBN = PolarCalculator.getTrueWindAngle(windDirectionSE, BoatHeadingN);
        double WSEBNE = PolarCalculator.getTrueWindAngle(windDirectionSE, BoatHeadingNE);
        double WSEBE = PolarCalculator.getTrueWindAngle(windDirectionSE, BoatHeadingE);
        double WSEBSE = PolarCalculator.getTrueWindAngle(windDirectionSE, BoatHeadingSE);
        double WSEBS = PolarCalculator.getTrueWindAngle(windDirectionSE, BoatHeadingS);
        double WSEBSW = PolarCalculator.getTrueWindAngle(windDirectionSE, BoatHeadingSW);
        double WSEBW = PolarCalculator.getTrueWindAngle(windDirectionSE, BoatHeadingW);
        double WSEBNW = PolarCalculator.getTrueWindAngle(windDirectionSE, BoatHeadingNW);
        double WSEBN2 = PolarCalculator.getTrueWindAngle(windDirectionSE, BoatHeadingN2);

        //Test Answers for wind direction South East
        assertEquals(120, WSEBN, 0);
        assertEquals(90, WSEBNE, 0);
        assertEquals(30, WSEBE, 0);
        assertEquals(15, WSEBSE, 0);
        assertEquals(60, WSEBS, 0);
        assertEquals(115, WSEBSW, 0);
        assertEquals(150, WSEBW, 0);
        assertEquals(180, WSEBNW, 0);
        assertEquals(120, WSEBN2, 0);

        //GetAnswers For Wind Direction South
        double WSBN = PolarCalculator.getTrueWindAngle(windDirectionS, BoatHeadingN);
        double WSBNE = PolarCalculator.getTrueWindAngle(windDirectionS, BoatHeadingNE);
        double WSBE = PolarCalculator.getTrueWindAngle(windDirectionS, BoatHeadingE);
        double WSBSE = PolarCalculator.getTrueWindAngle(windDirectionS, BoatHeadingSE);
        double WSBS = PolarCalculator.getTrueWindAngle(windDirectionS, BoatHeadingS);
        double WSBSW = PolarCalculator.getTrueWindAngle(windDirectionS, BoatHeadingSW);
        double WSBW = PolarCalculator.getTrueWindAngle(windDirectionS, BoatHeadingW);
        double WSBNW = PolarCalculator.getTrueWindAngle(windDirectionS, BoatHeadingNW);
        double WSBN2 = PolarCalculator.getTrueWindAngle(windDirectionS, BoatHeadingN2);

        //Test Answers for wind direction South
        assertEquals(180, WSBN, 0);
        assertEquals(150, WSBNE, 0);
        assertEquals(90, WSBE, 0);
        assertEquals(45, WSBSE, 0);
        assertEquals(0, WSBS, 0);
        assertEquals(55, WSBSW, 0);
        assertEquals(90, WSBW, 0);
        assertEquals(120, WSBNW, 0);
        assertEquals(180, WSBN2, 0);

        //GetAnswers For Wind Direction South West
        double WSWBN = PolarCalculator.getTrueWindAngle(windDirectionSW, BoatHeadingN);
        double WSWBNE = PolarCalculator.getTrueWindAngle(windDirectionSW, BoatHeadingNE);
        double WSWBE = PolarCalculator.getTrueWindAngle(windDirectionSW, BoatHeadingE);
        double WSWBSE = PolarCalculator.getTrueWindAngle(windDirectionSW, BoatHeadingSE);
        double WSWBS = PolarCalculator.getTrueWindAngle(windDirectionSW, BoatHeadingS);
        double WSWBSW = PolarCalculator.getTrueWindAngle(windDirectionSW, BoatHeadingSW);
        double WSWBW = PolarCalculator.getTrueWindAngle(windDirectionSW, BoatHeadingW);
        double WSWBNW = PolarCalculator.getTrueWindAngle(windDirectionSW, BoatHeadingNW);
        double WSWBN2 = PolarCalculator.getTrueWindAngle(windDirectionSW, BoatHeadingN2);

        //Test Answers for wind direction South West
        assertEquals(140, WSWBN, 0);
        assertEquals(170, WSWBNE, 0);
        assertEquals(130, WSWBE, 0);
        assertEquals(85, WSWBSE, 0);
        assertEquals(40, WSWBS, 0);
        assertEquals(15, WSWBSW, 0);
        assertEquals(50, WSWBW, 0);
        assertEquals(80, WSWBNW, 0);
        assertEquals(140, WSWBN2, 0);

        //GetAnswers For Wind Direction West
        double WWBN = PolarCalculator.getTrueWindAngle(windDirectionW, BoatHeadingN);
        double WWBNE = PolarCalculator.getTrueWindAngle(windDirectionW, BoatHeadingNE);
        double WWBE = PolarCalculator.getTrueWindAngle(windDirectionW, BoatHeadingE);
        double WWBSE = PolarCalculator.getTrueWindAngle(windDirectionW, BoatHeadingSE);
        double WWBS = PolarCalculator.getTrueWindAngle(windDirectionW, BoatHeadingS);
        double WWBSW = PolarCalculator.getTrueWindAngle(windDirectionW, BoatHeadingSW);
        double WWBW = PolarCalculator.getTrueWindAngle(windDirectionW, BoatHeadingW);
        double WWBNW = PolarCalculator.getTrueWindAngle(windDirectionW, BoatHeadingNW);
        double WWBN2 = PolarCalculator.getTrueWindAngle(windDirectionW, BoatHeadingN2);

        //Test Answers for wind direction West
        assertEquals(90, WWBN, 0);
        assertEquals(120, WWBNE, 0);
        assertEquals(180, WWBE, 0);
        assertEquals(135, WWBSE, 0);
        assertEquals(90, WWBS, 0);
        assertEquals(35, WWBSW, 0);
        assertEquals(0, WWBW, 0);
        assertEquals(30, WWBNW, 0);
        assertEquals(90, WWBN2, 0);

        //GetAnswers For Wind Direction North West
        double WNWBN = PolarCalculator.getTrueWindAngle(windDirectionNW, BoatHeadingN);
        double WNWBNE = PolarCalculator.getTrueWindAngle(windDirectionNW, BoatHeadingNE);
        double WNWBE = PolarCalculator.getTrueWindAngle(windDirectionNW, BoatHeadingE);
        double WNWBSE = PolarCalculator.getTrueWindAngle(windDirectionNW, BoatHeadingSE);
        double WNWBS = PolarCalculator.getTrueWindAngle(windDirectionNW, BoatHeadingS);
        double WNWBSW = PolarCalculator.getTrueWindAngle(windDirectionNW, BoatHeadingSW);
        double WNWBW = PolarCalculator.getTrueWindAngle(windDirectionNW, BoatHeadingW);
        double WNWBNW = PolarCalculator.getTrueWindAngle(windDirectionNW, BoatHeadingNW);
        double WNWBN2 = PolarCalculator.getTrueWindAngle(windDirectionNW, BoatHeadingN2);

        //Test Answers for wind direction North West
        assertEquals(45, WNWBN, 0);
        assertEquals(75, WNWBNE, 0);
        assertEquals(135, WNWBE, 0);
        assertEquals(180, WNWBSE, 0);
        assertEquals(135, WNWBS, 0);
        assertEquals(80, WNWBSW, 0);
        assertEquals(45, WNWBW, 0);
        assertEquals(15, WNWBNW, 0);
        assertEquals(45, WNWBN2, 0);

        //GetAnswers For Wind Direction North2
        double WN2BN = PolarCalculator.getTrueWindAngle(windDirectionN2, BoatHeadingN);
        double WN2BNE = PolarCalculator.getTrueWindAngle(windDirectionN2, BoatHeadingNE);
        double WN2BE = PolarCalculator.getTrueWindAngle(windDirectionN2, BoatHeadingE);
        double WN2BSE = PolarCalculator.getTrueWindAngle(windDirectionN2, BoatHeadingSE);
        double WN2BS = PolarCalculator.getTrueWindAngle(windDirectionN2, BoatHeadingS);
        double WN2BSW = PolarCalculator.getTrueWindAngle(windDirectionN2, BoatHeadingSW);
        double WN2BW = PolarCalculator.getTrueWindAngle(windDirectionN2, BoatHeadingW);
        double WN2BNW = PolarCalculator.getTrueWindAngle(windDirectionN2, BoatHeadingNW);
        double WN2BN2 = PolarCalculator.getTrueWindAngle(windDirectionN2, BoatHeadingN2);

        //Test Answers for wind direction North2
        assertEquals(0.0, WN2BN, 0);
        assertEquals(30, WN2BNE, 0);
        assertEquals(90, WN2BE, 0);
        assertEquals(135, WN2BSE, 0);
        assertEquals(180, WN2BS, 0);
        assertEquals(125, WN2BSW, 0);
        assertEquals(90, WN2BW, 0);
        assertEquals(60, WN2BNW, 0);
        assertEquals(0, WN2BN2, 0);

    }
}
