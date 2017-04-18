package seng302.team18.test_mock;


import com.sun.javafx.runtime.SystemProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LocationCSVParserTest {

    protected MockData mockDataTest;
    private LocationCSVParser testParser;
    private File isleCSV;
    private File newYorkCSV;

    @Before
    public void setUp(){
        mockDataTest = new MockData();
        testParser = new LocationCSVParser(mockDataTest);
        isleCSV = new File(getClass().getClassLoader().getResource("isleOfWightTest.csv").getFile());
        newYorkCSV = new File(getClass().getClassLoader().getResource("NYCTest.csv").getFile());
    }

    @After
    public void tearDown(){
        mockDataTest = null;
        testParser = null;
        isleCSV = null;
        newYorkCSV = null;
    }

    @Test
    public void parseCVSMockDataTest(){
        testParser.parseCSV(isleCSV);
        Coordinate topLeft = new Coordinate(50.7459, -1.4897);
        Coordinate topRight = new Coordinate(50.7735, -1.3825);
        Coordinate bottomRight = new Coordinate(50.7503, -1.3609);
        Coordinate bottomLeft = new Coordinate(50.7225, -1.466);

        assertEquals(22085106, mockDataTest.raceID, 0);
        assertEquals("Fleet", mockDataTest.raceType);
        assertEquals("12.00", mockDataTest.raceStartTime);
        assertEquals(topLeft, mockDataTest.boundaries.get(0));
        assertEquals(topRight, mockDataTest.boundaries.get(1));
        assertEquals(bottomRight, mockDataTest.boundaries.get(2));
        assertEquals(bottomLeft, mockDataTest.boundaries.get(3));
    }

    @Test
    public void parseCSVRegattaXMLtest(){
        testParser.parseCSV(newYorkCSV);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/main/resources/Regatta.xml")));
            String line1 = bufferedReader.readLine();
            String line2 = bufferedReader.readLine();
            String line3 = bufferedReader.readLine();
            String line4 = bufferedReader.readLine();
            String line5 = bufferedReader.readLine();
            String line6 = bufferedReader.readLine();
            String line7 = bufferedReader.readLine();
            String line8 = bufferedReader.readLine();
            String line9 = bufferedReader.readLine();
            String line10 = bufferedReader.readLine();
            String line11 = bufferedReader.readLine();
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>",line1);
            assertEquals("<RegattaConfig>",line2);
            assertEquals("  <RegattaID>121</RegattaID>",line3);
            assertEquals("  <RegattaName>New York City Mock</RegattaName>",line4);
            assertEquals("  <CourseName>AC13</CourseName>",line5);
            assertEquals("  <CentralLatitude>40.398482131821964</CentralLatitude>",line6);
            assertEquals("  <CentralLongitude>-73.92494706003819</CentralLongitude>",line7);
            assertEquals("  <CentralAltitude>0.00</CentralAltitude>",line8);
            assertEquals("  <UtcOffset>-4</UtcOffset>",line9);
            assertEquals("  <MagneticVariation>-12</MagneticVariation>",line10);
            assertEquals("</RegattaConfig>",line11);
        }catch (Exception e){
            System.out.println("oh no");
        }
    }
}
