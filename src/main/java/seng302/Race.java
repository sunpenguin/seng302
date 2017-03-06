package seng302;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A class to represent an individual race.
 */
class Race {

    private ArrayList<Boat> startingList = new ArrayList<>();
    private Course course;


    /**
     * Race class constructor.
     * @param startingList Arraylist holding all entered boats
     * @param course Course object
     */
    Race(ArrayList<Boat> startingList, Course course) {
        this.startingList = startingList;
        this.course = course;
        setCourseForBoats();
    }


    private void setCourseForBoats() {
        for (Boat boat : startingList) {
            boat.setMarkList(course.getMarks());
            boat.setCurrentMark(new Mark("Start", 0, 0, 0));
            boat.setNextMark(boat.getMarkList().get(0));
        }
    }

    /**
     * Starting list getter.
     * @return Arraylist holding all entered boats
     */
    ArrayList<Boat> getStartingList() {
        return startingList;
    }


    /**
     * Starting list setter.
     * @param startingList Arraylist holding all entered boats
     */
    public void setStartingList(ArrayList<Boat> startingList) {
        this.startingList = startingList;
    }


    /**
     * Course getter.
     * @return Course object
     */
    public Course getCourse() {
        return course;
    }


    /**
     * Course setter.
     * @param course Course object
     */
    public void setCourse(Course course) {
        this.course = course;
    }


    /**
     * Add a new Boat to the starting list of boats.
     * @param boatToAdd Boat object
     */
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


    /**
     * Shuffles the starting list of boats.
     */
    void randomiseOrder(){
        Collections.shuffle(this.startingList);
    }


    /**
     * Displays team and boat names of each boat in the starting list..
     */
    void viewBoats() {
        for (Boat boat : startingList) {
            System.out.printf("%s %s\n", boat.getTeamName(), boat.getBoatName());
        }
    }


    /**
     * Displays the finishing order of the race to the user.
     */
    void viewFinishOrder() {
        for (int i = 0; i < startingList.size(); i++){
            System.out.println(i+1 + ". " + startingList.get(i).getTeamName() + " " + startingList.get(i).getBoatName());
        }
    }


    /**
     * Display each boat in the race and their position at each mark.
     */
    void runRace(){
        System.out.println("Start");
        for(int i = 1; i < course.getMarks().size(); i++){
            course.getMarks().get(i).displayBoatOrder();
        }
    }
}
