package seng302.team18.util;


import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import seng302.team18.model.Polar;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Class to Test polar Calculator
 */
public class PolarCalculatorTest {

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
    public void getPolarForWindSpeedTestAboveMax(){
        //Windspeed greater than highest wind speed
        double windspeed1 = 33;

        Polar returned1 = calculator.getPolarForWindSpeed(windspeed1);

        assertEquals(polar3, returned1);

    }


    @Test
    public void getPolarForWindSpeedTestRoundingUp(){
        //Windspeeds lower than closest polar
        double windspeed2 = 20;
        double windspeed3 = 29;

        Polar returned2 = calculator.getPolarForWindSpeed(windspeed2);
        Polar returned3 = calculator.getPolarForWindSpeed(windspeed3);

        assertEquals(polar2, returned2);
        assertEquals(polar3, returned3);
    }


    @Test
    public void getPolarForWindSpeedTestEqual(){
        //Windspeeds Equal polar windSpeeds
        double windspeed1 = 12;
        double windspeed2 = 25;
        double windspeed3 = 30;

        Polar returned1 = calculator.getPolarForWindSpeed(windspeed1);
        Polar returned2 = calculator.getPolarForWindSpeed(windspeed2);
        Polar returned3 = calculator.getPolarForWindSpeed(windspeed3);

        assertEquals(polar1, returned1);
        assertEquals(polar2, returned2);
        assertEquals(polar3, returned3);
    }


    @Test
    public void getPolarForWindSpeedTestRoundingDown(){
        //Windspeeds higher than closest polar
        double windspeed1 = 14;
        double windspeed2 = 27;

        Polar returned1 = calculator.getPolarForWindSpeed(windspeed1);
        Polar returned2 = calculator.getPolarForWindSpeed(windspeed2);

        assertEquals(polar1, returned1);
        assertEquals(polar2, returned2);
    }


    @Test
    public void getPolarForWindSpeedTestBelowMin(){
        //Windspeed lower than lowest wind speed
        double windspeed1 = 10;

        Polar returned1 = calculator.getPolarForWindSpeed(windspeed1);

        assertEquals(polar1, returned1);

    }


    @Test
    public void getPolarForWindSpeedTestCenter(){
        //Windspeeds centered between polars
        double windspeed1 = 18.5;
        double windspeed2 = 27.5;

        Polar returned1 = calculator.getPolarForWindSpeed(windspeed1);
        Polar returned2 = calculator.getPolarForWindSpeed(windspeed2);

        assertEquals(polar1, returned1);
        assertEquals(polar2, returned2);
    }


    @Test
    public void getSpeedForBoatTestMinAngle(){
        //Minimum TWA
        double boatTWA = 0;
        double windSpeed1 = 12;
        double windSpeed2 = 25;

        double returned1 = calculator.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = calculator.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(0.0, returned1);
        assertEquals(0.0, returned2);
    }


    @Test
    public void getSpeedForBoatsMaxAngle(){
        //Max TWA
        double boatTWA = 175;
        double windSpeed1 = 12;
        double windSpeed2 = 30;

        double returned1 = calculator.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = calculator.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(14.0, returned1);
        assertEquals(32.0, returned2);
    }


    @Test
    public void getSpeedForBoatsAboveMaxAngle(){
        //Above max TWA
        double boatTWA = 177;
        double windSpeed1 = 25;
        double windSpeed2 = 30;

        double returned1 = calculator.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = calculator.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(30.0, returned1);
        assertEquals(32.0, returned2);
    }


    @Test
    public void getSpeedForBoatsRoundingUp(){
        //TWA should be rounded up to 90
        double boatTWA = 85;
        double windSpeed1 = 12;
        double windSpeed2 = 25;

        double returned1 = calculator.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = calculator.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(23.0, returned1);
        assertEquals(49.0, returned2);
    }


    @Test
    public void getSpeedForBoatsRoundingDown(){
        //TWA should be rounded down to 75
        double boatTWA = 80;
        double windSpeed1 = 25;
        double windSpeed2 = 30;

        double returned1 = calculator.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = calculator.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(44.0, returned1);
        assertEquals(42.0, returned2);
    }


    @Test
    public void getSpeedForBoatsBetweenPoints(){
        //TWA should be rounded down to 75
        double boatTWA = 82.5;
        double windSpeed1 = 12;
        double windSpeed2 = 30;

        double returned1 = calculator.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = calculator.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(20.0, returned1);
        assertEquals(42.0, returned2);
    }


    @Test
    public void getSpeedForBoatsUpTWA(){
        //TWA should be rounded down to 75
        double boatTWA1 = 43;
        double boatTWA2 = 40;
        double windSpeed1 = 12;
        double windSpeed2 = 25;

        double returned1 = calculator.getSpeedForBoat(boatTWA1, windSpeed1);
        double returned2 = calculator.getSpeedForBoat(boatTWA2, windSpeed2);

        assertEquals(14.4, returned1);
        assertEquals(30.0, returned2);
    }


    @Test
    public void getSpeedForBoatsDnTWA(){
        //TWA should be rounded down to 75
        double boatTWA1 = 151;
        double boatTWA2 = 150;
        double windSpeed1 = 25;
        double windSpeed2 = 30;

        double returned1 = calculator.getSpeedForBoat(boatTWA1, windSpeed1);
        double returned2 = calculator.getSpeedForBoat(boatTWA2, windSpeed2);

        assertEquals(47.0, returned1);
        assertEquals(46.0, returned2);
    }

}
