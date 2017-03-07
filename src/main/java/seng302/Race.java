package seng302;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A class to represent an individual race.
 */
class Race {

    private ArrayList<Boat> startingList = new ArrayList<>();
    private Course course;
    private ArrayList<Boat> finishedList = new ArrayList<>();


    /**
     * Race class constructor.
     *
     * @param startingList Arraylist holding all entered boats
     * @param course       Course object
     */
    Race(ArrayList<Boat> startingList, Course course) {
        this.startingList = startingList;
        this.course = course;
        setCourseForBoats();
    }


    private void setCourseForBoats() {
        for (Boat boat : startingList) {
            boat.setMarkList(course.getMarks());
            boat.setCurrentMark(boat.getMarkList().get(0));
            boat.setNextMark(boat.getMarkList().get(1));
        }
    }

    /**
     * Starting list getter.
     *
     * @return Arraylist holding all entered boats
     */
    ArrayList<Boat> getStartingList() {
        return startingList;
    }


    /**
     * Starting list setter.
     *
     * @param startingList Arraylist holding all entered boats
     */
    public void setStartingList(ArrayList<Boat> startingList) {
        this.startingList = startingList;
    }


    /**
     * Course getter.
     *
     * @return Course object
     */
    public Course getCourse() {
        return course;
    }


    /**
     * Course setter.
     *
     * @param course Course object
     */
    public void setCourse(Course course) {
        this.course = course;
    }


    /**
     * Add a new Boat to the starting list of boats.
     *
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
    void randomiseOrder() {
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
        System.out.println("Finish order:");
        for (int i = 0; i < finishedList.size(); i++) {
            System.out.println(i + 1 + ". " + finishedList.get(i).getTeamName() + " " + finishedList.get(i).getBoatName());
        }
    }


    void viewStartingList() {
        System.out.println("Starting List:");

        for (Boat boat : startingList) {
            System.out.printf("Boatname: %s, Teamname: %s, Speed: %.2f\n", boat.getBoatName(), boat.getTeamName(), boat.getSpeed());
        }
    }


    double knotsToMetersPerSecond(double knots) {
        return ((knots * 1.852)/3.6);
    }


    private void checkBoatPostions() {
        for (Boat boat : startingList) {
            if (!(boat.isFinished())){
                Mark nextMark = boat.getNextMark();
                double boatPosition = boat.getPosition();
                if (boatPosition >= nextMark.getDistanceFromStart()){
                    boat.setCurrentMark(boat.getNextMark());
                    boat.setHeading(boat.getCurrentMark().getMarkHeading());
                    int i = boat.getMarkList().indexOf(boat.getNextMark());

                    if (i < boat.getMarkList().size() - 1) {
                        boat.setNextMark(boat.getMarkList().get(i + 1));
                    } else {
                        boat.setFinished(true);
                        finishedList.add(boat);
                    }
                    if (boat.getCurrentMark().getMarkHeading() != -1) {
                        System.out.printf("%s has passed mark: %s with heading %d degrees\n",
                                boat.getBoatName(), boat.getCurrentMark().getMarkName(), boat.getHeading());
                    } else {
                        System.out.printf("%s has finished the race!! :)\n", boat.getBoatName());
                    }
                }
                boat.setPosition(boat.getPosition() + 0.1 * knotsToMetersPerSecond(boat.getSpeed()));
            }
        }

    }


    /**
     * Display each boat in the race and their position at each mark.
     */
    void runRace() {
        System.out.println("Start");
        int loopTime = 100;

        while (finishedList.size() < startingList.size()) {
            final long startTime = System.currentTimeMillis();
            checkBoatPostions();
            final long endTime = System.currentTimeMillis();
            try {
                Thread.sleep(loopTime - (endTime - startTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        viewFinishOrder();
        System.exit(0);
    }
}
