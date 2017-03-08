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

                // Check next boat does not have the same name as any other in the file
                boolean duplicateName = false;
                String boatName = boatInfo[0];
                String teamName = boatInfo[1];

                for (Boat boat : boatList) {
                    if (boatName.equals(boat.getBoatName())) {
                        System.out.printf("There is a boat with the name '%s' in the list already.\n" +
                                        "The boat '%s %s' has not been added to the race\n",
                                boatName, boatName, teamName);
                        duplicateName = true;
                    }
                }
                if (!duplicateName) {
                    boatList.add(new Boat(boatName, teamName)); //files take form: boatName,teamName
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boatList;
    }
}
