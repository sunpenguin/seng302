package seng302.team18.test_mock;

import seng302.team18.model.Coordinate;

import java.io.File;

/**
 * Created by jth102 on 11/04/17.
 */
public class Main {

    protected MockData mockData = new MockData();

    public static void main (String[] args){
       Main myMain = new Main();
       myMain.go();
    }

    public void go(){
        CoordinateContainer coordinates = new CoordinateContainer();
        LocationCSVParser MyParser = new LocationCSVParser(mockData);
        File LocationsCSV = new File("racevision/test_mock/src/main/resources/Locations.csv");
        MyParser.ParserCSV(LocationsCSV);
    }

    public MockData getMockData() {
        return mockData;
    }

}
