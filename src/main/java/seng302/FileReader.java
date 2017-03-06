package seng302;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for reading files
 */
class FileReader {

    /**
     * Method to return an ArrayList of boats to compete in a race
     * @param fileName The name of the file with a list of boats
     * @return an ArrayList of Boat objects.
     */
    ArrayList<Boat> readBoatListFile(String fileName) {
        ArrayList<Boat> boatList = new ArrayList<>();
        String line;
        String csvSplitBy = ",";

        try (BufferedReader b = new BufferedReader(new java.io.FileReader(fileName))) {
            while ((line = b.readLine()) != null) {
                String[] boatInfo = line.split(csvSplitBy);

                boatList.add(new Boat(boatInfo[0], boatInfo[1])); //files take form: boatName,teamName
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boatList;
    }
}
