package seng302;

import java.util.ArrayList;

public class App {

    /**
     * The main method for the application
     */
    public static void main(String[] args) {

        FileReader fileReader = new FileReader();
        ArrayList<Boat> ac35 = fileReader.readBoatListFile("testAc35.txt");
//        System.out.println(ac35);

        Course testCourse = new Course("testCourse.txt");
        testCourse.displayCourse();

    }
}





