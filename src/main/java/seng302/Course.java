package seng302;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by csl62 on 3/03/17.
 */
public class Course {

    ArrayList<Mark> marks = new ArrayList<>();

    public Course(ArrayList<Mark> marks) {
        this.marks = marks;
    }



    public void displayCourse(){
        for(Mark mark : marks){
            System.out.println(mark.getMarkName());
        }
    }

}
