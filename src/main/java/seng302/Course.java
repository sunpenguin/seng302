package seng302;

import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * A class which represents a course that is used in a race event
 */
class Course {

    private ArrayList<CompoundMark> compoundMarks = new ArrayList<>();

    public Course(ArrayList<CompoundMark> marks) {
        this.compoundMarks = marks;
    }

    public void setCompoundMarks(ArrayList<CompoundMark> marks) {
        this.compoundMarks = marks;
    }

//    /**
//     * Construct the course by reading the file given to the constructor
//     * and creating the marks
//     */
//    private void constructCourse() {
//        String csvFile = courseFilePath;
//        String line;
//        String csvSplitBy = ",";
//        float markDistance;
//        float distanceFromStart = 0; // Initialise to 0, add to this as marks are read from file
//        int markPosition =  0; // the first mark added is in position 0 as it is the start
//        int markHeading;
//
//        InputStream f = Course.class.getClass().getResourceAsStream(csvFile);
//
//        try (BufferedReader b = new BufferedReader(new InputStreamReader(f))) {
//            while ((line = b.readLine()) != null) {
//                String[] markInfo = line.split(csvSplitBy);
//                markDistance = Float.parseFloat(markInfo[1]);
//                markHeading = Integer.parseInt(markInfo[2]);
//                distanceFromStart += markDistance;
//                marks.add(new Mark(markInfo[0], markDistance, distanceFromStart, markPosition, markHeading));
//                markPosition += 1; // Update mark position for next mark to be added to the course
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * A getter for the CompoundMarks in the course
     * @return the Arraylist of CompoundMarks
     */
    ArrayList<CompoundMark> getCompoundMarks() {
        return compoundMarks;
    }


//    /**
//     * A method which displays all of the marks included in this course
//     */
//    void displayCourse(){
//        System.out.println("--------------------------COURSE DESCRIPTION-----------------------------------");
//        for(Mark mark : marks){
//            System.out.printf("Markname: %s, Length of leg: %.2f, Distance from start to end of leg: %.2f, Position in course: %d\n",
//                    mark.getMarkName(), mark.getmarkDistance(), mark.getDistanceFromStart(), mark.getMarkPosition());
//        }
//        System.out.println("-------------------------------------------------------------------------------");
//    }
}
