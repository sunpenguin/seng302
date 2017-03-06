package seng302;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A class which represents a course that is used in a race event
 */
class Course {

    private String courseFilePath;
    private ArrayList<Mark> marks = new ArrayList<>();


    /**
     * A constructor for the Course class
     * @param courseFilePath file path to a course definition
     */
    Course(String courseFilePath) {
        this.courseFilePath = courseFilePath;
        constructCourse();
    }


    /**
     * Construct the course by reading the file given to the constructor
     * and creating the marks
     */
    private void constructCourse() {
        String csvFile = courseFilePath;
        String line;
        String csvSplitBy = ",";
        float markDistance;
        float distanceFromStart = 0; // Initialise to 0, add to this as marks are read from file
        int markPosition =  1; // the first mark added is in position 1

        try (BufferedReader b = new BufferedReader(new FileReader(csvFile))) {
            while ((line = b.readLine()) != null) {
                String[] markInfo = line.split(csvSplitBy);
                markDistance = Float.parseFloat(markInfo[1]);
                distanceFromStart += markDistance;
                marks.add(new Mark(markInfo[0], markDistance, distanceFromStart, markPosition));
                markPosition += 1; // Update mark position for next mark to be added to the course
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * A getter for the marks in the course
     * @return the Arraylist of marks
     */
    ArrayList<Mark> getMarks() {
        return marks;
    }


    /**
     * A method which displays all of the marks included in this course
     */
    void displayCourse(){
        for(Mark mark : marks){
            System.out.printf("Markname: %s, Length of leg: %.2f, Distance from start to end of leg: %.2f, Position in course: %d\n",
                    mark.getMarkName(), mark.getmarkDistance(), mark.getDistanceFromStart(), mark.getMarkPosition());
        }
    }
}
