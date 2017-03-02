package seng302;

import java.util.ArrayList;

/**
 * A class to represent an individual race.
 */
public class Race {

    private ArrayList<Boat> startingList = new ArrayList<Boat>();

    void addBoat(Boat boatToAdd) {
        boolean nameDifferent = true;

        for (Boat boatToCheck : startingList) {
            if ((boatToCheck.getBoatName().equals(boatToAdd.getBoatName()))
                    || (boatToCheck.getTeamName().equals(boatToAdd.getTeamName()))) {
                nameDifferent = false;
                System.out.println("Error, this boat is already in the race.");
            }
        }

        if (nameDifferent) {
            this.startingList.add(boatToAdd);
        }
    }


    void viewBoats() {
        for (Boat boat : startingList) {
            System.out.printf("%s %s\n", boat.getTeamName(), boat.getBoatName());
        }
    }

}
