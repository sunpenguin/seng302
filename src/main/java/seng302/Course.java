package seng302;

import java.util.ArrayList;


/**
 * A class which represents a course that is used in a race event
 */
public class Course {

    ArrayList<Mark> marks = new ArrayList<>();

    /**
     * A constructor for the Course class
     * @param marks An arrayList of marks for the course
     */
    public Course(ArrayList<Mark> marks) {
        this.marks = marks;
    }

    /**
     * A method which displays all of the marks included in this course
     */
    public void displayCourse(){
        for(Mark mark : marks){
            System.out.println(mark.getMarkName());
        }
    }

}
