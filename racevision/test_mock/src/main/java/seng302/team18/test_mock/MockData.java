package seng302.team18.test_mock;

import seng302.team18.model.Coordinate;

import java.io.File;

/**
 * Created by jth102 on 11/04/17.
 */
public class MockData {

    public static void main (String[] args){
        CoordinateContainer coordinates = new CoordinateContainer();
        File LocationsCSV = new File(System.getProperty("user.dir")+"/racevision/test_mock/src/main/resources/Locations.csv");
        LocationCSVParser.ParserCSV(LocationsCSV);
    }
}
