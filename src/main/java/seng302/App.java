package seng302;

import java.util.ArrayList;

public class App {

    /**
     * The main method for the application
     */
    public static void main(String[] args) {

        FileReader fileReader = new FileReader();
        ArrayList<Boat> ac35 = fileReader.readBoatListFile("testAc35.txt");
        System.out.println(ac35);

//        // create a new Course, and then set each mark in the course to have the boats from ac35
//        Course course = new Course("testCourse.txt");
//
//        for (Mark mark : course.getMarks()) {
//            mark.setBoats(ac35);
//        }
//
//        Race race = new Race(ac35, course);
//        race.runRace();


        Course testCourse = new Course("testCourse.txt");
        testCourse.displayCourse();
    }
}





