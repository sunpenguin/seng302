package seng302.team18.model;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.AC35PolarPattern;
import seng302.team18.model.Polar;
import seng302.team18.util.XYPair;

import java.util.ArrayList;
import java.util.List;


import static junit.framework.TestCase.assertEquals;

/**
 * Created by sbe67 on 5/07/17.
 */
public class AC35PollarPatternTest {

    AC35PolarPattern ac35PolarPattern = new AC35PolarPattern();
    Polar polar0; //windspeed = 0
    Polar polar1; //windspeed = 4
    Polar polar2; //windspeed = 8
    Polar polar3; //windspeed = 12
    Polar polar4; //windspeed = 16
    Polar polar5; //windspeed = 20
    Polar polar6; //windspeed = 25
    Polar polar7; //windspeed = 30


    @Before
    public void setUp() {

        //Make polar 0
        polar0 = new Polar(0,45,0,155,0);
        polar0.addToMap(0,0);
        polar0.addToMap(15,0);
        polar0.addToMap(30,0);
        polar0.addToMap(60,0);
        polar0.addToMap(75,0);
        polar0.addToMap(90,0);
        polar0.addToMap(115,0);
        polar0.addToMap(140,0);
        polar0.addToMap(170,0);
        polar0.addToMap(165,0);
        polar0.addToMap(180,0);

        //Make Polar 1
        polar1 = new Polar(4, 45,8,155,10);
        polar1.addToMap(0,0);
        polar1.addToMap(30,4);
        polar1.addToMap(60,9);
        polar1.addToMap(75,10);
        polar1.addToMap(90,10);
        polar1.addToMap(115,10);
        polar1.addToMap(145,10);
        polar1.addToMap(175,4);

        //Make polar 3
        polar3 = new Polar(12, 43, 14.4, 153, 21.6);
        polar3.addToMap(0, 0);
        polar3.addToMap(30, 11);
        polar3.addToMap(60, 16);
        polar3.addToMap(75, 20);
        polar3.addToMap(90, 23);
        polar3.addToMap(115, 24);
        polar3.addToMap(145, 23);
        polar3.addToMap(175, 14);

        //MAke polar 4
        polar4 = new Polar (16,42,19.2,153,28.8);
        polar4.addToMap(0,0);
        polar4.addToMap(30,12);
        polar4.addToMap(60,25);
        polar4.addToMap(75,27);
        polar4.addToMap(90,31);
        polar4.addToMap(115,32);
        polar4.addToMap(145,30);
        polar4.addToMap(175,20);

        //Make polar 6
        polar6 = new Polar(25, 40, 30, 151, 47);
        polar6.addToMap(0, 0);
        polar6.addToMap(30, 15);
        polar6.addToMap(60, 38);
        polar6.addToMap(75, 44);
        polar6.addToMap(90, 49);
        polar6.addToMap(115, 50);
        polar6.addToMap(145, 49);
        polar6.addToMap(175, 30);

        //Make polar 7
        polar7 = new Polar(30, 42, 30, 150, 46);
        polar7.addToMap(0, 0);
        polar7.addToMap(30, 15);
        polar7.addToMap(60, 37);
        polar7.addToMap(75, 42);
        polar7.addToMap(90, 48);
        polar7.addToMap(115, 49);
        polar7.addToMap(145, 48);
        polar7.addToMap(175, 32);

    }

    @Test
    public void getPolarForWindSpeedTestAboveMax(){
        //Windspeed greater than highest wind speed
        double windspeed1 = 33;

        Polar returned1 = ac35PolarPattern.getPolarForWindSpeed(windspeed1);

        assertEquals(polar7, returned1);
    }


    @Test
    public void getPolarForWindSpeedTestRoundingUp(){
        //Windspeeds lower than closest polar
        double windspeed2 = 23;
        double windspeed3 = 29;

        Polar returned2 = ac35PolarPattern.getPolarForWindSpeed(windspeed2);
        Polar returned3 = ac35PolarPattern.getPolarForWindSpeed(windspeed3);

        assertEquals(polar6, returned2);
        assertEquals(polar7, returned3);
    }


    @Test
    public void getPolarForWindSpeedTestEqual(){
        //Windspeeds Equal polar windSpeeds
        double windspeed1 = 12;
        double windspeed2 = 25;
        double windspeed3 = 30;

        Polar returned1 = ac35PolarPattern.getPolarForWindSpeed(windspeed1);
        Polar returned2 = ac35PolarPattern.getPolarForWindSpeed(windspeed2);
        Polar returned3 = ac35PolarPattern.getPolarForWindSpeed(windspeed3);

        assertEquals(polar3, returned1);
        assertEquals(polar6, returned2);
        assertEquals(polar7, returned3);
    }


    @Test
    public void getPolarForWindSpeedTestRoundingDown(){
        //Windspeeds higher than closest polar
        double windspeed1 = 14;
        double windspeed2 = 27;

        Polar returned1 = ac35PolarPattern.getPolarForWindSpeed(windspeed1);
        Polar returned2 = ac35PolarPattern.getPolarForWindSpeed(windspeed2);

        assertEquals(polar3, returned1);
        assertEquals(polar6, returned2);
    }


    @Test
    public void getPolarForWindSpeedTestBelowMin(){
        //Windspeed lower than lowest wind speed
        double windspeed1 = 11;

        Polar returned1 = ac35PolarPattern.getPolarForWindSpeed(windspeed1);

        assertEquals(polar3, returned1);

    }


    @Test
    public void getPolarForWindSpeedTestCenter(){
        //Windspeeds centered between polars
        double windspeed1 = 14;
        double windspeed2 = 27.5;

        Polar returned1 = ac35PolarPattern.getPolarForWindSpeed(windspeed1);
        Polar returned2 = ac35PolarPattern.getPolarForWindSpeed(windspeed2);

        assertEquals(polar3, returned1);
        assertEquals(polar6, returned2);
    }


    @Test
    public void getSpeedForBoatTestMinAngle(){
        //Minimum TWA
        double boatTWA = 0;
        double windSpeed1 = 12;
        double windSpeed2 = 25;

        double returned1 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(0.0, returned1);
        assertEquals(0.0, returned2);
    }


    @Test
    public void getSpeedForBoatsMaxAngle(){
        //Max TWA
        double boatTWA = 175;
        double windSpeed1 = 12;
        double windSpeed2 = 30;

        double returned1 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(14.0, returned1);
        assertEquals(32.0, returned2);
    }


    @Test
    public void getSpeedForBoatsAboveMaxAngle(){
        //Above max TWA
        double boatTWA = 177;
        double windSpeed1 = 25;
        double windSpeed2 = 30;

        double returned1 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(30.0, returned1);
        assertEquals(32.0, returned2);
    }


    @Test
    public void getSpeedForBoatsRoundingUp(){
        //TWA should be rounded up to 90
        double boatTWA = 85;
        double windSpeed1 = 12;
        double windSpeed2 = 25;

        double returned1 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(23.0, returned1);
        assertEquals(49.0, returned2);
    }


    @Test
    public void getSpeedForBoatsRoundingDown(){
        //TWA should be rounded down to 75
        double boatTWA = 80;
        double windSpeed1 = 25;
        double windSpeed2 = 30;

        double returned1 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed2);

        assertEquals(44.0, returned1);
        assertEquals(42.0, returned2);
    }


    @Test
    public void getSpeedForBoatsBetweenPoints(){
        //TWA should be rounded down to 75
        double boatTWA = 82.5;
        double windSpeed1 = 12;
        double windSpeed2 = 30;

        double returned1 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed1);
        double returned2 = ac35PolarPattern.getSpeedForBoat(boatTWA, windSpeed2);

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

        double returned1 = ac35PolarPattern.getSpeedForBoat(boatTWA1, windSpeed1);
        double returned2 = ac35PolarPattern.getSpeedForBoat(boatTWA2, windSpeed2);

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

        double returned1 = ac35PolarPattern.getSpeedForBoat(boatTWA1, windSpeed1);
        double returned2 = ac35PolarPattern.getSpeedForBoat(boatTWA2, windSpeed2);

        assertEquals(47.0, returned1);
        assertEquals(46.0, returned2);
    }


    @Test
    public void getTwoClosestPolarsWindSpeedAboveMax(){
        //Test when windspeed is greater then the max windSpeed
        double windSpeed1 = 31;
        double windSpeed2 = 90;

        List<Polar> returned1 = ac35PolarPattern.getTwoClosestPolars(windSpeed1);
        List<Polar> returned2 = ac35PolarPattern.getTwoClosestPolars(windSpeed2);

        assertEquals(1, returned1.size());
        assertEquals(1, returned2.size());

        assertEquals(polar7, returned1.get(0));
        assertEquals(polar7, returned2.get(0));
    }


    @Test
    public void getTwoClosestPolarsWindSpeedEqualToValue(){
        //Test when windSpeed is equal to polar windspeeds
        double windSpeed1 = 0;
        double windSpeed2 = 12;
        double windSpeed3 = 30;

        List<Polar> returned1 = ac35PolarPattern.getTwoClosestPolars(windSpeed1);
        List<Polar> returned2 = ac35PolarPattern.getTwoClosestPolars(windSpeed2);
        List<Polar> returned3 = ac35PolarPattern.getTwoClosestPolars(windSpeed3);

        assertEquals(1, returned1.size());
        assertEquals(1, returned2.size());
        assertEquals(1, returned3.size());

        assertEquals(polar0, returned1.get(0));
        assertEquals(polar3, returned2.get(0));
        assertEquals(polar7, returned3.get(0));
    }


    @Test
    public void getTwoClosestPolarsWindSpeedIsBetweenPolars(){
        //Test when WindSpeed is between the windspeed of 2 polars
        double windSpeed1 = 2;
        double windSpeed2 = 14;
        double windSpeed3 = 27;

        List<Polar> returned1 = ac35PolarPattern.getTwoClosestPolars(windSpeed1);
        List<Polar> returned2 = ac35PolarPattern.getTwoClosestPolars(windSpeed2);
        List<Polar> returned3 = ac35PolarPattern.getTwoClosestPolars(windSpeed3);

        assertEquals(2, returned1.size());
        assertEquals(2, returned2.size());
        assertEquals(2, returned3.size());

        assertEquals(true, returned1.contains(polar0));
        assertEquals(true, returned1.contains(polar1));
        assertEquals(true, returned2.contains(polar3));
        assertEquals(true, returned2.contains(polar4));
        assertEquals(true, returned3.contains(polar6));
        assertEquals(true, returned3.contains(polar7));
    }


    @Test
    public void getClosestPointsAboveMax(){
        //Test for when the twa is about the highest value in polar
        double baotTWA1 = 177;

        List<Polar> polarList1 = new ArrayList<>();
        polarList1.add(polar1);
        List<Polar> polarList2 = new ArrayList<>();
        polarList2.add(polar3);
        polarList2.add(polar4);

        List<XYPair> returned1 = ac35PolarPattern.getClosestPoints(baotTWA1, polarList1);
        List<XYPair> returned2 = ac35PolarPattern.getClosestPoints(baotTWA1, polarList2);

        assertEquals(1, returned1.size());
        assertEquals(2, returned2.size());

        XYPair returned1Pair1 = new XYPair(4, 175);
        XYPair returned2Pair1 = new XYPair(12, 175);
        XYPair returned2Pair2 = new XYPair(16, 175);

        assertEquals(true, returned1.contains(returned1Pair1));
        assertEquals(true, returned2.contains(returned2Pair1));
        assertEquals(true, returned2.contains(returned2Pair2));
    }


    @Test
    public void getClosestPointsEqualToValue(){
        //Test for when the twa is equal to a point on  a polar
        double boatTWA1 = 90;

        List<Polar> polarList1 = new ArrayList<>();
        polarList1.add(polar7);
        polarList1.add(polar6);


        List<XYPair> returned1 = ac35PolarPattern.getClosestPoints(boatTWA1, polarList1);

        assertEquals(2, returned1.size());

        XYPair returned1Pair1 = new XYPair(30, 90);
        XYPair returned1Pair2 = new XYPair(25, 90);

        assertEquals(true, returned1.contains(returned1Pair1));
        assertEquals(true, returned1.contains(returned1Pair2));
    }


    @Test
    public void getClosestPointsCloserToWrongValues(){
        //Test for when the twa is between points
        //However it is closer to two values on one side
        double boatTWA1 = 43;

        List<Polar> polarList1 = new ArrayList<>();
        polarList1.add(polar3);
        polarList1.add(polar4);

        List<XYPair> returned1 = ac35PolarPattern.getClosestPoints(boatTWA1, polarList1);

        assertEquals(3, returned1.size());

        XYPair returned1Pair1 = new XYPair(12, 43);
        XYPair returned1Pair2 = new XYPair(16, 42);
        XYPair returned1Pair3 = new XYPair(16, 60);

        assertEquals(true, returned1.contains(returned1Pair1));
        assertEquals(true, returned1.contains(returned1Pair2));
        assertEquals(true, returned1.contains(returned1Pair3));
    }


    @Test
    public void getClosestPointsBetweenValues(){
        //Test for when the TWA is between points
        double boatTWA1 = 100;

        List<Polar> polarList1 = new ArrayList<>();
        polarList1.add(polar7);
        polarList1.add(polar6);

        List<XYPair> returned1 = ac35PolarPattern.getClosestPoints(boatTWA1, polarList1);

        assertEquals(4, returned1.size());

        XYPair returned1Pair1 = new XYPair(30, 90);
        XYPair returned1Pair2 = new XYPair(25, 90);
        XYPair returned1Pair3 = new XYPair(30, 115);
        XYPair returned1Pair4 = new XYPair(25, 115);

        assertEquals(true, returned1.contains(returned1Pair1));
        assertEquals(true, returned1.contains(returned1Pair2));
        assertEquals(true, returned1.contains(returned1Pair3));
        assertEquals(true, returned1.contains(returned1Pair4));
    }


    @Test
    public void getValueForPolarPointEqualsTWA(){
        //Test for when one point is given equal to a point in polar
        double boatTWA1 = 115;
        double boatTWA2 = 175;
        double windSpeed = 12;

        List<XYPair> points1 = new ArrayList<>();
        points1.add(new XYPair(windSpeed, 115));

        List<XYPair> points2 = new ArrayList<>();
        points2.add(new XYPair(windSpeed, 175));

        double returned1  = ac35PolarPattern.getValueForPolar(points1, boatTWA1);
        double returned2  = ac35PolarPattern.getValueForPolar(points2, boatTWA2);

        double expected1 = ac35PolarPattern.getPolarForWindSpeed(points1.get(0).getX()).getMapSpeedAtAngles().get(boatTWA1);
        double expected2 = ac35PolarPattern.getPolarForWindSpeed(points2.get(0).getX()).getMapSpeedAtAngles().get(boatTWA2);

        assertEquals(expected1, returned1);
        assertEquals(expected2, returned2);

    }


    @Test
    public void getValueForPolarTWAAboveMax(){
        //Test for when one point is given above the max in the polar
        double boatTWA1 = 177;
        double boatTWA2 = 179;
        double windSpeed = 20;

        List<XYPair> points1 = new ArrayList<>();
        points1.add(new XYPair(windSpeed, 175));

        double returned1  = ac35PolarPattern.getValueForPolar(points1, boatTWA1);
        double returned2  = ac35PolarPattern.getValueForPolar(points1, boatTWA2);

        double expected1 = 20;
        double expected2 = 16;

        assertEquals(expected1, returned1);
        assertEquals(expected2, returned2);
    }


    @Test
    public void getValueForPolarTwoPointsGiven(){
        //Test for when TWA is between two points
        double boatTWA1 = 70;
        double boatTWA2 = 100;
        double windSpeed = 25;

        List<XYPair> points1 = new ArrayList<>();
        points1.add(new XYPair(windSpeed, 60));
        points1.add(new XYPair(windSpeed, 75));

        List<XYPair> points2 = new ArrayList<>();
        points2.add(new XYPair(windSpeed, 90));
        points2.add(new XYPair(windSpeed, 115));

        double returned1 = ac35PolarPattern.getValueForPolar(points1, boatTWA1);
        double returned2 = ac35PolarPattern.getValueForPolar(points2, boatTWA2);

        double expected1 = 42;
        double expected2 = 49.4;

        assertEquals(expected1, returned1, 0.5);
        assertEquals(expected2, returned2, 0.5);
    }

}
