package seng302;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A class to represent an individual race.
 */
class Race {

    private ArrayList<Boat> startingList = new ArrayList<>();
    private Course course;

    public Race(ArrayList<Boat> startingList, Course course) {
        this.startingList = startingList;
        this.course = course;
    }

    public void setStartingList(ArrayList<Boat> startingList) {
        this.startingList = startingList;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    ArrayList<Boat> getStartingList() {
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
        Collections.shuffle(this.startingList);
    }

    void viewBoats() {
        for (Boat boat : startingList) {
            System.out.printf("%s %s\n", boat.getTeamName(), boat.getBoatName());
        }
    }

    /**
     * Displays the finishing order of the race to the user
     */
    void viewFinishOrder() {
        for (int i = 0; i < startingList.size(); i++){
            System.out.println(i+1 + ". " + startingList.get(i).getTeamName() + " " + startingList.get(i).getBoatName());
        }
    }

    void runRace(){
        System.out.println("Start");
        for(int i = 1; i < course.marks.size(); i++){
            course.marks.get(i).displayBoatOrder();
        }

    }

}
