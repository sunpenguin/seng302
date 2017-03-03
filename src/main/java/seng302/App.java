package seng302;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

        ArrayList<String> courseList = new ArrayList<>();

        try {

            File f = new File("/home/cosc/student/csl62/Desktop/Seng302/team-18/testCourse.txt");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String courseMark;

            while ((courseMark = b.readLine()) != null) {
                courseList.add(courseMark);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Mark> marks = new ArrayList<>();

        for (String s : courseList) {

            marks.add(new Mark(ac35, s));
        }

        Course course = new Course(marks);

        Race race = new Race(ac35, course);
        race.runRace();
    }
}





