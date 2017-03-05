package seng302;

import java.util.ArrayList;

public class App {

    /**
     * The main method for the application
     */
    public static void main(String[] args) {

        ArrayList<Boat> ac35 = new ArrayList<>();

        Boat emirates = new Boat("Emirates", "New Zealand");
        Boat oracle = new Boat("Oracle", "USA");
        Boat artemis = new Boat("Artemis", "Sweden");
        Boat groupama = new Boat("Groupama", "France");
        Boat landRover = new Boat("Land Rover", "Britain");
        Boat softBank = new Boat("SoftBank", "Japan");

        ac35.add(emirates);
        ac35.add(oracle);
        ac35.add(artemis);
        ac35.add(groupama);
        ac35.add(landRover);
        ac35.add(softBank);

        // create a new Course, and then set each mark in the course to have the boats from ac35
        Course course = new Course("testCourse.txt");

        for (Mark mark : course.getMarks()) {
            mark.setBoats(ac35);
        }

        Race race = new Race(ac35, course);
        race.runRace();
    }
}





