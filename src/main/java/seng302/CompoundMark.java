package seng302;

import java.util.ArrayList;

/**
 * Created by csl62 on 15/03/17.
 */
public class CompoundMark {

    private String name;
    private ArrayList<Mark> marks;

    public CompoundMark(String name, ArrayList<Mark> marks) {
        this.name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Mark> getMarks() {
        return marks;
    }

    public void setMarks(ArrayList<Mark> marks) {
        this.marks = marks;
    }
}
