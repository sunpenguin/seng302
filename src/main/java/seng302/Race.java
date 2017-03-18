package seng302;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A class to represent an individual race.
 */
public class Race {

    private ArrayList<Boat> startingList = new ArrayList<>();
    private Course course;
    private ArrayList<Boat> finishedList = new ArrayList<>();


    /**
     * Race class constructor.
     *
     * @param startingList Arraylist holding all entered boats
     * @param course       Course object
     */
    public Race(ArrayList<Boat> startingList, Course course) {
        this.startingList = startingList;
        this.course = course;
        setCourseForBoats();
    }

    public void LOL() {
        for (Boat b : startingList) {
            b.setCoordinate(course.getCompoundMarks().get(0).getMarks().get(0).getMarkCoordinates());
        }
    }


    /**
     * Called in Race constructor.
     * Set up the course CompoundMarks for each boat in the race as well as set the
     * current(starting CompoundMark) and next CompoundMark.
     */
    private void setCourseForBoats() {
        for (Boat boat : startingList) {
//            boat.setCompoundMarkList(course.getCompoundMarks());
//            boat.setCurrentCompoundMark(boat.getCompoundMarkList().get(0));
//            boat.setNextCompoundMark(boat.getCompoundMarkList().get(1));
            boat.setCurrentLeg(course.getLegs().get(0));
        }
    }

    /**
     * Starting list getter.
     *
     * @return Arraylist holding all entered boats
     */
    public ArrayList<Boat> getStartingList() {
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
    public void addBoat(Boat boatToAdd) {
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
    public void randomiseOrder() {
        Collections.shuffle(this.startingList);
    }


    /**
     * Displays team and boat names of each boat in the starting list..
     */
    public void viewBoats() {
        for (Boat boat : startingList) {
            System.out.printf("%s %s\n", boat.getTeamName(), boat.getBoatName());
        }
    }


    /**
     * Displays the finishing order of the race to the user.
     */
    private void viewFinishOrder() {
        System.out.println("Finish order:");
        for (int i = 0; i < finishedList.size(); i++) {
            System.out.println(i + 1 + ". " + finishedList.get(i).getBoatName() + " " + finishedList.get(i).getTeamName());
        }
    }


    /**
     * Print out the list of starting boats in a race.
     * For each boat, print: Boat name, Team name, current speed.
     */
    public void viewStartingList() {
        System.out.println("Starting List:");
        System.out.println("-----------------------------STARTING LIST-------------------------------------");
        for (Boat boat : startingList) {
            System.out.printf("Boatname: %s, Teamname: %s, Speed: %.2f\n", boat.getBoatName(), boat.getTeamName(), boat.getSpeed());
        }
        System.out.println("-------------------------------------------------------------------------------");
    }


    /**
     * Convert a value given in knots to meters per second.
     * @param knots speed in knots.
     * @return speed in meters per second.
     */
    public static double knotsToMetersPerSecond(double knots) {
        return ((knots * 1.852)/3.6);
    }

//
//    /**
//     * Check the positions of each boat in the race that is still racing
//     * and update the positions based on boat speeds.
//     * If a boat passes a mark(not the finish) its current and next mark
//     * will be updated and it's new heading as well as the mark it passed
//     * will be reported.
//     * If a boat crosses the finish, this will be reported and the boat
//     * will be added to the finishers list.
//     */
//    private void checkBoatPostions() {
//        for (Boat boat : startingList) {
//            if (!(boat.isFinished())){
//                Mark nextMark = boat.getNextMark();
//                double boatPosition = boat.getPosition();
//                if (boatPosition >= nextMark.getDistanceFromStart()){
//                    boat.setCurrentMark(boat.getNextMark());
//                    boat.setHeading(boat.getCurrentMark().getMarkHeading());
//                    int i = boat.getMarkList().indexOf(boat.getNextMark());
//
//                    if (i < boat.getMarkList().size() - 1) {
//                        boat.setNextMark(boat.getMarkList().get(i + 1));
//                    } else {
//                        boat.setFinished(true);
//                        finishedList.add(boat);
//                    }
//                    if (boat.getCurrentMark().getMarkHeading() != -1) {
//                        System.out.printf("%s has passed mark: %s with heading %d degrees\n\n",
//                                boat.getBoatName(), boat.getCurrentMark().getMarkName(), boat.getHeading());
//                    } else {
//                        System.out.printf("%s has finished the race!\n\n", boat.getBoatName());
//                    }
//                }
//                boat.setPosition(boat.getPosition() + 0.1 * knotsToMetersPerSecond(boat.getSpeed()));
//            }
//        }
//
//    }

//    public void updateBoats(float time) {
//        for (Boat boat : startingList) {
//            if (!(boat.isFinished())) {
//                CompoundMark nextCompoundMark = boat.getNextCompoundMark();
//                Coordinate markCoordinate = nextCompoundMark.getMarks().get(0).getMarkCoordinates();
//                Coordinate boatCoordinates = boat.getBoatCoordinates();
//                if (nextCompoundMark.getMarks().size() == 1) { //that is a mark
//                    // check if the boat has passed the mark
//                } else {
//                    // the compound mark is a gate, check if it has passed it
//                }
//
//            }
//        }
//    }

    public void updateBoats(double time) { // time in seconds
        for (Boat boat : startingList) {
            if (!(boat.isFinished())) {
                updateBoat(boat, time);
            }
        }
    }


    private void updateBoat(Boat boat, double time) {
        XYPair currentCoordinate = GPSCalculations.GPSxy(boat.getCoordinate());
        double speed = boat.getSpeed() * 1000 / 3600; // meters per second
        double distanceTravelled = speed * time; // meters
        double angle = 450 - boat.getCurrentLeg().getHeading(); // convert heading to actual angle
        if (angle > 360) {
            angle -= 360;
        } else if (angle < 0) {
            angle += 360;
        }
        System.out.println("angle = " + angle);
        System.out.println("distance travelled  = " + distanceTravelled);
        double nextX = currentCoordinate.getX() + distanceTravelled * Math.cos(angle);
        double nextY = currentCoordinate.getY() + distanceTravelled * Math.sin(angle);
        XYPair nextCoordinate = new XYPair(nextX, nextY);
        System.out.println();
        System.out.println("Current = " + currentCoordinate.getX() + "    " + currentCoordinate.getY());
        System.out.println("next = " + nextCoordinate.getX() + "    " + nextCoordinate.getY());
        System.out.println();
        boat.setCoordinate(GPSCalculations.XYToCoordinate(nextCoordinate));
    }



//    /**
//     * Display each boat in the race and their position at each mark.
//     */
//    void runRace() {
//        System.out.println("Start");
//        int loopTime = 100; // Boats are checked and updated 10 times per second
//
//        while (finishedList.size() < startingList.size()) {
//            final long startTime = System.currentTimeMillis();
//            checkBoatPostions();
//            final long endTime = System.currentTimeMillis();
//            try {
//                Thread.sleep(loopTime - (endTime - startTime));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        viewFinishOrder();
//        //System.exit(0);
//    }
}
