package seng302;

import java.util.ArrayList;

public class App {

    /**
     * The main method for the application
     */
    public static void main(String[] args) {

        FileReader fileReader = new FileReader();
        ArrayList<Boat> ac35 = fileReader.readBoatListFile("testAc35_1.txt");
        Course testCourse = new Course("testCourse.txt");
        Race race = new Race(ac35, testCourse);
        double speed = 40;
        for (Boat boat : ac35) {
            boat.setSpeed(speed);
            speed += 5;
        }
        ac35.get(3).setSpeed(100);

        System.out.println("-----------------------------COMPETITORS---------------------------------------");
        race.viewStartingList();
        System.out.println("-------------------------------------------------------------------------------");

        System.out.println("--------------------------COURSE DESCRIPTION-----------------------------------");
        testCourse.displayCourse();
        System.out.println("-------------------------------------------------------------------------------");

        race.runRace();

    }
}





