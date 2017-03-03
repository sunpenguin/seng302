package seng302;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * A class to represent an individual race.
 */
public class Race {

    private ArrayList<Boat> startingList = new ArrayList<>();

    public ArrayList<Boat> getStartingList() {
        return startingList;
    }

    void addBoat(Boat boatToAdd) {
        boolean nameDifferent = true;

        for (Boat boatToCheck : startingList) {
            if (boatToCheck.getBoatName().equals(boatToAdd.getBoatName())) {
                nameDifferent = false;
                System.out.println("Error, this boat is already in the race.");
            }
        }

        if (nameDifferent) {
            this.startingList.add(boatToAdd);
        }
    }

    void randomiseOrder(){
        long seed = System.nanoTime();
        Collections.shuffle(this.startingList, new Random(seed));
    }

    void viewBoats() {
        for (Boat boat : startingList) {
            System.out.printf("%s %s\n", boat.getTeamName(), boat.getBoatName());
        }
    }

}
