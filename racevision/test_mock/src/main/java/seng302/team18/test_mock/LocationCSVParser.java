package seng302.team18.test_mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Class to parse race data from a CSV.
 * Data is to be used to create Regatta.xml.
 * CSV should have lines in the format of:
 * Regatta ID, Regatta Name, Course Name, UTC, Magnetic Variation, Race ID, Race Type, Race Start Time, top left Lat, top left long, top right Lat, top right long, bottom right Lat, bottom right long, top left Lat, top left long
 */
public class LocationCSVParser {

    /**
     * Reads CSV file and uses information to create Reggata.xml
     * @param file CSV file
     */
    public static void ParserCSV(File file) {
        int numRaces = 0;
        try {
            numRaces = getNumRaces(file);           //get number of lines
            System.out.println();
        }catch (Exception e){
        }
        Random rand = new Random();
        int  lineNum = rand.nextInt(numRaces);      //chose a random line
        String location = "oh no";
        try {
            location = getLine(lineNum, file);      //get chosen line
        }catch (Exception e){
        }
        System.out.println(lineNum + "  " +location);
        //makeRegatta(location);                      //create Regatta.xml
    }

    //gets the number of lines in a file
    private static int getNumRaces(File file) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String input;
        int count = 0;
        while((input = bufferedReader.readLine()) != null)
        {
            count++;
        }
        return count;
    }

    //gets a specific line from a file
    private static String getLine(int lineNum, File file)throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        int count = 0;
        while(count < lineNum)
        {
            line = bufferedReader.readLine();
            count++;
        }
        return line;
    }

//    private static File makeRegatta(String location){
//
//    }

}
