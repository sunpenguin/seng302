package seng302;

import java.util.ArrayList;
import java.util.Collections;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class App {

    public static void main(String[] args) {

        ArrayList<Boat> ac35 = new ArrayList<>();
        Boat emirates = new Boat("Emirates", "New Zealand");
        Boat oracle = new Boat("Oracle", "USA");
        Boat artemis = new Boat("Artemis", "Sweden");
        Boat groupama = new Boat("Groupama", "France");
        Boat landRover = new Boat("Land Rover", "Britain");
        Boat softBank = new Boat("SoftBank", "Japan");

        Race raceTest = new Race();
        ac35.add(emirates);
        ac35.add(oracle);
        ac35.add(artemis);
        ac35.add(groupama);
        ac35.add(landRover);
        ac35.add(softBank);
        Collections.shuffle(ac35);
        raceTest.addBoat(ac35.get(0));
        raceTest.addBoat(ac35.get(1));
        raceTest.viewFinishOrder();

        ArrayList<String> courseList = new ArrayList<>();

        try {

            File f = new File("/home/cosc/student/jth102/302/project302/team-18/testCourse.txt");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String courseMark = "";

            while ((courseMark = b.readLine()) != null) {
                courseList.add(courseMark);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}





